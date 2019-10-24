package com.wei.springbootstarterexample.service.impl;

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.service.AbstractService;
import com.wei.springbootstarterexample.dto.UserInfoDto;
import com.wei.springbootstarterexample.model.UserInfo;
import com.wei.springbootstarterexample.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type User info service.
 *
 * @author
 * @createTime 2019 -04-10 16:53:42
 * @description
 */
@Service
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService {

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(UserInfoDto dto) {
        UserInfo userInfo = dto.toModel();
        return mapper.insertSelective(userInfo);
    }

    /**
     * Delete int.
     *
     * @param id
     * @return the int
     */
    @Override
    public int delete(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * Update int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int update(UserInfoDto dto) {
        UserInfo userInfo = dto.toModel();
        return mapper.updateByPrimaryKeySelective(userInfo);
    }

    /**
     * Details user info dto.
     *
     * @return the user info dto
     */
    @Override
    public UserInfoDto details(Object id) {
        UserInfo userInfo = mapper.selectByPrimaryKey(id);
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.copyModel(userInfo);
        return userInfoDto;
    }

    /**
     * List list.
     *
     * @param userInfoDto
     * @param page
     * @return the list
     */
    @Override
    public List<UserInfoDto> list(UserInfoDto userInfoDto, Page page) {

        return page.getList();
    }
}
