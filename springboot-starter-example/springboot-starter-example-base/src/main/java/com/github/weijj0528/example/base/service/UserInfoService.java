package com.github.weijj0528.example.base.service;

import com.github.weijj0528.example.base.dto.UserInfoDto;
import com.github.weijj0528.example.base.model.UserInfo;
import com.wei.starter.base.bean.Page;
import com.wei.starter.mybatis.service.BaseService;

import java.util.List;


/**
 * The interface User info service.
 *
 * @author
 * @createTime 2019 -04-10 16:53:42
 * @description
 */
public interface UserInfoService extends BaseService<UserInfo> {


    /**
     * Save int.
     *
     * @param dto the dto
     * @return the int
     */
    int save(UserInfoDto dto);

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     */
    int delete(Object id);

    /**
     * Update int.
     *
     * @param dto the dto
     * @return the int
     */
    int update(UserInfoDto dto);

    /**
     * Details user info dto.
     *
     * @param id the id
     * @return the user info dto
     */
    UserInfoDto details(Object id);

    /**
     * List list.
     *
     * @return the list
     * @param userInfoDto
     * @param page
     */
    List<UserInfoDto> list(UserInfoDto userInfoDto, Page page);

}