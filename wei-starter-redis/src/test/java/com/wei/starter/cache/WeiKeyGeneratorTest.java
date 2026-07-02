package com.wei.starter.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * WeiKeyGenerator 单元测试
 * <p>
 * 覆盖: 基本类型/包装类型/String 产生稳定键; 非基本类型回退 toString()(修复);
 * null 参数使用默认占位; 无参方法使用 NO_PARAM_KEY。
 */
@DisplayName("WeiKeyGenerator 键生成")
class WeiKeyGeneratorTest {

    private final WeiKeyGenerator generator = new WeiKeyGenerator();

    /** 测试目标对象 */
    static class SampleTarget {
        public void doSomething(Long id, String name) {
        }

        public void doOther(Object o) {
        }

        public void noArgs() {
        }
    }

    /** toString 回退测试用对象 */
    static class ToStringObj {
        private final String s;

        ToStringObj(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    @Test
    @DisplayName("基本类型/包装类型/String 参数产生稳定键(相同参数相同键)")
    void primitiveParams_produceStableKeys() throws Exception {
        Method m = SampleTarget.class.getMethod("doSomething", Long.class, String.class);
        SampleTarget target = new SampleTarget();

        Object k1 = generator.generate(target, m, 1L, "abc");
        Object k2 = generator.generate(target, m, 1L, "abc");

        assertEquals(k1, k2, "相同基本类型参数应产生相同键");
    }

    @Test
    @DisplayName("不同基本类型参数产生不同键")
    void differentPrimitiveParams_produceDifferentKeys() throws Exception {
        Method m = SampleTarget.class.getMethod("doSomething", Long.class, String.class);
        SampleTarget target = new SampleTarget();

        Object k1 = generator.generate(target, m, 1L, "abc");
        Object k2 = generator.generate(target, m, 2L, "abc");

        assertNotEquals(k1, k2, "不同参数应产生不同键");
    }

    @Test
    @DisplayName("非基本类型参数回退 toString(): 相同 toString 产生相同键(修复点)")
    void nonPrimitiveParams_fallbackToToString() throws Exception {
        Method m = SampleTarget.class.getMethod("doOther", Object.class);
        SampleTarget target = new SampleTarget();

        // 两个不同实例但 toString 相同
        Object k1 = generator.generate(target, m, new ToStringObj("payload"));
        Object k2 = generator.generate(target, m, new ToStringObj("payload"));

        assertEquals(k1, k2, "非基本类型应回退 toString() 并产生相同键");
    }

    @Test
    @DisplayName("非基本类型不同 toString 产生不同键")
    void nonPrimitiveParams_differentToString_produceDifferentKeys() throws Exception {
        Method m = SampleTarget.class.getMethod("doOther", Object.class);
        SampleTarget target = new SampleTarget();

        Object k1 = generator.generate(target, m, new ToStringObj("a"));
        Object k2 = generator.generate(target, m, new ToStringObj("b"));

        assertNotEquals(k1, k2);
    }

    @Test
    @DisplayName("null 参数使用 NULL_PARAM_KEY 占位, 产生稳定键")
    void nullParam_usesPlaceholder_stableKey() throws Exception {
        Method m = SampleTarget.class.getMethod("doOther", Object.class);
        SampleTarget target = new SampleTarget();

        Object k1 = generator.generate(target, m, (Object) null);
        Object k2 = generator.generate(target, m, (Object) null);

        assertEquals(k1, k2, "null 参数应产生稳定键");
    }

    @Test
    @DisplayName("无参方法使用 NO_PARAM_KEY, 产生稳定键")
    void noParamMethod_usesNoParamKey_stableKey() throws Exception {
        Method m = SampleTarget.class.getMethod("noArgs");
        SampleTarget target = new SampleTarget();

        Object k1 = generator.generate(target, m);
        Object k2 = generator.generate(target, m);

        assertEquals(k1, k2, "无参方法应产生稳定键");
    }

    @Test
    @DisplayName("生成键为 String 类型")
    void generatedKey_isString() throws Exception {
        Method m = SampleTarget.class.getMethod("doSomething", Long.class, String.class);
        Object key = generator.generate(new SampleTarget(), m, 1L, "x");
        assertEquals(String.class, key.getClass());
    }
}
