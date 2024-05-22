package com.wei.starter.lock.impl;

import cn.hutool.core.lang.Assert;
import com.wei.starter.lock.WeiLock;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * The type Multiple lock.
 * 多重锁
 *
 * @author William.Wei
 */
public class MultipleLock implements WeiLock {

    private final List<WeiLock> lockList;

    public MultipleLock(List<WeiLock> lockList) {
        Assert.notEmpty(lockList, "The lock is empty!");
        this.lockList = lockList;
    }

    @Override
    public void lock() {
        lockList.forEach(Lock::lock);
    }

    @Override
    public void unlock() {
        lockList.forEach(Lock::unlock);
    }
}
