package com.wei.starter.lock.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * LockService 单元测试
 * <p>
 * 覆盖: 构造接受 RedissonClient 接口(不抛 ClassCastException);
 * getExpression 通过 computeIfAbsent 缓存已解析 SpEL(同 key 仅解析一次)。
 */
@DisplayName("LockService 契约")
class LockServiceTest {

    @Test
    @DisplayName("构造接受 RedissonClient 接口实例(不抛 ClassCastException)")
    void constructor_acceptsRedissonClientInterface() {
        RedissonClient client = mock(RedissonClient.class);
        assertDoesNotThrow(() -> new LockService(client));
    }

    @Test
    @DisplayName("getExpression 缓存已解析 SpEL: 同一 key 仅解析一次")
    void getExpression_cachesParsedSpel() throws Exception {
        LockService lockService = new LockService((RedissonClient) null);

        // 注入 mock parser 以便校验 parseExpression 调用次数
        SpelExpressionParser mockParser = mock(SpelExpressionParser.class);
        Expression mockExpr = mock(Expression.class);
        when(mockParser.parseExpression("#id")).thenReturn(mockExpr);
        Field parserField = LockService.class.getDeclaredField("parser");
        parserField.setAccessible(true);
        parserField.set(lockService, mockParser);

        Method getExpression = LockService.class.getDeclaredMethod("getExpression", String.class);
        getExpression.setAccessible(true);

        Expression first = (Expression) getExpression.invoke(lockService, "#id");
        Expression second = (Expression) getExpression.invoke(lockService, "#id");

        assertSame(mockExpr, first, "首次调用应返回解析结果");
        assertSame(mockExpr, second, "第二次应命中缓存返回同一实例");
        verify(mockParser, times(1)).parseExpression("#id");
    }
}
