package com.github.weijj0528.example.swagger2.controller;

import com.github.weijj0528.example.swagger2.dto.QueryDto;
import com.wei.starter.base.bean.Result;
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
@Slf4j
@Api(tags = {"演示接口模块", "演示模块分组"})
@RestController
@RequestMapping("/example2")
public class Example2Controller {

    @ApiOperation(value = "接口标题", notes = "接口详细说明……", tags = {"接口分组"})
    @GetMapping("/hello")
    public Result<Void> hello(QueryDto query) {
        Result<Void> resultBean = new Result<>();
        resultBean.setCode("20000");
        resultBean.setMsg("Hello world!");
        return resultBean;
    }
}
