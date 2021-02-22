package com.github.weijj0528.example.cache.service;

import com.github.weijj0528.example.cache.CacheExampleApplicationTest;
import com.github.weijj0528.example.cache.entity.UserInfo;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

class WeiCacheServiceTest extends CacheExampleApplicationTest {

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