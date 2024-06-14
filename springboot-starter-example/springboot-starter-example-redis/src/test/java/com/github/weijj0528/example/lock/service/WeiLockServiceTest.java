package com.github.weijj0528.example.lock.service;

import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.github.weijj0528.example.lock.entity.UserInfo;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

class WeiLockServiceTest extends RedisExampleApplicationTest {

    @Resource
    private WeiLockService weiLockService;

    @Test
    public void updateUser() {
        final UserInfo query = new UserInfo();
        query.setId(1000L);
        weiLockService.updateUser(query);
    }

}