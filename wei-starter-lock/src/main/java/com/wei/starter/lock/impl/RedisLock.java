package com.wei.starter.lock.impl;

import cn.hutool.core.util.IdUtil;
import com.wei.starter.lock.WeiLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * @author William
 * @Date 2019/4/1
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
    private long expiredTime;

    /**
     * 等待时间
     */
    private long waitTime;

    public RedisLock(String lockKey, RedisConnectionFactory redisConnectionFactory) {
        this(lockKey, 0, DEFAULT_EXPIRED_TIME, redisConnectionFactory);
    }

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
        boolean b = tryLock();
        if (!b) {
            throw new RuntimeException("Did not get a lock:" + lockKey);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("RedisLock lockInterruptibly method is unrealized!");
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(expiredTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Did not get a lock:" + lockKey);
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(waitTime, time, unit);
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
    public boolean tryLock(long time, long expirationTime, TimeUnit unit) throws InterruptedException {
        // 获取连接
        RedisConnection redisConnection = getRedisConnection();
        // 过期时间
        Expiration expiration = Expiration.from(expirationTime, unit);
        byte[] expirationBytes = String.valueOf(expiration.getExpirationTimeInSeconds()).getBytes();
        Boolean set;
        // 自旋时间
        long targetTime = System.currentTimeMillis() + (time * 1000);
        do {
            String script = "if redis.call('set', KEYS[1], ARGV[1], ARGV[2], ARGV[3], ARGV[4]) then return 1 else return 0 end";
            set = redisConnection.eval(script.getBytes(), ReturnType.BOOLEAN,
                    1, lockKey.getBytes(), lockValue.getBytes(),
                    "nx".getBytes(), "ex".getBytes(), expirationBytes);
            if (set) {
                break;
            }
            if (System.currentTimeMillis() < targetTime) {
                // 未获取到则稍等一会再次获取
                Thread.sleep(100);
            }
        } while (System.currentTimeMillis() < targetTime);
        redisConnection.close();
        if (set) {
            lockStartTime = System.currentTimeMillis();
        }
        return set;
    }

    @Override
    public void unlock() {
        if (lockStartTime > 0) {
            RedisConnection redisConnection = getRedisConnection();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Boolean result = redisConnection.eval(script.getBytes(), ReturnType.BOOLEAN, 1, lockKey.getBytes(), lockValue.getBytes());
            if (!result) {
                log.warn("Lock expired:" + lockKey);
            }
            long time = System.currentTimeMillis() - lockStartTime;
            log.debug(lockKey + " locking " + time + "ms");
            redisConnection.close();
        }
    }

    @Override
    public Condition newCondition() {
        throw new RuntimeException("RedisLock newCondition method is unrealized!");
    }
}
