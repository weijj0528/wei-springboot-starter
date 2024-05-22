package com.github.weijj0528.example.cache.service;


import com.github.weijj0528.example.cache.entity.UserInfo;

/**
 * The interface Wei cache service.
 *
 * @author William.Wei
 */
public interface WeiCacheService {

    /**
     * Add cache user info.
     *
     * @param query the query
     * @return the user info
     */
    UserInfo addCache(UserInfo query);

    /**
     * Update cache user info.
     *
     * @param query the query
     * @return the user info
     */
    UserInfo updateCache(UserInfo query);

    /**
     * Remove cache.
     *
     * @param query the query
     */
    void removeCache(UserInfo query);

}
