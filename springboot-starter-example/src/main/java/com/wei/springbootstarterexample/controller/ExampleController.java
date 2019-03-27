package com.wei.springbootstarterexample.controller;

import com.wei.springboot.starter.bean.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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


    @ApiOperation(value = "Hello world")
    @GetMapping("/hello")
    public ResultBean hello() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        resultBean.setMsg("Hello world!");
        return resultBean;
    }


}
