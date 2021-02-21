package com.wei.springbootstarterexample.service.impl;

import com.wei.springbootstarterexample.model.UserInfo;
import com.wei.springbootstarterexample.service.WeiLockService;
import com.wei.starter.lock.annotation.Lock;
import com.wei.starter.lock.annotation.Locking;
import org.springframework.stereotype.Service;

@Service
public class WeiLockServiceImpl implements WeiLockService {

    @Lock(lockName = "update_user", key = "#query.id", waitTime = 3, expiredTime = 3)
    @Locking({
            @Lock(lockName = "update_userinfo", key = "#query.id", waitTime = 3, expiredTime = 3),
            @Lock(lockName = "update_user_extends", key = "#query.id", waitTime = 3, expiredTime = 3)
    })
    @Override
    public void updateUser(UserInfo query) {
        System.out.println("updateUser = " + query);
    }
}
