package com.wei.starter.base.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * WeiJsonUtils 单元测试
 * <p>
 * 覆盖 toJsonString 序列化、parse 反序列化、toOptionalJsonObject 空值/非法值兜底。
 */
@DisplayName("WeiJsonUtils")
class WeiJsonUtilsTest {

    public static class Sample {
        public String name;
        public int age;
    }

    @Nested
    @DisplayName("toJsonString")
    class ToJsonString {

        @Test
        @DisplayName("produces expected JSON for a plain object")
        void toJsonString_producesExpectedJson() {
            Sample sample = new Sample();
            sample.name = "alice";
            sample.age = 30;

            String json = WeiJsonUtils.toJsonString(sample);
            assertNotNull(json);

            ObjectNode node = WeiJsonUtils.toJsonObject(json);
            assertEquals("alice", node.get("name").asText());
            assertEquals(30, node.get("age").asInt());
        }

        @Test
        @DisplayName("excludes null fields (NON_NULL inclusion)")
        void toJsonString_excludesNullFields() {
            Sample sample = new Sample();
            sample.name = null;
            sample.age = 30;

            String json = WeiJsonUtils.toJsonString(sample);
            ObjectNode node = WeiJsonUtils.toJsonObject(json);

            assertFalse(node.has("name"));
            assertTrue(node.has("age"));
        }
    }

    @Nested
    @DisplayName("parse")
    class Parse {

        @Test
        @DisplayName("round-trips an object via toJsonString -> parse")
        void parse_roundTripsObject() {
            Sample sample = new Sample();
            sample.name = "bob";
            sample.age = 25;

            String json = WeiJsonUtils.toJsonString(sample);
            Sample parsed = WeiJsonUtils.parse(json, Sample.class);

            assertEquals("bob", parsed.name);
            assertEquals(25, parsed.age);
        }

        @Test
        @DisplayName("parse ignores unknown properties (FAIL_ON_UNKNOWN_PROPERTIES disabled)")
        void parse_ignoresUnknownProperties() {
            String json = "{\"name\":\"carol\",\"age\":40,\"extra\":\"unknown\"}";

            Sample parsed = WeiJsonUtils.parse(json, Sample.class);

            assertEquals("carol", parsed.name);
            assertEquals(40, parsed.age);
        }
    }

    @Nested
    @DisplayName("toOptionalJsonObject")
    class ToOptionalJsonObject {

        @Test
        @DisplayName("null string returns empty Optional without throwing")
        void toOptionalJsonObject_null_returnsEmpty() {
            Optional<ObjectNode> result = WeiJsonUtils.toOptionalJsonObject((String) null);

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("empty string returns empty Optional without throwing")
        void toOptionalJsonObject_empty_returnsEmpty() {
            Optional<ObjectNode> result = WeiJsonUtils.toOptionalJsonObject("");

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("invalid JSON returns empty Optional without throwing")
        void toOptionalJsonObject_invalidJson_returnsEmpty() {
            Optional<ObjectNode> result = WeiJsonUtils.toOptionalJsonObject("not valid json");

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("valid JSON returns populated Optional")
        void toOptionalJsonObject_validJson_returnsPresent() {
            Optional<ObjectNode> result = WeiJsonUtils.toOptionalJsonObject("{\"key\":\"value\"}");

            assertTrue(result.isPresent());
            assertEquals("value", result.get().get("key").asText());
        }
    }
}
