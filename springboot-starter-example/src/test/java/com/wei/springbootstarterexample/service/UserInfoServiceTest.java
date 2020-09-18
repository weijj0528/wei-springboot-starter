package com.wei.springbootstarterexample.service;

import com.alibaba.fastjson.JSON;
import com.wei.starter.base.bean.Page;
import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import com.wei.springbootstarterexample.model.UserInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public class UserInfoServiceTest extends SpringbootStarterExampleApplicationTests {


    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void selectTest() {
        List<UserInfo> userInfos = userInfoService.selectByExample(new Example(UserInfo.class));
        System.out.println(JSON.toJSONString(userInfos));
    }

    @Test
    public void selectPageTest() {
        Page<UserInfo> page = new Page<>();
        Example example = new Example(UserInfo.class);
        userInfoService.selectPageByExample(example, page);
        System.out.println(JSON.toJSONString(page));
    }

}