package com.github.weijj0528.example.lock.service.impl;

import com.github.weijj0528.example.lock.entity.UserInfo;
import com.github.weijj0528.example.lock.service.WeiLockService;
import com.wei.starter.lock.annotation.Lock;
import com.wei.starter.lock.annotation.Locking;
import org.redisson.Redisson;
import org.springframework.stereotype.Service;

@Service
public class WeiLockServiceImpl implements WeiLockService {

    private Redisson redisson;

    @Lock(lockName = "update_user", key = "#query.id", waitTime = 3, expiredTime = 3)
    @Locking({
            @Lock(lockName = "update_userinfo", key = "#query.id", waitTime = 3, expiredTime = 3),
            @Lock(lockName = "update_user_extends", key = "#query.id", waitTime = 3, expiredTime = 3)
    })
    @Override
    public void updateUser(UserInfo query) {
        System.out.println(redisson);
        System.out.println("updateUser = " + query);
    }
}
