package com.wei.starter.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * The type Json utils.
 *
 * @author Weijj0528
 */
public class WeiJsonUtils {
    private WeiJsonUtils() {
    }

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * Sets object mapper.
     *
     * @param objectMapper the object mapper
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    /**
     * Gets object mapper.
     *
     * @return the object mapper
     */
    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * 返回json字符串
     *
     * @param object the object
     * @return string string
     */
    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析成ObjectNode
     *
     * @param json 需要转换的json
     * @return ObjectNode object node
     * @throws RuntimeException 解析失败
     */
    public static ObjectNode toJsonObject(String json) {
        try {
            return mapper.readValue(json, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析json并返回Optional
     *
     * @param json the json
     * @return Optional
     */
    public static Optional<ObjectNode> toOptionalJsonObject(String json) {
        if (json == null || "".equals(json)) {
            return Optional.empty();
        }
        try {
            return Optional.of(mapper.readValue(json, ObjectNode.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 解析json并返回Optional
     *
     * @param json the json
     * @return Optional
     */
    public static Optional<ObjectNode> toOptionalJsonObject(byte[] json) {
        if (json == null || json.length == 0) {
            return Optional.empty();
        }
        try {
            return Optional.of(mapper.readValue(json, ObjectNode.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 解析成指定类
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return 指定类实例 t
     * @throws RuntimeException 解析失败时
     */
    public static <T> T parse(byte[] json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析成指定类
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return 指定类实例 t
     * @throws RuntimeException 解析失败时
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析成指定带泛型的类
     *
     * @param <T>       the type parameter
     * @param json      the json
     * @param reference the reference
     * @return t t
     * @throws RuntimeException 解析失败
     */
    public static <T> T parse(String json, TypeReference<T> reference) {
        try {
            return mapper.readValue(json, reference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析成指定类并返回Optional
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return Optional
     */
    public static <T> Optional<T> optionalParse(String json, Class<T> clazz) {
        try {
            return Optional.of(mapper.readValue(json, clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 解析成指定类并返回Optional
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return Optional
     */
    public static <T> Optional<T> optionalParse(byte[] json, Class<T> clazz) {
        try {
            return Optional.of(mapper.readValue(json, clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 解析成指定带泛型的类并返回Optional
     *
     * @param <T>       the type parameter
     * @param json      the json
     * @param reference the reference
     * @return Optional
     */
    public static <T> Optional<T> optionalParse(String json, TypeReference<T> reference) {
        try {
            return Optional.of(mapper.readValue(json, reference));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 解析成指定带泛型的类并返回Optional
     *
     * @param <T>       the type parameter
     * @param json      the json
     * @param reference the reference
     * @return Optional
     */
    public static <T> Optional<T> optionalParse(byte[] json, TypeReference<T> reference) {
        try {
            return Optional.of(mapper.readValue(json, reference));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 创建空的json对象
     *
     * @return object node
     */
    public static ObjectNode createObject() {
        return mapper.createObjectNode();
    }

    /**
     * 创建空的json array对象
     *
     * @return array node
     */
    public static ArrayNode createArray() {
        return mapper.createArrayNode();
    }

    /**
     * 节点转text
     *
     * @param node the node
     * @return 如果节点不存在返回null string
     */
    public static String asText(JsonNode node) {
        return node == null ? null : node.asText();
    }

    /**
     * 节点转text并返回Optional
     *
     * @param node the node
     * @return Optional
     */
    public static Optional<String> asOptionalText(JsonNode node) {
        return Optional.ofNullable(asText(node));
    }

    /**
     * As text string.
     *
     * @param node the node
     * @param key  the key
     * @return the string
     */
    public static String asText(JsonNode node, String key) {
        return node == null ? null : asText(node.get(key));
    }

    /**
     * As optional text optional.
     *
     * @param node the node
     * @param key  the key
     * @return the optional
     */
    public static Optional<String> asOptionalText(JsonNode node, String key) {
        return Optional.ofNullable(asText(node, key));
    }

    /**
     * As text or default string.
     *
     * @param node         the node
     * @param defaultValue the default value
     * @return the string
     */
    public static String asTextOrDefault(JsonNode node, String defaultValue) {
        return node == null ? defaultValue : node.asText();
    }

    /**
     * As text or default string.
     *
     * @param node         the node
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    public static String asTextOrDefault(JsonNode node, String key, String defaultValue) {
        return node == null ? defaultValue : asTextOrDefault(node.get(key), defaultValue);
    }

    /**
     * As optional.
     *
     * @param node the node
     * @param key  the key
     * @return the optional
     */
    public static Optional<JsonNode> asOptional(JsonNode node, String key) {
        return Optional.ofNullable(node == null ? null : node.get(key));
    }

    /**
     * As int int.
     *
     * @param node the node
     * @return the int
     */
    public static int asInt(JsonNode node) {
        if (node == null) {
            return 0;
        }
        if (node.canConvertToInt()) {
            return node.asInt();
        }
        return 0;
    }

    /**
     * As int or default int.
     *
     * @param node         the node
     * @param defaultValue the default value
     * @return the int
     */
    public static int asIntOrDefault(JsonNode node, int defaultValue) {
        if (node == null) {
            return defaultValue;
        }
        if (node.canConvertToInt()) {
            return node.asInt();
        }
        return defaultValue;
    }

    /**
     * As int int.
     *
     * @param node the node
     * @param key  the key
     * @return the int
     */
    public static int asInt(JsonNode node, String key) {
        return node == null ? 0 : asInt(node.get(key));
    }

    /**
     * As int or default int.
     *
     * @param node         the node
     * @param key          the key
     * @param defaultValue the default value
     * @return the int
     */
    public static int asIntOrDefault(JsonNode node, String key, int defaultValue) {
        return node == null ? defaultValue : asIntOrDefault(node.get(key), defaultValue);
    }

    /**
     * As array array node.
     *
     * @param node the node
     * @return the array node
     */
    public static ArrayNode asArray(JsonNode node) {
        if (node == null) {
            return createArray();
        }
        if (node.isArray()) {
            return ((ArrayNode) node);
        }
        return createArray().add(node);
    }
}

