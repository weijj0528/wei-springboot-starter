package com.wei.springbootstarterexample.service;

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.service.BaseService;
import com.wei.springbootstarterexample.dto.SysIdDto;
import com.wei.springbootstarterexample.model.SysId;

import java.util.List;

/**
 * @author
 * @createTime 2019-10-24 21:33:27
 * @description
 */
public interface SysIdService extends BaseService<SysId> {

    /**
     * Save int.
     *
     * @param dto the sysId dto
     * @return the int
     */
    int save(SysIdDto dto);

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
     * @param dto the sysId dto
     * @return the int
     */
    int update(SysIdDto dto);

    /**
     * Details
     *
     * @param id the id
     * @return the
     */
    SysIdDto details(Object id);

    /**
     * List list.
     *
     * @param dto
     * @param page
     * @return the list
     */
    List<SysIdDto> list(SysIdDto dto, Page page);

    void updateByPrimaryKeySelective(SysId sysId);

    /**
     * Query by sys name and biz type com id.
     *
     * @param sysName the sys name
     * @param bizKey  the biz key
     * @return the com id
     */
    SysId queryBySysNameAndBizType(String sysName, String bizKey);

    /**
     * Add new com id com id.
     *
     * @param sysName the sys name
     * @param bizKey  the biz key
     * @return the com id
     */
    SysId addNewComId(String sysName, String bizKey);

}
