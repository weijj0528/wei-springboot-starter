package com.wei.springbootstarterexample.controller;

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.bean.Result;
import com.wei.springboot.starter.valid.Add;
import com.wei.springbootstarterexample.dto.UserInfoDto;
import com.wei.springbootstarterexample.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @createTime 2019/7/20 15:09
 * @description 用户相关接口
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @PostMapping
    public Result save(@RequestBody @Validated(Add.class) UserInfoDto userInfoDto) {
        userInfoService.save(userInfoDto);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        userInfoService.delete(id);
        return Result.success();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody @Validated(Add.class) UserInfoDto userInfoDto) {
        userInfoDto.setId(id);
        userInfoService.update(userInfoDto);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Result details(@PathVariable Long id) {
        UserInfoDto details = userInfoService.details(id);
        return Result.success(details);
    }

    @ResponseBody
    @GetMapping
    public Result list(@RequestParam UserInfoDto userInfoDto, @RequestParam Page page) {
        userInfoService.list(userInfoDto, page);
        return Result.success(page);
    }

}
