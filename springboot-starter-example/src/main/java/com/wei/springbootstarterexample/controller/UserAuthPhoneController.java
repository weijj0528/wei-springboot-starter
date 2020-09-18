package com.wei.springbootstarterexample.controller;

import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import com.wei.springbootstarterexample.dto.UserAuthPhoneDto;
import com.wei.springbootstarterexample.service.UserAuthPhoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
@Api(tags = "(UserAuthPhone)前端控制器")
@Controller
@RequestMapping("/userAuthPhone")
public class UserAuthPhoneController {

    @Autowired
    private UserAuthPhoneService userAuthPhoneService;

    @ApiOperation(value = "Save", notes = "新增")
    @ResponseBody
    @PostMapping
    public Result<Void> save(@RequestBody @Validated(Add.class) UserAuthPhoneDto saveDto) {
        userAuthPhoneService.save(saveDto);
        return Result.success();
    }

    @ApiOperation(value = "Delete", notes = "删除")
    @ResponseBody
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userAuthPhoneService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "Update", notes = "更新")
    @ResponseBody
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated(Update.class) UserAuthPhoneDto updateDto) {
        updateDto.setId(id);
        userAuthPhoneService.update(updateDto);
        return Result.success();
    }

    @ApiOperation(value = "Details", notes = "详情查询")
    @ResponseBody
    @GetMapping("/{id}")
    public Result<UserAuthPhoneDto> details(@PathVariable Long id) {
        UserAuthPhoneDto details = userAuthPhoneService.details(id);
        return Result.success(details);
    }

    @ApiOperation(value = "Page", notes = "分页查询")
    @ResponseBody
    @GetMapping
    public Result<Page<UserAuthPhoneDto>> page(UserAuthPhoneDto queryDto, Page<UserAuthPhoneDto> page) {
        userAuthPhoneService.list(queryDto, page);
        return Result.success(page);
    }

}
