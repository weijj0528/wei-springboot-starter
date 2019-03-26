package com.wei.springboot.starter.cache;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
    public CacheManager cacheManager(RedisConnectionFactory factory,
                                     FastJsonRedisSerializer fastJsonRedisSerializer) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
        RedisCacheManager cacheManager = RedisCacheManager
                .builder(factory)
                .cacheDefaults(configuration)
                .build();
        return cacheManager;
    }

}
