package com.github.weijj0528.example.security.controller;

import com.wei.starter.base.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author William
 * @Date 2019/3/27
 * @Description 示例控制器
 */
@Slf4j
@RestController
@RequestMapping("/example")
public class ExampleController {


    @GetMapping("/hello")
    public Result<Void> hello() {
        Result<Void> resultBean = new Result<>();
        resultBean.setCode("20000");
        resultBean.setMsg("Hello world!");
        return resultBean;
    }

    @GetMapping("/cache")
    public Result<Void> cacheTest() {
        System.out.println("Hello cache!");
        return Result.success();
    }
}
