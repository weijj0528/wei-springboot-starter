package com.wei.starter.cache;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Cacheable(value = CacheAutoConfig.REDIS_CACHE, keyGenerator = "cacheKeyGenerator")
public @interface RedisCacheable {
}
