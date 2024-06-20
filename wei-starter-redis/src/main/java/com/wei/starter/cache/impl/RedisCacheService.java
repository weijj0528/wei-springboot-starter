package com.wei.starter.cache.impl;

import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.cache.CacheService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.concurrent.TimeUnit;

/**
 * @author William
 * @Date 2019/3/22
 * @Description
 */
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

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
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(1000)
                .build();
        try (Cursor<String> scan = redisTemplate.scan(scanOptions)) {
            scan.forEachRemaining(redisTemplate::delete);
        } catch (Exception e) {
            throw new ErrorMsgException("缓存删除失败");
        }
    }

    @Override
    public void removeAll() {
        // redisTemplate.getConnectionFactory().getConnection().flushDb();
        throw new UnsupportedOperationException();
    }

    @Override
    public RedisTemplate<String, ?> redisTemplate() {
        return redisTemplate;
    }
}
