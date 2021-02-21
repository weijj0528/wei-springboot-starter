package com.wei.springbootstarterexample.service;

import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import com.wei.springbootstarterexample.model.UserInfo;
import org.junit.Test;

import javax.annotation.Resource;

public class WeiLockServiceTest extends SpringbootStarterExampleApplicationTests {

    @Resource
    private WeiLockService weiLockService;

    @Test
    public void updateUser() {
        final UserInfo query = new UserInfo();
        query.setId(1000L);
        weiLockService.updateUser(query);
    }
}