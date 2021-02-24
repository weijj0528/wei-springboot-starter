package com.github.weijj0528.example.mybatis.service;

import com.github.weijj0528.example.mybatis.dto.UserAuthPhoneDto;
import com.github.weijj0528.example.mybatis.model.UserAuthPhone;
import com.wei.starter.base.bean.Page;
import com.wei.starter.mybatis.service.BaseService;

import java.util.List;

/**
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
public interface UserAuthPhoneService extends BaseService<UserAuthPhone> {

    /**
     * Save int.
     *
     * @param dto the userAuthPhone dto
     * @return the int
     */
    int save(UserAuthPhoneDto dto);

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
     * @param dto the userAuthPhone dto
     * @return the int
     */
    int update(UserAuthPhoneDto dto);

    /**
     * Details
     *
     * @param id the id
     * @return the
     */
    UserAuthPhoneDto details(Object id);

    /**
     * List list.
     *
     * @return the list
     * @param queryDto
     * @param page
     */
    List<UserAuthPhoneDto> list(UserAuthPhoneDto queryDto, Page<UserAuthPhoneDto> page);

}
