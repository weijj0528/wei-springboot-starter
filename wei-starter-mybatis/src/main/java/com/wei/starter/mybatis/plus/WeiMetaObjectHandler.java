package com.wei.starter.mybatis.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wei.starter.mybatis.entity.Entity;
import com.wei.starter.security.Principal;
import com.wei.starter.security.WeiSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

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
            strictInsertFill(metaObject, Entity.CREATOR, String.class, name);
            strictInsertFill(metaObject, Entity.UPDATER, String.class, name);
        });
        strictInsertFill(metaObject, Entity.VERSION, Long.class, 1L);
        strictInsertFill(metaObject, Entity.DELETED, Boolean.class, Boolean.FALSE);
        Date now = new Date();
        strictInsertFill(metaObject, Entity.CTIME, Date.class, now);
        strictInsertFill(metaObject, Entity.UTIME, Date.class, now);
        log.debug("insert fill");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Principal principal = WeiSecurityUtil.getPrincipal();
        Optional.ofNullable(principal).map(Principal::getName).ifPresent(name -> {
            strictUpdateFill(metaObject, Entity.UPDATER, String.class, name);
        });
        strictUpdateFill(metaObject, Entity.UTIME, Date.class, new Date());
        log.debug("update fill");
    }
}
