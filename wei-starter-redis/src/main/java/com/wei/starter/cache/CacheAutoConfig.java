package com.wei.starter.cache;

import com.wei.starter.cache.advice.CacheTtlAspect;
import com.wei.starter.cache.impl.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.math.BigInteger;
import java.time.Duration;

/**
 * @author Administrator
 * @createTime 2019/3/17 15:59
 * @description 缓存配置
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheAutoConfig {

    public static final String REDIS_CACHE = "REDIS_CACHE";

    /**
     * 默认缓存KEY生成器
     */
    public static final String DEFAULT_KEY_GENERATOR = "weiKeyGenerator";

    /**
     * Redis cache manager
     */
    public static final String CM_REDIS = "redisCacheManager";

    @Primary
    @Bean(DEFAULT_KEY_GENERATOR)
    public KeyGenerator weiKeyGenerator() {
        return new WeiKeyGenerator();
    }

    @Primary
    @Bean(CM_REDIS)
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .entryTtl(Duration.ofDays(BigInteger.ONE.intValue()));
        return new WeiRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), configuration);
    }

    @Bean
    public CacheService cacheService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisCacheService(redisTemplate);
    }

    @Bean
    public CacheTtlAspect cacheTtlAspect() {
        log.info("Cache aspect init");
        return new CacheTtlAspect();
    }

}
