package com.github.weijj0528.example.cache.service.impl;

import com.github.weijj0528.example.cache.entity.UserInfo;
import com.github.weijj0528.example.cache.service.WeiCacheService;
import com.wei.starter.cache.CacheTtlContext;
import com.wei.starter.cache.annotation.CacheTtl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;


/**
 * The type Wei cache service.
 * 默认Spring cache 没有实现缓存过期时间
 * 这里演示使用自定义注解实现缓存过期时间
 *
 * @author William.Wei
 * @see com.wei.starter.cache.annotation.CacheTtl
 */
@Service("ttlCacheService")
public class TtlCacheServiceImpl implements WeiCacheService {

    /**
     * 缓存过期时间演示
     *
     * @param query the query
     * @return
     */
    @CacheTtl(600L)
    @Cacheable(value = "ttl_cache", key = "#query.id")
    @Override
    public UserInfo addCache(UserInfo query) {
        System.out.println("addCache = " + query);
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("addCache");
        return userInfo;
    }

    /**
     * 重设缓存过期时间演示
     *
     * @param query the query
     * @return
     */
    @CacheTtl(600)
    @CachePut(value = "ttl_cache", key = "#query.id")
    @Override
    public UserInfo updateCache(UserInfo query) {
        System.out.println("updateCache = " + query);
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("updateCache");
        // 动态重设缓存过期时间
        CacheTtlContext.resetTtl(Duration.parse("PT1H").getSeconds());
        return userInfo;
    }

    @CacheEvict(value = "ttl_cache", key = "#query.id")
    @Override
    public void removeCache(UserInfo query) {
        System.out.println("removeCache = " + query);
    }

}
