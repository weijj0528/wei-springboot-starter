package com.wei.starter.mybatis.plugin;

import org.apache.ibatis.mapping.ParameterMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * SqlCostInterceptor#formatSql 单元测试
 * <p>
 * 覆盖 Matcher.quoteReplacement 修复: 参数值中含正则元字符 $ 或 \ 时,
 * 必须按字面量替换占位符, 而不被 replaceFirst 当作分组引用/转义处理。
 * <p>
 * 修复前 (直接传 value 给 replaceFirst): "$1" 会被当作分组引用 -> IllegalArgumentException,
 * 被 formatSql 内部 catch 后返回未替换占位符的 SQL。
 * 修复后 (Matcher.quoteReplacement(value)): $ 与 \ 被转义, 按字面量替换。
 * <p>
 * 注: 走通用对象路径 (handleCommonParameter) 触发, 该路径同样使用 Matcher.quoteReplacement。
 * 不使用 HashMap 作为 parameterObject —— isStrictMap 实现存在缺陷
 * (parameterObjectClass.isAssignableFrom(StrictMap.class) 方向写反, StrictMap extends HashMap,
 * 导致任意 HashMap 被误判为 StrictMap 后 ClassCastException), 会绕过 handleMapParameter。
 */
@DisplayName("SqlCostInterceptor#formatSql")
class SqlCostInterceptorTest {

    private SqlCostInterceptor interceptor;

    private Method formatSql;

    @BeforeEach
    void setUp() throws Exception {
        interceptor = new SqlCostInterceptor();
        formatSql = SqlCostInterceptor.class.getDeclaredMethod(
                "formatSql", String.class, Object.class, List.class);
        formatSql.setAccessible(true);
    }

    /**
     * 反射调用私有 formatSql, 解包 InvocationTargetException 以便断言直接看到真实异常。
     */
    @SuppressWarnings("unchecked")
    private String formatSql(String sql, Object parameterObject, List<ParameterMapping> mappings) {
        try {
            return (String) formatSql.invoke(interceptor, sql, parameterObject, mappings);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new AssertionError("formatSql 抛出受检异常: " + cause, cause);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("formatSql 反射调用失败", e);
        }
    }

    private ParameterMapping mapping(String property) {
        ParameterMapping pm = mock(ParameterMapping.class);
        when(pm.getProperty()).thenReturn(property);
        return pm;
    }

    /**
     * 通用参数对象, 字段名需与 ParameterMapping#getProperty() 一致。
     * handleCommonParameter 通过反射 getDeclaredField + setAccessible 读取字段值。
     */
    @SuppressWarnings("unused")
    static final class Param {
        private String name;

        Param(String name) {
            this.name = name;
        }
    }

    @Test
    @DisplayName("参数值含 $ 时按字面量替换, 不抛异常(Matcher.quoteReplacement 修复)")
    void formatSql_valueWithDollarSign_replacedLiterally() {
        String sql = "SELECT * FROM t WHERE name = ?";
        Param param = new Param("$100");

        String result = formatSql(sql, param, List.of(mapping("name")));

        // $ 被当作字面量而非正则分组引用, 占位符被正确替换
        assertEquals("SELECT * FROM t WHERE name = '$100'", result);
    }

    @Test
    @DisplayName("参数值含 \\ 时按字面量替换, 不抛异常(Matcher.quoteReplacement 修复)")
    void formatSql_valueWithBackslash_replacedLiterally() {
        String sql = "SELECT * FROM t WHERE name = ?";
        Param param = new Param("a\\b");

        String result = formatSql(sql, param, List.of(mapping("name")));

        // \ 被当作字面量而非转义符, 占位符被正确替换
        assertEquals("SELECT * FROM t WHERE name = 'a\\b'", result);
    }

    @Test
    @DisplayName("参数值同时含 $ 与 \\ 时按字面量替换")
    void formatSql_valueWithDollarAndBackslash_replacedLiterally() {
        String sql = "SELECT * FROM t WHERE name = ?";
        Param param = new Param("a$1\\b");

        String result = formatSql(sql, param, List.of(mapping("name")));

        assertEquals("SELECT * FROM t WHERE name = 'a$1\\b'", result);
    }

    @Test
    @DisplayName("无参数映射时返回美化后的 SQL(不替换占位符)")
    void formatSql_noMappings_returnsBeautifiedSql() {
        String sql = "SELECT *\n  FROM   t";

        String result = formatSql(sql, null, Collections.emptyList());

        assertEquals("SELECT * FROM t", result);
    }

    @Test
    @DisplayName("parameterObject 为 null 时直接返回美化后的 SQL")
    void formatSql_nullParameterObject_returnsBeautifiedSql() {
        String sql = "SELECT *\nFROM t WHERE id = ?";

        String result = formatSql(sql, null, List.of(mapping("id")));

        assertEquals("SELECT * FROM t WHERE id = ?", result);
    }

    @Test
    @DisplayName("空 SQL 返回空字符串")
    void formatSql_emptySql_returnsEmpty() {
        String result = formatSql("", new Param("x"), Collections.emptyList());

        assertEquals("", result);
    }
}
