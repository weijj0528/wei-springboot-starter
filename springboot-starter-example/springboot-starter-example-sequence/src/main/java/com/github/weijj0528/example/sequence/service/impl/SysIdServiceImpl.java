package com.github.weijj0528.example.sequence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.weijj0528.example.sequence.dto.SysIdDto;
import com.github.weijj0528.example.sequence.mapper.SysIdMapper;
import com.github.weijj0528.example.sequence.model.SysId;
import com.github.weijj0528.example.sequence.service.SysIdService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @createTime 2019-10-24 21:33:27
 * @description
 */
@Service
public class SysIdServiceImpl extends AbstractService<SysId> implements SysIdService {

    @Resource
    private SysIdMapper sysIdMapper;

    @Override
    public XMapper<SysId> getMapper() {
        return sysIdMapper;
    }

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(SysIdDto dto) {
        SysId sysId = WeiBeanUtil.toBean(dto, SysId.class);
        return insertSelective(sysId);
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
    public int update(SysIdDto dto) {
        SysId sysId = WeiBeanUtil.toBean(dto, SysId.class);
        return updateByPrimaryKeySelective(sysId);
    }

    /**
     * Details sysId dto.
     *
     * @return the sysId dto
     */
    @Override
    public SysIdDto details(Serializable id) {
        SysId sysId = selectByPrimaryKey(id);
        return WeiBeanUtil.toBean(sysId, SysIdDto.class);
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
        QueryWrapper<SysId> example = new QueryWrapper<>(SysId.class);
        selectPageByExample(example, page);
        return page.getList();
    }

    @Override
    public SysId queryBySysNameAndBizType(String sysName, String bizKey) {
        QueryWrapper<SysId> example = new QueryWrapper<>(SysId.class);
        example.eq(SysId.SYS_NAME, sysName);
        example.eq(SysId.BIZ_TYPE, bizKey);
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
        insertSelective(sysId);
        return sysId;
    }
}
