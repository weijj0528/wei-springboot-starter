package com.wei.starter.mybatis.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * 基础实体 equals/hashCode 单元测试
 * <p>
 * 覆盖 id 为 null 时退化为引用相等的修复，避免两个未持久化实体被误判为相等。
 */
@DisplayName("Entity<T> equals/hashCode")
class EntityTest {

    @Nested
    @DisplayName("equals")
    class Equals {

        @Test
        @DisplayName("两个 id 相同(非空)的实体相等")
        void sameId_areEqual() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();
            a.setId(1L);
            b.setId(1L);

            assertEquals(a, b);
            assertEquals(b, a);
        }

        @Test
        @DisplayName("两个 id 不同(非空)的实体不相等")
        void differentId_areNotEqual() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();
            a.setId(1L);
            b.setId(2L);

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("两个 id 均为 null 的实体不相等(退化为引用相等)")
        void bothNullId_areNotEqual_identityFallback() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("实体与自身相等(引用相等)")
        void self_isEqual() {
            Entity<Long> a = new Entity<>();
            assertEquals(a, a);

            a.setId(5L);
            assertEquals(a, a);
        }

        @Test
        @DisplayName("一个 null id 一个非 null id 不相等")
        void oneNullId_areNotEqual() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();
            b.setId(1L);

            assertNotEquals(a, b);
            assertNotEquals(b, a);
        }

        @Test
        @DisplayName("传入 null 返回 false")
        void nullArg_returnsFalse() {
            Entity<Long> a = new Entity<>();
            a.setId(1L);

            assertNotEquals(a, null);
        }

        @Test
        @DisplayName("传入非 Entity 类型返回 false")
        void wrongType_returnsFalse() {
            Entity<Long> a = new Entity<>();
            a.setId(1L);

            assertNotEquals(a, "not an entity");
            assertNotEquals(a, 1L);
        }

        @Test
        @DisplayName("跨类型: 不同 Entity 子类即使 id 相同也不相等(getClass 校验)")
        void crossType_sameId_areNotEqual() {
            TestEntityA a = new TestEntityA();
            TestEntityB b = new TestEntityB();
            a.setId(1L);
            b.setId(1L);

            assertNotEquals(a, b);
            assertNotEquals(b, a);
        }
    }

    @Nested
    @DisplayName("hashCode")
    class HashCode {

        @Test
        @DisplayName("相同非空 id 产生相同 hashCode")
        void sameId_sameHashCode() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();
            a.setId(1L);
            b.setId(1L);

            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("不同非空 id 产生不同 hashCode")
        void differentId_differentHashCode() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();
            a.setId(1L);
            b.setId(2L);

            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("null id 使用身份哈希(System.identityHashCode)，不同实例各不相同")
        void nullId_usesIdentityHashCode() {
            Entity<Long> a = new Entity<>();
            Entity<Long> b = new Entity<>();

            assertEquals(System.identityHashCode(a), a.hashCode());
            assertEquals(System.identityHashCode(b), b.hashCode());
            // 关键: 两个未持久化实体不应落入同一桶
            assertNotEquals(a.hashCode(), b.hashCode());
        }
    }

    static final class TestEntityA extends Entity<Long> {
    }

    static final class TestEntityB extends Entity<Long> {
    }
}
