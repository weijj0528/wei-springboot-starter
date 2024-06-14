package com.wei.starter.lock.impl;

import com.wei.starter.lock.WeiLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson实现的分布式锁
 *
 * @author William
 * @date 2019 /4/1
 */
@Slf4j
public class RedissonLock implements WeiLock {

    /**
     * 默认过期时间，秒
     */
    private static final long DEFAULT_EXPIRED_TIME = 3;
    /**
     * 过期时间
     */
    private final long expiredTime;
    /**
     * 等待时间
     */
    private final long waitTime;
    /**
     * 内部锁
     */
    private final RLock lock;
    /**
     * 开始锁定时间
     */
    private long lockStartTime = 0;

    /**
     * Instantiates a new Redisson lock.
     *
     * @param lockKey  the lock key
     * @param redisson the redisson
     */
    public RedissonLock(String lockKey, Redisson redisson) {
        this(lockKey, 0, DEFAULT_EXPIRED_TIME, redisson);
    }

    /**
     * Instantiates a new Redisson lock.
     *
     * @param lockKey     the lock key
     * @param waitTime    the wait time
     * @param expiredTime the expired time
     * @param redisson    the redisson
     */
    public RedissonLock(String lockKey, long waitTime, long expiredTime, Redisson redisson) {
        this.expiredTime = expiredTime;
        this.waitTime = waitTime;
        lock = redisson.getLock(lockKey);

    }

    @Override
    public void lock() {
        boolean b = tryLock();
        if (!b) {
            throw new RuntimeException("Did not get a lock:" + lock.getName());
        }
    }

    @Override
    public void lockInterruptibly() {
        unlock();
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(expiredTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Did not get a lock:" + lock.getName());
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
    @Override
    public boolean tryLock(long time, long expirationTime, TimeUnit unit) throws InterruptedException {
        boolean tryLock = lock.tryLock(time, expirationTime, unit);
        if (tryLock) {
            lockStartTime = System.currentTimeMillis();
        }
        return tryLock;
    }

    @Override
    public void unlock() {
        if (lockStartTime > 0) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            long time = System.currentTimeMillis() - lockStartTime;
            log.debug(lock.getName() + " locking " + time + "ms");
        }
    }
}
