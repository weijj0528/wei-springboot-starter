package com.wei.starter.lock;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * The type Wei lock.
 *
 * @author William.Wei
 */
public interface WeiLock extends Lock {

    @Override
    default void lockInterruptibly() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean tryLock() {
        return false;
    }

    @Override
    default boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * Try lock boolean.
     *
     * @param waitTime the wait time
     * @param time     the time
     * @param unit     the unit
     * @return the boolean
     * @throws InterruptedException the interrupted exception
     */
    default boolean tryLock(long waitTime, long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    default Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
