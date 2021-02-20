package com.wei.springbootstarterexample.service;

import com.wei.springbootstarterexample.model.UserInfo;

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
