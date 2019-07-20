package com.wei.springbootstarterexample.controller;

import com.wei.springboot.starter.bean.ResultBean;
import com.wei.springboot.starter.valid.Add;
import com.wei.springboot.starter.valid.Update;
import com.wei.springbootstarterexample.dto.UserInfoDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @createTime 2019/7/20 15:09
 * @description 用户相关
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @ResponseBody
    @PostMapping("/add")
    public ResultBean add(@RequestBody @Validated(Add.class) UserInfoDto userInfoDto) {
        return ResultBean.success();
    }

    @ResponseBody
    @PostMapping("/update")
    public ResultBean update(@RequestBody @Validated(Update.class) UserInfoDto userInfoDto) {
        return ResultBean.success();
    }

}
