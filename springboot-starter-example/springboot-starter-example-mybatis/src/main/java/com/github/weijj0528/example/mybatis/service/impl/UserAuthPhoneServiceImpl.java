package com.github.weijj0528.example.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.weijj0528.example.mybatis.dto.UserAuthPhoneDto;
import com.github.weijj0528.example.mybatis.mapper.UserAuthPhoneMapper;
import com.github.weijj0528.example.mybatis.model.UserAuthPhone;
import com.github.weijj0528.example.mybatis.service.UserAuthPhoneService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
@Service
public class UserAuthPhoneServiceImpl extends AbstractService<UserAuthPhone> implements UserAuthPhoneService {

    @Resource
    private UserAuthPhoneMapper userAuthPhoneMapper;

    @Override
    public XMapper<UserAuthPhone> getMapper() {
        return userAuthPhoneMapper;
    }

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(UserAuthPhoneDto dto) {
        UserAuthPhone userAuthPhone = WeiBeanUtil.toBean(dto, UserAuthPhone.class);
        return insertSelective(userAuthPhone);
    }

    /**
     * Delete int.
     *
     * @param id
     * @return the int
     */
    @Override
    public int delete(Serializable id) {
        return deleteByPrimaryKey(id);
    }

    /**
     * Update int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int update(UserAuthPhoneDto dto) {
        UserAuthPhone userAuthPhone = WeiBeanUtil.toBean(dto, UserAuthPhone.class);
        return updateByPrimaryKeySelective(userAuthPhone);
    }

    /**
     * Details userAuthPhone dto.
     *
     * @return the userAuthPhone dto
     */
    @Override
    public UserAuthPhoneDto details(Serializable id) {
        UserAuthPhone userAuthPhone = selectByPrimaryKey(id);
        return WeiBeanUtil.toBean(userAuthPhone, UserAuthPhoneDto.class);
    }

    /**
     * List list.
     *
     * @param queryDto
     * @param page
     * @return the list
     */
    @Override
    public List<UserAuthPhoneDto> list(UserAuthPhoneDto queryDto, Page<UserAuthPhoneDto> page) {
        QueryWrapper<UserAuthPhone> wrapper = new QueryWrapper<>(UserAuthPhone.class);
        // TODO 查询条件组装
        wrapper.checkSqlInjection();
        Page<UserAuthPhone> userAuthPhonePage = new Page<>();
        userAuthPhonePage.setPage(page.getPage());
        userAuthPhonePage.setSize(page.getSize());
        selectPageByExample(wrapper, userAuthPhonePage);
        List<UserAuthPhoneDto> userAuthPhoneDtoList = WeiBeanUtil.toList(userAuthPhonePage.getList(), UserAuthPhoneDto.class);
        page.setTotal(userAuthPhonePage.getTotal());
        page.setList(userAuthPhoneDtoList);
        return userAuthPhoneDtoList;
    }
}
