package com.wei.starter.base.util;

import com.wei.starter.base.exception.ErrorMsgException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * WeiComUtils#operationRetry 单元测试
 * <p>
 * 覆盖首次成功、重试后成功、达到上限抛出异常三种场景。
 */
@DisplayName("WeiComUtils#operationRetry")
class WeiComUtilsTest {

    @Test
    @DisplayName("returns result when operation succeeds first try")
    void operationRetry_returnsResult_whenSucceedsFirstTry() {
        String result = WeiComUtils.operationRetry(() -> "ok", "test-key", 0, 3);

        assertEquals("ok", result);
    }

    @Test
    @DisplayName("retries and eventually succeeds when operation fails then succeeds")
    void operationRetry_retriesAndSucceeds_whenFailsThenSucceeds() {
        AtomicInteger counter = new AtomicInteger(0);

        String result = WeiComUtils.operationRetry(() -> {
            if (counter.incrementAndGet() < 3) {
                throw new RuntimeException("transient failure");
            }
            return "ok";
        }, "test-key", 0, 5);

        assertEquals("ok", result);
        assertEquals(3, counter.get(), "supplier should be invoked exactly 3 times");
    }

    @Test
    @DisplayName("does not retry indefinitely — bounded by max retries")
    void operationRetry_boundedByMaxRetries() {
        AtomicInteger counter = new AtomicInteger(0);

        assertThrows(ErrorMsgException.class, () ->
                WeiComUtils.operationRetry(() -> {
                    counter.incrementAndGet();
                    throw new RuntimeException("always fails");
                }, "test-key", 0, 3)
        );

        assertEquals(3, counter.get(), "supplier should be invoked exactly max times before giving up");
    }
}
