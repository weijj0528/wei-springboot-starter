package com.wei.springbootstarterexample.service;

import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import com.wei.springbootstarterexample.model.UserInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserInfoServiceTest extends SpringbootStarterExampleApplicationTests {


    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void Test() {
        List<UserInfo> userInfos = userInfoService.testCache();
        List<UserInfo> userInfos1 = userInfoService.testCache();
        System.out.println(userInfos);
        System.out.println(userInfos1);
    }

}