package com.wei.springbootstarterexample.service.impl;

import com.wei.springboot.starter.cache.RedisCacheable;
import com.wei.springboot.starter.service.AbstractService;
import com.wei.springbootstarterexample.model.UserInfo;
import com.wei.springbootstarterexample.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @createTime 2019-04-10 16:53:42
 * @description
 */
@Service
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService {

    @Override
    @RedisCacheable
    public List<UserInfo> testCache() {
        List<UserInfo> userInfos = selectAll();
        return userInfos;
    }
}
