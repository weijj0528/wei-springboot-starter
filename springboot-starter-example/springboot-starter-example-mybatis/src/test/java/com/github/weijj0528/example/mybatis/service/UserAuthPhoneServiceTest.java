package com.github.weijj0528.example.mybatis.service;

import com.github.weijj0528.example.mybatis.MybatisExampleApplicationTest;
import com.github.weijj0528.example.mybatis.dto.UserAuthPhoneDto;
import com.github.weijj0528.example.mybatis.mapper.UserAuthPhoneMapper;
import com.github.weijj0528.example.mybatis.model.UserAuthPhone;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserAuthPhoneServiceTest extends MybatisExampleApplicationTest {

    @Resource
    private UserAuthPhoneService userAuthPhoneService;
    @Resource
    private UserAuthPhoneMapper userAuthPhoneMapper;

    @Test
    void delete() {
    }

    @Test
    void update() {
        final UserAuthPhoneDto dto = new UserAuthPhoneDto();
        dto.setId(1L);
        dto.setUtime(new Date());
        final int update = userAuthPhoneService.update(dto);
        assertFalse(update <= 0);
    }

    @Test
    void select() {
        final List<UserAuthPhone> select = userAuthPhoneService.select(new UserAuthPhone());
        System.out.println(select);
        assertFalse(select.isEmpty());
    }

    @Test
    void selectByExample() {
        final Example example = new Example(UserAuthPhone.class);
        final Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(UserAuthPhone.IS_DEL, false);
        criteria.andIn(UserAuthPhone.ID, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        final List<UserAuthPhone> userAuthPhones = userAuthPhoneMapper.selectByExample(example);
        assertFalse(userAuthPhones.isEmpty());
    }
}