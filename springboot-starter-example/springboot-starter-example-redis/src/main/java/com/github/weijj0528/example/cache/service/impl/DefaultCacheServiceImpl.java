package com.github.weijj0528.example.cache.service.impl;

import com.github.weijj0528.example.cache.entity.UserInfo;
import com.github.weijj0528.example.cache.service.WeiCacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.wei.starter.cache.CacheAutoConfig.DEFAULT_KEY_GENERATOR;

/**
 * The type Wei cache service.
 * 默认KEY生成演示
 * 注意：缓存保存与更新删除 KEY 都可能会不一样，这取决于KEY生成策略
 *
 * @author William.Wei
 */
@Service("defaultCacheService")
public class DefaultCacheServiceImpl implements WeiCacheService {

    @Cacheable(value = "default_cache", keyGenerator = DEFAULT_KEY_GENERATOR)
    @Override
    public UserInfo addCache(UserInfo query) {
        System.out.println("addCache = " + query);
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("addCache");
        return userInfo;
    }

    @CachePut(value = "default_cache", keyGenerator = DEFAULT_KEY_GENERATOR)
    @Override
    public UserInfo updateCache(UserInfo query) {
        System.out.println("updateCache = " + query);
        final UserInfo userInfo = new UserInfo();
        userInfo.setName("updateCache");
        return userInfo;
    }

    @CacheEvict(value = "default_cache", keyGenerator = DEFAULT_KEY_GENERATOR)
    @Override
    public void removeCache(UserInfo query) {
        System.out.println("removeCache = " + query);
    }

}
