package com.wei.starter.cache;

import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * 重写 RedisCache 动态添加过期时间
 * 新增与更新时设置缓存过期时间
 *
 * @see WeiRedisCacheManager
 */
public class WeiRedisCache extends RedisCache {
    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration cacheConfig;

    protected WeiRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void put(@NonNull Object key, @Nullable Object value) {

        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {

            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                    name));
        }

        // add expire ttl
        Duration ttl = cacheConfig.getTtl();
        // 获取ThreadLocal上下文中的TTL
        long ctxTtl = CacheTtlContext.getTtl();
        if (ctxTtl > 0) {
            ttl = Duration.ofSeconds(ctxTtl);
        }

        this.cacheWriter.put(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue), ttl);
    }

    private byte[] createAndConvertCacheKey(Object key) {
        return this.serializeCacheKey(this.createCacheKey(key));
    }

    @Override
    public ValueWrapper putIfAbsent(@NonNull Object key, @Nullable Object value) {

        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }

        // add expire ttl
        Duration ttl = cacheConfig.getTtl();
        // 获取ThreadLocal上下文中的TTL
        Long ctxTtl = CacheTtlContext.getTtl();
        if (ctxTtl != null && ctxTtl > 0) {
            ttl = Duration.ofSeconds(ctxTtl);
        }

        byte[] result = cacheWriter.putIfAbsent(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue), ttl);
        if (result == null) {
            return null;
        }

        return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
    }
}
