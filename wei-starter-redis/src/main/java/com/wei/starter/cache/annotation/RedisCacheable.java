package com.wei.starter.cache.annotation;

import com.wei.starter.cache.CacheAutoConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wei.starter.cache.CacheAutoConfig.CM_REDIS;
import static com.wei.starter.cache.CacheAutoConfig.DEFAULT_KEY_GENERATOR;

/**
 * 增强 Cacheable
 * 指定 缓存管理器 和 缓存键生成器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Cacheable(value = CacheAutoConfig.REDIS_CACHE, cacheManager = CM_REDIS, keyGenerator = DEFAULT_KEY_GENERATOR)
public @interface RedisCacheable {
    @AliasFor(annotation = Cacheable.class, attribute = "value")
    String[] value() default {};


    @AliasFor(annotation = Cacheable.class, attribute = "cacheNames")
    String[] cacheNames() default {};


    @AliasFor(annotation = Cacheable.class, attribute = "key")
    String key() default "";


    @AliasFor(annotation = Cacheable.class, attribute = "keyGenerator")
    String keyGenerator() default "";


    @AliasFor(annotation = Cacheable.class, attribute = "cacheResolver")
    String cacheResolver() default "";


    @AliasFor(annotation = Cacheable.class, attribute = "condition")
    String condition() default "";


    @AliasFor(annotation = Cacheable.class, attribute = "unless")
    String unless() default "";


    @AliasFor(annotation = Cacheable.class, attribute = "sync")
    boolean sync() default false;

}
