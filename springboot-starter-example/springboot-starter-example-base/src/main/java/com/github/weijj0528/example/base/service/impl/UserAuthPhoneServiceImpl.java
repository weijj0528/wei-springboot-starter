package com.github.weijj0528.example.base.service.impl;

import com.github.weijj0528.example.base.dto.UserAuthPhoneDto;
import com.github.weijj0528.example.base.mapper.UserAuthPhoneMapper;
import com.github.weijj0528.example.base.model.UserAuthPhone;
import com.github.weijj0528.example.base.service.UserAuthPhoneService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
        return getMapper().insertSelective(userAuthPhone);
    }

    /**
     * Delete int.
     *
     * @param id
     * @return the int
     */
    @Override
    public int delete(Object id) {
        return getMapper().deleteByPrimaryKey(id);
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
        return getMapper().updateByPrimaryKeySelective(userAuthPhone);
    }

    /**
     * Details userAuthPhone dto.
     *
     * @return the userAuthPhone dto
     */
    @Override
    public UserAuthPhoneDto details(Object id) {
        UserAuthPhone userAuthPhone = getMapper().selectByPrimaryKey(id);
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
        Example example = new Example(UserAuthPhone.class);
        Example.Criteria criteria = example.createCriteria();
        // TODO 查询条件组装
        Page<UserAuthPhone> userAuthPhonePage = new Page<>();
        userAuthPhonePage.setPage(page.getPage());
        userAuthPhonePage.setSize(page.getSize());
        selectPageByExample(example, userAuthPhonePage);
        List<UserAuthPhoneDto> userAuthPhoneDtoList = WeiBeanUtil.toList(userAuthPhonePage.getList(), UserAuthPhoneDto.class);
        page.setTotal(userAuthPhonePage.getTotal());
        page.setList(userAuthPhoneDtoList);
        return userAuthPhoneDtoList;
    }
}
