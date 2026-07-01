package com.wei.starter.lock.impl;

import cn.hutool.core.util.IdUtil;
import com.wei.starter.lock.WeiLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * The type Redis lock.
 *
 * @author William
 * @Date 2019 /4/1
 * @Description 基于Redis实现的分布式锁
 */
@Slf4j
public class RedisLock implements WeiLock {

    /**
     * 默认过期时间，秒
     */
    private static final long DEFAULT_EXPIRED_TIME = 3;

    /**
     * 连接工厂
     */
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 锁头
     */
    private final String lockKey;

    /**
     * 锁芯
     */
    private final String lockValue;

    /**
     * 开始锁定时间
     */
    private long lockStartTime = 0;

    /**
     * 过期时间
     */
    private final long expiredTime;

    /**
     * 等待时间
     */
    private final long waitTime;

    /**
     * Instantiates a new Redis lock.
     *
     * @param lockKey                the lock key
     * @param redisConnectionFactory the redis connection factory
     */
    public RedisLock(String lockKey, RedisConnectionFactory redisConnectionFactory) {
        this(lockKey, 0, DEFAULT_EXPIRED_TIME, redisConnectionFactory);
    }

    /**
     * Instantiates a new Redis lock.
     *
     * @param lockKey                the lock key
     * @param waitTime               the wait time
     * @param expiredTime            the expired time
     * @param redisConnectionFactory the redis connection factory
     */
    public RedisLock(String lockKey, long waitTime, long expiredTime, RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.lockKey = lockKey;
        this.lockValue = IdUtil.simpleUUID();
        this.expiredTime = expiredTime;
        this.waitTime = waitTime;
    }

    private RedisConnection getRedisConnection() {
        return redisConnectionFactory.getConnection();
    }

    @Override
    public void lock() {
        try {
            // Lock.lock() 必须阻塞直到获取锁, 无超时上限
            while (!tryLock(expiredTime, expiredTime, TimeUnit.SECONDS)) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Did not get a lock:" + lockKey);
        }
    }

    @Override
    public void lockInterruptibly() {
        // 当前实现不支持可中断式加锁，请使用 tryLock(long, long, TimeUnit) 代替
        throw new UnsupportedOperationException("RedisLock does not support lockInterruptibly()");
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(expiredTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Did not get a lock:" + lockKey);
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(time, expiredTime, unit);
    }

    /**
     * Try lock boolean.
     * 偿试在指定时间内(秒)获取锁
     *
     * @param time           the time
     * @param expirationTime the expiration time
     * @param unit           the unit
     * @return the boolean
     * @throws InterruptedException the interrupted exception
     */
    @Override
    public boolean tryLock(long time, long expirationTime, TimeUnit unit) throws InterruptedException {
        // 获取连接
        RedisConnection redisConnection = getRedisConnection();
        // 过期时间: 亚秒级单位使用 PX (毫秒), 否则使用 EX (秒)
        boolean subSecond = unit == TimeUnit.MILLISECONDS || unit == TimeUnit.MICROSECONDS || unit == TimeUnit.NANOSECONDS;
        String expireUnit = subSecond ? "px" : "ex";
        long expireValue = subSecond ? unit.toMillis(expirationTime) : unit.toSeconds(expirationTime);
        byte[] expirationBytes = String.valueOf(expireValue).getBytes(StandardCharsets.UTF_8);
        Boolean set = Boolean.FALSE;
        // 自旋时间
        long targetTime = System.currentTimeMillis() + unit.toMillis(time);
        try {
            do {
                String script = "if redis.call('set', KEYS[1], ARGV[1], ARGV[2], ARGV[3], ARGV[4]) then return 1 else return 0 end";
                set = redisConnection.eval(script.getBytes(StandardCharsets.UTF_8), ReturnType.BOOLEAN,
                        1, lockKey.getBytes(StandardCharsets.UTF_8), lockValue.getBytes(StandardCharsets.UTF_8),
                        "nx".getBytes(StandardCharsets.UTF_8), expireUnit.getBytes(StandardCharsets.UTF_8), expirationBytes);
                if (Boolean.TRUE.equals(set)) {
                    break;
                }
                if (System.currentTimeMillis() < targetTime) {
                    // 未获取到则稍等一会再次获取
                    Thread.sleep(100);
                }
            } while (System.currentTimeMillis() < targetTime);
        } finally {
            redisConnection.close();
        }
        if (Boolean.TRUE.equals(set)) {
            lockStartTime = System.currentTimeMillis();
        }
        return set;
    }

    @Override
    public void unlock() {
        if (lockStartTime > 0) {
            RedisConnection redisConnection = getRedisConnection();
            try {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Boolean result = redisConnection.eval(script.getBytes(StandardCharsets.UTF_8), ReturnType.BOOLEAN, 1, lockKey.getBytes(StandardCharsets.UTF_8), lockValue.getBytes(StandardCharsets.UTF_8));
                if (!Boolean.TRUE.equals(result)) {
                    log.warn("Lock expired:" + lockKey);
                }
                long time = System.currentTimeMillis() - lockStartTime;
                log.debug("{} locking {}ms", lockKey, time);
            } finally {
                redisConnection.close();
            }
        }
    }
}
