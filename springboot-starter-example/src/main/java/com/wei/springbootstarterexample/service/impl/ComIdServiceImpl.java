package com.wei.springbootstarterexample.service.impl;

import com.wei.springboot.starter.service.AbstractService;
import com.wei.springbootstarterexample.model.ComId;
import com.wei.springbootstarterexample.service.ComIdService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author
 * @createTime 2019-04-30 13:21:53
 * @description
 */
@Service
public class ComIdServiceImpl extends AbstractService<ComId> implements ComIdService {

    @Override
    public void updateByPrimaryKeySelective(ComId comId) {
        mapper.updateByPrimaryKeySelective(comId);
    }

    @Override
    public ComId queryBySysNameAndBizType(String sysName, String bizKey) {
        Example example = new Example(ComId.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(ComId.SYS_NAME, sysName);
        criteria.andEqualTo(ComId.BIZ_TYPE, bizKey);
        return selectOneByExample(example);
    }

    @Override
    public ComId addNewComId(String sysName, String bizKey) {
        ComId comId = new ComId();
        comId.setSysName(sysName);
        comId.setBizType(bizKey);
        comId.setNextStart(0L);
        comId.setStep(1000L);
        comId.setCtime(new Date());
        mapper.insertSelective(comId);
        return comId;
    }
}
