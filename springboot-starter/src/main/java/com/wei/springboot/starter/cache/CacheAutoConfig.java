package com.wei.springboot.starter.cache;

import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Administrator
 * @createTime 2019/3/17 15:59
 * @description 缓存配置
 */
@Configuration
@EnableCaching
public class CacheAutoConfig {

    public static final String REDIS_CACHE = "REDIS_CACHE";

    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return new CacheKeyGenerator();
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, ?> template) {
        RedisCacheManager cacheManager = new RedisCacheManager(template);
        cacheManager.setCacheNames(Lists.newArrayList(REDIS_CACHE));
        cacheManager.setUsePrefix(true);
        cacheManager.setCachePrefix(new DefaultRedisCachePrefix("CACHE:"));
        cacheManager.setDefaultExpiration(10);
        return cacheManager;
    }

}
