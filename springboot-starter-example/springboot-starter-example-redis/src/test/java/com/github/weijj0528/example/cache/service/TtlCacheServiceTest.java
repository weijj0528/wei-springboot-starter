package com.github.weijj0528.example.cache.service;

import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.github.weijj0528.example.cache.entity.UserInfo;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

class TtlCacheServiceTest extends RedisExampleApplicationTest {

    @Resource
    private WeiCacheService ttlCacheService;

    @Test
    public void addCache() {
        final UserInfo query = new UserInfo();
        query.setId(1L);
        final UserInfo userInfo = ttlCacheService.addCache(query);
        System.out.println(userInfo);
    }

    @Test
    public void updateCache() {
        final UserInfo query = new UserInfo();
        query.setId(1L);
        final UserInfo userInfo = ttlCacheService.updateCache(query);
        System.out.println(userInfo);
    }

    @Test
    public void removeCache() {
        final UserInfo query = new UserInfo();
        query.setId(1L);
        ttlCacheService.removeCache(query);
    }
}