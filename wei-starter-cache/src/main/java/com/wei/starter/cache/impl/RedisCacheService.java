package com.wei.starter.cache.impl;

import com.wei.starter.cache.CacheService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author William
 * @Date 2019/3/22
 * @Description
 */
public class RedisCacheService implements CacheService {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, int expirationTime) {
        set(key, value, expirationTime, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value, int expirationTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expirationTime, timeUnit);
    }

    @Override
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    @Override
    public void removeAll() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Override
    public RedisTemplate<String, ?> redisTemplate() {
        return redisTemplate;
    }
}
