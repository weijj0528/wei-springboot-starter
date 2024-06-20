package com.wei.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存有效时长, 单位: 秒, 若写在类上则类中所有方法都继承此时间
 *
 * @see com.wei.starter.cache.advice.CacheTtlAspect
 * @see com.wei.starter.cache.CacheTtlContext
 * @see com.wei.starter.cache.WeiRedisCache
 * @see com.wei.starter.cache.WeiRedisCacheManager
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CacheTtl {
    /**
     * 缓存有效时长, 单位: 秒
     */
    long value() default -1;
}
