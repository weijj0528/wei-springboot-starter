package com.wei.springbootstarterexample.controller;

import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import com.wei.springbootstarterexample.dto.SysIdDto;
import com.wei.springbootstarterexample.service.SysIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 * @createTime 2019-10-24 21:42:09
 * @description
 */
@Controller
@RequestMapping("/sysId")
public class SysIdController {

    @Autowired
    private SysIdService sysIdService;

    @ResponseBody
    @PostMapping
    public Result save(@RequestBody @Validated(Add.class) SysIdDto saveDto) {
        sysIdService.save(saveDto);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        sysIdService.delete(id);
        return Result.success();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody @Validated(Update.class) SysIdDto updateDto) {
        updateDto.setId(id);
        sysIdService.update(updateDto);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Result details(@PathVariable Long id) {
        SysIdDto details = sysIdService.details(id);
        return Result.success(details);
    }

    @ResponseBody
    @GetMapping
    public Result list(SysIdDto sysIdDto, Page page) {
        sysIdService.list(sysIdDto, page);
        return Result.success(page);
    }

}
