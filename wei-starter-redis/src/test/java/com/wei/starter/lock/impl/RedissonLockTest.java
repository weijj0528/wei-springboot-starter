package com.wei.starter.lock.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * RedissonLock 单元测试
 * <p>
 * 覆盖契约修复: lockInterruptibly() 委派至 RLock.lockInterruptibly()(不再抛 UnsupportedOperationException);
 * tryLock(time, unit) 以 time 为等待、expiredTime 为租约; unlock() 未持锁时仅告警不调用 unlock。
 */
@DisplayName("RedissonLock 契约")
class RedissonLockTest {

    private RedissonClient redisson;
    private RLock rLock;

    @BeforeEach
    void setUp() {
        redisson = mock(RedissonClient.class);
        rLock = mock(RLock.class);
        when(redisson.getLock("k")).thenReturn(rLock);
        when(rLock.getName()).thenReturn("k");
    }

    @Test
    @DisplayName("构造时通过 RedissonClient 获取 RLock")
    void constructor_obtainsRLockFromClient() {
        new RedissonLock("k", redisson);
        verify(redisson).getLock("k");
    }

    @Test
    @DisplayName("lockInterruptibly() 委派至 RLock.lockInterruptibly()(不抛 UnsupportedOperationException)")
    void lockInterruptibly_delegatesToRLock() throws Exception {
        RedissonLock lock = new RedissonLock("k", redisson);

        assertDoesNotThrow(lock::lockInterruptibly);

        verify(rLock).lockInterruptibly();
    }

    @Test
    @DisplayName("tryLock(time, unit) 委派时 time 为等待, expiredTime 为租约")
    void tryLock_delegatesWithTimeAsWaitAndExpiredTimeAsLease() throws Exception {
        // waitTime=99(字段, 此处不应作为等待); expiredTime=7(租约字段)
        RedissonLock lock = new RedissonLock("k", 99, 7, redisson);
        when(rLock.tryLock(2, 7, TimeUnit.SECONDS)).thenReturn(true);

        // 调用 tryLock(2, SECONDS): time=2 应作为等待, expiredTime=7 作为租约
        boolean result = lock.tryLock(2, TimeUnit.SECONDS);

        assertTrue(result);
        verify(rLock).tryLock(2, 7, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("unlock() 未被当前线程持有时不调用 RLock.unlock()")
    void unlock_notHeldByCurrentThread_doesNotUnlock() throws Exception {
        RedissonLock lock = new RedissonLock("k", redisson);
        // 先成功加锁以置 lockStartTime > 0
        when(rLock.tryLock(0, 3, TimeUnit.SECONDS)).thenReturn(true);
        lock.tryLock(0, TimeUnit.SECONDS);
        // 此时 lockStartTime 已被设置

        // 模拟锁已不被当前线程持有(过期/释放)
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        lock.unlock();

        verify(rLock, never()).unlock();
    }

    @Test
    @DisplayName("unlock() 被当前线程持有时调用 RLock.unlock()")
    void unlock_heldByCurrentThread_unlocks() throws Exception {
        RedissonLock lock = new RedissonLock("k", redisson);
        when(rLock.tryLock(0, 3, TimeUnit.SECONDS)).thenReturn(true);
        lock.tryLock(0, TimeUnit.SECONDS);

        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        lock.unlock();

        verify(rLock).unlock();
    }
}
