package com.wei.springbootstarterexample.service;

import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import com.wei.springbootstarterexample.model.UserInfo;
import org.junit.Test;

import javax.annotation.Resource;

public class WeiCacheServiceTest extends SpringbootStarterExampleApplicationTests {

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