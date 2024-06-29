package com.wei.starter.mybatis.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wei.starter.mybatis.entity.Entity;
import com.wei.starter.security.Principal;
import com.wei.starter.security.WeiSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

/**
 * 自动填充处理
 *
 * @author Weijj0528
 */
@Slf4j
public class WeiMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Principal principal = WeiSecurityUtil.getPrincipal();
        Optional.ofNullable(principal).map(Principal::getName).ifPresent(name -> {
            setFieldValByName(Entity.CREATER, name, metaObject);
        });
        setFieldValByName(Entity.VERSION, BigInteger.ONE.longValue(), metaObject);
        setFieldValByName(Entity.DELETED, Boolean.FALSE, metaObject);
        setFieldValByName(Entity.CTIME, new Date(), metaObject);
        log.info("insert fill!");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Principal principal = WeiSecurityUtil.getPrincipal();
        Optional.ofNullable(principal).map(Principal::getName).ifPresent(name -> {
            setFieldValByName(Entity.UPDATER, name, metaObject);
        });
        setFieldValByName(Entity.UTIME, new Date(), metaObject);
        log.info("update fill!");
    }
}
