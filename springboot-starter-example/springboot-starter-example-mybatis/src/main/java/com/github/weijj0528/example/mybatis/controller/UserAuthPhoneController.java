package com.github.weijj0528.example.mybatis.controller;

import com.github.weijj0528.example.mybatis.dto.UserAuthPhoneDto;
import com.github.weijj0528.example.mybatis.service.UserAuthPhoneService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
@Controller
@RequestMapping("/userAuthPhone")
public class UserAuthPhoneController {

    @Autowired
    private UserAuthPhoneService userAuthPhoneService;

    @ResponseBody
    @PostMapping
    public Result<Void> save(@RequestBody @Validated(Add.class) UserAuthPhoneDto saveDto) {
        userAuthPhoneService.save(saveDto);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userAuthPhoneService.delete(id);
        return Result.success();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated(Update.class) UserAuthPhoneDto updateDto) {
        updateDto.setId(id);
        userAuthPhoneService.update(updateDto);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Result<UserAuthPhoneDto> details(@PathVariable Long id) {
        UserAuthPhoneDto details = userAuthPhoneService.details(id);
        return Result.success(details);
    }

    @ResponseBody
    @GetMapping
    public Result<Page<UserAuthPhoneDto>> page(UserAuthPhoneDto queryDto, Page<UserAuthPhoneDto> page) {
        userAuthPhoneService.list(queryDto, page);
        return Result.success(page);
    }

}
