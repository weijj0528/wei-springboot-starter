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
    default public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    default public boolean tryLock() {
        return false;
    }

    @Override
    default public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    default public Condition newCondition() {
        return null;
    }
}
