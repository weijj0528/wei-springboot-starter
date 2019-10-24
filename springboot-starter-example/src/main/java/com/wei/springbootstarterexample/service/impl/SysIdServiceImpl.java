package com.wei.springbootstarterexample.service.impl;

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.service.AbstractService;
import com.wei.springbootstarterexample.dto.SysIdDto;
import com.wei.springbootstarterexample.model.SysId;
import com.wei.springbootstarterexample.service.SysIdService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author
 * @createTime 2019-10-24 21:33:27
 * @description
 */
@Service
public class SysIdServiceImpl extends AbstractService<SysId> implements SysIdService {

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(SysIdDto dto) {
        SysId sysId = dto.toModel();
        return mapper.insertSelective(sysId);
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
    public int update(SysIdDto dto) {
        SysId sysId = dto.toModel();
        return mapper.updateByPrimaryKeySelective(sysId);
    }

    /**
     * Details sysId dto.
     *
     * @return the sysId dto
     */
    @Override
    public SysIdDto details(Object id) {
        SysId sysId = mapper.selectByPrimaryKey(id);
        SysIdDto sysIdDto = new SysIdDto();
        sysIdDto.copyModel(sysId);
        return sysIdDto;
    }

    /**
     * List list.
     *
     * @param sysIdDto
     * @param page
     * @return the list
     */
    @Override
    public List<SysIdDto> list(SysIdDto sysIdDto, Page page) {
        Example example = new Example(SysId.class);
        Example.Criteria criteria = example.createCriteria();
        selectPageByExample(example, page);
        return page.getList();
    }

    @Override
    public void updateByPrimaryKeySelective(SysId sysId) {
        mapper.updateByPrimaryKeySelective(sysId);
    }

    @Override
    public SysId queryBySysNameAndBizType(String sysName, String bizKey) {
        Example example = new Example(SysId.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(SysId.SYS_NAME, sysName);
        criteria.andEqualTo(SysId.BIZ_TYPE, bizKey);
        return selectOneByExample(example);
    }

    @Override
    public SysId addNewComId(String sysName, String bizKey) {
        SysId sysId = new SysId();
        sysId.setSysName(sysName);
        sysId.setBizType(bizKey);
        sysId.setNextStart(0L);
        sysId.setStep(1000L);
        sysId.setCtime(new Date());
        mapper.insertSelective(sysId);
        return sysId;
    }
}
