package com.wei.springbootstarterexample.service.impl;

import com.wei.springbootstarterexample.model.UserInfo;
import com.wei.springbootstarterexample.service.WeiCacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * The type Wei cache service.
 *
 * @author William.Wei
 */
@Service
public class WeiCacheServiceImpl implements WeiCacheService {

    @Cacheable(value = "user_info", key = "#query.id")
    @Override
    public UserInfo addCache(UserInfo query) {
        System.out.println("addCache = " + query);
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("addCache");
        return userInfo;
    }

    @CachePut(value = "user_info", key = "#query.id")
    @Override
    public UserInfo updateCache(UserInfo query) {
        System.out.println("updateCache = " + query);
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("updateCache");
        return userInfo;
    }

    @CacheEvict(value = "user_info", key = "#query.id")
    @Override
    public void removeCache(UserInfo query) {
        System.out.println("removeCache = " + query);
    }
}
