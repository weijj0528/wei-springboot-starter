package com.wei.springbootstarterexample.service;

import com.wei.springboot.starter.service.BaseService;
import com.wei.springbootstarterexample.model.ComId;


/**
 * The interface Com id service.
 *
 * @author
 * @createTime 2019 -04-30 13:21:53
 * @description
 */
public interface ComIdService extends BaseService<ComId> {

    void updateByPrimaryKeySelective(ComId comId);

    /**
     * Query by sys name and biz type com id.
     *
     * @param sysName the sys name
     * @param bizKey  the biz key
     * @return the com id
     */
    ComId queryBySysNameAndBizType(String sysName, String bizKey);

    /**
     * Add new com id com id.
     *
     * @param sysName the sys name
     * @param bizKey  the biz key
     * @return the com id
     */
    ComId addNewComId(String sysName, String bizKey);
}
