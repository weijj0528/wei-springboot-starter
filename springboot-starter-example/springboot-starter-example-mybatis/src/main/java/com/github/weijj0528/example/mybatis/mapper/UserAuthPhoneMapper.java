package com.github.weijj0528.example.mybatis.mapper;

import com.github.weijj0528.example.mybatis.model.UserAuthPhone;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAuthPhoneMapper extends XMapper<UserAuthPhone> {

    List<UserAuthPhone> select(@Param("q") UserAuthPhone query);

}