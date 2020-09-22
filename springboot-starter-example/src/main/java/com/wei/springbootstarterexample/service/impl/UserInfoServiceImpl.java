package com.wei.springbootstarterexample.service.impl;

import com.wei.springbootstarterexample.dto.UserInfoDto;
import com.wei.springbootstarterexample.mapper.UserInfoMapper;
import com.wei.springbootstarterexample.model.UserInfo;
import com.wei.springbootstarterexample.service.UserInfoService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public XMapper<UserInfo> getMapper() {
        return userInfoMapper;
    }

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(UserInfoDto dto) {
        UserInfo userInfo = WeiBeanUtil.toBean(dto, UserInfo.class);
        return getMapper().insertSelective(userInfo);
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
    public int update(UserInfoDto dto) {
        UserInfo userInfo = WeiBeanUtil.toBean(dto, UserInfo.class);
        return getMapper().updateByPrimaryKeySelective(userInfo);
    }

    /**
     * Details user info dto.
     *
     * @return the user info dto
     */
    @Override
    public UserInfoDto details(Object id) {
        UserInfo userInfo = getMapper().selectByPrimaryKey(id);
        return WeiBeanUtil.toBean(userInfo, UserInfoDto.class);
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
        Example example = new Example(UserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        selectPageByExample(example, page);
        return page.getList();
    }
}
