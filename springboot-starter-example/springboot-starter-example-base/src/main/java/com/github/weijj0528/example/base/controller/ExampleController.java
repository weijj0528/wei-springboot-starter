package com.github.weijj0528.example.base.controller;

import com.github.weijj0528.example.base.service.UserInfoService;
import com.wei.starter.base.bean.Result;
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
@Api(tags = "示例控制器")
@Slf4j
@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "Hello world")
    @GetMapping("/hello")
    public Result<Void> hello() {
        Result<Void> resultBean = new Result<>();
        resultBean.setCode("20000");
        resultBean.setMsg("Hello world!");
        return resultBean;
    }

    @ApiOperation(value = "Hello cache", notes = "测试缓存")
    @GetMapping("/cache")
    public Result<Void> cacheTest() {
        System.out.println("Hello cache!");
        return Result.success();
    }
}
