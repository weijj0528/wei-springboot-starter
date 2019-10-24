package com.wei.springbootstarterexample.controller;

import com.wei.springboot.starter.bean.Result;
import com.wei.springboot.starter.cache.RedisCacheable;
import com.wei.springbootstarterexample.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author William
 * @Date 2019/3/27
 * @Description 示例控制器
 */
@Api(description = "示例控制器")
@Slf4j
@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "Hello world")
    @GetMapping("/hello")
    public Result hello() {
        Result resultBean = new Result();
        resultBean.setCode("20000");
        resultBean.setMsg("Hello world!");
        return resultBean;
    }

    @ApiOperation(value = "Hello cache", notes = "测试缓存")
    @RedisCacheable
    @GetMapping("/cache")
    public Result cacheTest() {
        System.out.println("Hello cache!");
        return new Result();
    }
}
