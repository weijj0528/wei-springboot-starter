package com.github.weijj0528.example.sequence.service;

import com.github.weijj0528.example.sequence.dto.SysIdDto;
import com.github.weijj0528.example.sequence.model.SysId;
import com.wei.starter.base.bean.Page;
import com.wei.starter.mybatis.service.BaseService;

import java.io.Serializable;
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
    int delete(Serializable id);

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
    SysIdDto details(Serializable id);

    /**
     * List list.
     *
     * @param dto
     * @param page
     * @return the list
     */
    List<SysIdDto> list(SysIdDto dto, Page page);

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
