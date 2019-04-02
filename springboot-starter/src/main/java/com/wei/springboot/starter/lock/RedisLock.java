package com.wei.springboot.starter.lock;

import com.wei.springboot.starter.config.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author William
 * @Date 2019/4/1
 * @Description 基于Redis实现的分布式锁
 */
@Slf4j
public class RedisLock implements Lock {

    /**
     * 锁头
     */
    private static final String LOCK = "LOCK:";

    /**
     * 默认过期时间，秒
     */
    private static final long Default_Expired_Time = 3;

    /**
     * 开始锁定时间
     */
    private long lockStartTime = 0;
    /**
     * 锁芯 ~ ha
     */
    private final String lockKey;

    public RedisLock(String lockKey) {
        this.lockKey = LOCK + lockKey;
    }

    private RedisConnection getRedisConnection() {
        RedisConnectionFactory factory = SpringContext.getBean(RedisConnectionFactory.class);
        return factory.getConnection();
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
            return tryLock(Default_Expired_Time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Did not get a lock:" + lockKey);
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(0, time, unit);
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
        long id = Thread.currentThread().getId();
        // 获取连接
        RedisConnection redisConnection = getRedisConnection();
        // 过期时间
        Expiration expiration = Expiration.from(expirationTime, unit);
        // 不存在则设置
        RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
        Boolean set;
        // 自旋时间
        long targetTime = System.currentTimeMillis() + (time * 1000);
        do {
            set = redisConnection.set(lockKey.getBytes(), String.valueOf(id).getBytes(), expiration, setOption);
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
        long id = Thread.currentThread().getId();
        RedisConnection redisConnection = getRedisConnection();
        byte[] bytes = redisConnection.get(lockKey.getBytes());
        if (bytes != null && String.valueOf(id).equals(new String(bytes))) {
            redisConnection.del(lockKey.getBytes());
            redisConnection.close();
        } else {
            if (lockStartTime > 0) {
                log.warn("Lock expired:" + lockKey);
            }
        }
        if (lockStartTime > 0) {
            long time = System.currentTimeMillis() - lockStartTime;
            log.debug(lockKey + " locking " + time + "ms");
        }
    }

    @Override
    public Condition newCondition() {
        throw new RuntimeException("RedisLock lockInterruptibly method is unrealized!");
    }
}
