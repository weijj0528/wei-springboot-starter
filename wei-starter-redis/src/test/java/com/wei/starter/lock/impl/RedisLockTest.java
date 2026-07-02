package com.wei.starter.lock.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * RedisLock 单元测试
 * <p>
 * 覆盖分布式锁契约修复: tryLock(time, unit) 中 time 为等待预算、expiredTime 为租约;
 * lock() 阻塞重试; 中断恢复中断标志; Boolean 返回值空安全。
 */
@DisplayName("RedisLock 契约")
class RedisLockTest {

    private RedisConnectionFactory factory;
    private RedisConnection connection;

    @BeforeEach
    void setUp() {
        factory = mock(RedisConnectionFactory.class);
        connection = mock(RedisConnection.class);
        when(factory.getConnection()).thenReturn(connection);
    }

    @Test
    @DisplayName("tryLock(time, unit) 委派时 time 作为等待预算, expiredTime 作为租约")
    @Timeout(5)
    void tryLock_delegatesWithTimeAsWaitAndExpiredTimeAsLease() throws InterruptedException {
        // expiredTime=5 作为租约; time=0 作为等待预算(仅尝试一次, 便于精确校验租约参数)
        RedisLock lock = new RedisLock("k", 99, 5, factory);

        when(connection.eval(any(), any(), anyInt(), any())).thenReturn(Boolean.FALSE);

        lock.tryLock(0, TimeUnit.SECONDS);

        // 验证租约参数 = expiredTime(5), 而非 time(0)
        // varargs: capture 组件类型 byte[], getAllValues 返回全部变参元素
        org.mockito.ArgumentCaptor<byte[]> captor =
                org.mockito.ArgumentCaptor.forClass(byte[].class);
        verify(connection, org.mockito.Mockito.times(1)).eval(any(), any(), anyInt(), captor.capture());
        java.util.List<byte[]> args = captor.getAllValues();
        // keysAndArgs: [lockKey, lockValue, "nx", expireUnit, expirationBytes]
        org.junit.jupiter.api.Assertions.assertEquals(5, args.size(),
                "应包含 5 个变参元素: lockKey, lockValue, nx, expireUnit, expirationBytes");
        String leaseArg = new String(args.get(4), StandardCharsets.UTF_8);
        org.junit.jupiter.api.Assertions.assertEquals("5", leaseArg,
                "租约(expirationBytes)应取自 expiredTime=5, 而非 time=0; 证明 time 为等待预算, expiredTime 为租约");
    }

    @Test
    @DisplayName("lock() 在首次获取失败后重试, 直到成功(不立即抛出)")
    @Timeout(5)
    void lock_retriesUntilAcquired() {
        // expiredTime=1s 使循环快速; 首两次失败, 第三次成功
        RedisLock lock = new RedisLock("k", 0, 1, factory);
        when(connection.eval(any(), any(), anyInt(), any()))
                .thenReturn(Boolean.FALSE)
                .thenReturn(Boolean.FALSE)
                .thenReturn(Boolean.TRUE);

        lock.lock();

        // 至少调用 3 次, 证明发生了重试而非首次失败即返回/抛出
        verify(connection, atLeast(3)).eval(any(), any(), anyInt(), any());
    }

    @Test
    @DisplayName("lock() 被 sleep 中断时, 恢复中断标志并以 RuntimeException 重新抛出")
    @Timeout(5)
    void lock_interruptedDuringSleep_restoresInterruptAndRethrows() {
        RedisLock lock = new RedisLock("k", 0, 1, factory);
        when(connection.eval(any(), any(), anyInt(), any())).thenReturn(Boolean.FALSE);

        // 预置中断标志: 进入 lock() 后 Thread.sleep(100) 会立即抛 InterruptedException
        Thread.currentThread().interrupt();

        try {
            RuntimeException ex = assertThrows(RuntimeException.class, lock::lock);
            assertTrue(ex.getMessage().contains("Did not get a lock"),
                    "应抛出包含锁键的 RuntimeException");
            // catch 块已调用 Thread.currentThread().interrupt() 恢复标志
            assertTrue(Thread.currentThread().isInterrupted(),
                    "中断标志应被恢复");
        } finally {
            // 清理: 避免影响后续测试
            Thread.interrupted();
        }
    }

    @Test
    @DisplayName("eval 返回 null 时 tryLock 应优雅返回 false(空安全)")
    void tryLock_evalReturnsNull_returnsFalseGracefully() throws InterruptedException {
        // eval 返回 null 时不应 NPE, 应返回 false (Boolean.TRUE.equals(null) = false)
        RedisLock lock = new RedisLock("k", 0, 1, factory);
        when(connection.eval(any(), any(), anyInt(), any())).thenReturn(null);

        assertFalse(lock.tryLock(0, TimeUnit.SECONDS),
                "eval 返回 null 应返回 false 而非 NPE");
    }

    @Test
    @DisplayName("tryLock 成功获取锁返回 true")
    void tryLock_acquired_returnsTrue() throws InterruptedException {
        RedisLock lock = new RedisLock("k", 0, 3, factory);
        when(connection.eval(any(), any(), anyInt(), any())).thenReturn(Boolean.TRUE);

        assertTrue(lock.tryLock(1, TimeUnit.SECONDS));
        verify(connection, times(1)).eval(any(), any(), anyInt(), any());
        verify(connection).close();
    }

    @Test
    @DisplayName("tryLock 等待预算耗尽且未获取时返回 false")
    void tryLock_waitBudgetExhausted_returnsFalse() throws InterruptedException {
        // time=0: 等待预算为 0, 仅尝试一次
        RedisLock lock = new RedisLock("k", 0, 3, factory);
        when(connection.eval(any(), any(), anyInt(), any())).thenReturn(Boolean.FALSE);

        assertFalse(lock.tryLock(0, TimeUnit.SECONDS));
        verify(connection, times(1)).eval(any(), any(), anyInt(), any());
    }
}
