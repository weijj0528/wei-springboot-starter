package com.wei.starter.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;

/**
 * 重写 RedisCacheManager 支持动态添加过期时间
 *
 * @see WeiRedisCache
 */
public class WeiRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfiguration;

    public WeiRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    @NonNull
    @Override
    protected RedisCache createRedisCache(@NonNull String name, RedisCacheConfiguration cacheConfig) {
        return new WeiRedisCache(name, this.cacheWriter, cacheConfig != null ? cacheConfig : this.defaultCacheConfiguration);
    }

}
