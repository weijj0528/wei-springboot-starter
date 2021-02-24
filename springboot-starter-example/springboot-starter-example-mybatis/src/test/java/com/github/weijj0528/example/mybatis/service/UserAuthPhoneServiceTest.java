package com.github.weijj0528.example.mybatis.service;

import com.github.weijj0528.example.mybatis.MybatisExampleApplicationTest;
import com.github.weijj0528.example.mybatis.model.UserAuthPhone;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserAuthPhoneServiceTest extends MybatisExampleApplicationTest {

    @Resource
    private UserAuthPhoneService userAuthPhoneService;

    @Test
    void save() {
        final List<UserAuthPhone> select = userAuthPhoneService.select(new UserAuthPhone());
        System.out.println(select);
        assertFalse(select.isEmpty());
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}