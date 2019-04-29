package com.wei.springbootstarterexample.service;

import com.wei.springboot.starter.service.BaseService;
import com.wei.springbootstarterexample.model.UserInfo;

import java.util.List;


/**
 * @author
 * @createTime 2019-04-10 16:53:42
 * @description
 */
public interface UserInfoService extends BaseService<UserInfo> {


    List<UserInfo> testCache();

}
