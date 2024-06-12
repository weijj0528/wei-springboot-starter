package com.github.weijj0528.example.mybatis.controller;

import com.github.weijj0528.example.mybatis.dto.UserAuthPhoneDto;
import com.github.weijj0528.example.mybatis.model.UserAuthPhone;
import com.github.weijj0528.example.mybatis.service.UserAuthPhoneService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

/**
 * 演示 Controller
 *
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
@Slf4j
@Controller
@RequestMapping("/userAuthPhone")
public class UserAuthPhoneController {

    @Autowired
    private UserAuthPhoneService userAuthPhoneService;

    /**
     * 新增演示
     *
     * @param saveDto 新增数据
     * @return
     */
    @ResponseBody
    @PostMapping
    public Result<Void> save(@RequestBody @Validated(Add.class) UserAuthPhoneDto saveDto) {
        userAuthPhoneService.save(saveDto);
        return Result.success();
    }

    /**
     * 更新演示
     *
     * @param id        主键
     * @param updateDto 更新数据
     * @return
     */
    @ResponseBody
    @PostMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated(Update.class) UserAuthPhoneDto updateDto) {
        updateDto.setId(id);
        userAuthPhoneService.update(updateDto);
        return Result.success();
    }

    /**
     * 删除演示
     *
     * @param id 主键
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userAuthPhoneService.delete(id);
        return Result.success();
    }

    /**
     * 详情查询演示
     *
     * @param id 主键
     * @return
     */
    @ResponseBody
    @GetMapping("/{id}")
    public Result<UserAuthPhoneDto> details(@PathVariable Long id) {
        UserAuthPhoneDto details = userAuthPhoneService.details(id);
        return Result.success(details);
    }

    /**
     * 分页查询演示
     *
     * @param queryDto 查询条件
     * @param page     分页
     * @return
     */
    @ResponseBody
    @GetMapping
    public Result<Page<UserAuthPhoneDto>> page(UserAuthPhoneDto queryDto, Page<UserAuthPhoneDto> page) {
        userAuthPhoneService.list(queryDto, page);
        return Result.success(page);
    }

    /**
     * 游标操作演示
     *
     * @param queryDto 查询条件
     * @return
     */
    @ResponseBody
    @GetMapping("/cursor")
    public Result<Page<UserAuthPhoneDto>> cursor(UserAuthPhoneDto queryDto) {
        Example example = new Example(UserAuthPhone.class);
        Example.Criteria criteria = example.createCriteria();
        // criteria.andEqualTo(UserAuthPhone.ID, 1);
        userAuthPhoneService.cursorOperator("selectByExample", 10, example, list -> {
            list.forEach(o -> log.info("cursor record: {}", o));
        });
        return Result.success();
    }

}
