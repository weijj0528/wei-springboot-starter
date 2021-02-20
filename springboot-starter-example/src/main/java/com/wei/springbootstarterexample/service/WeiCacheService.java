package com.wei.springbootstarterexample.service;

import com.wei.springbootstarterexample.model.UserInfo;

/**
 * The interface Wei cache service.
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
