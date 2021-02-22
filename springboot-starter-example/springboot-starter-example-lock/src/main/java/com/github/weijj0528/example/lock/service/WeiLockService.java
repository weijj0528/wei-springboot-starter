package com.github.weijj0528.example.lock.service;


import com.github.weijj0528.example.lock.entity.UserInfo;

/**
 * The interface Wei cache service.
 *
 * @author William.Wei
 */
public interface WeiLockService {

    /**
     * Add cache user info.
     *
     * @param query the query
     * @return the user info
     */
    void updateUser(UserInfo query);

}
