package com.github.weijj0528.example.cache.service;

import com.github.weijj0528.example.cache.entity.UserInfo;
import com.github.weijj0528.example.redis.RedisExampleApplicationTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

class WeiCacheServiceTest extends RedisExampleApplicationTest {

    @Resource
    private WeiCacheService weiCacheService;

    @Test
    public void addCache() {
        final UserInfo query = new UserInfo();
        query.setId(1L);
        final UserInfo userInfo = weiCacheService.addCache(query);
        System.out.println(userInfo);
    }

    @Test
    public void updateCache() {
        final UserInfo query = new UserInfo();
        query.setId(1L);
        final UserInfo userInfo = weiCacheService.updateCache(query);
        System.out.println(userInfo);
    }

    @Test
    public void removeCache() {
        final UserInfo query = new UserInfo();
        query.setId(1L);
        weiCacheService.removeCache(query);
    }
}