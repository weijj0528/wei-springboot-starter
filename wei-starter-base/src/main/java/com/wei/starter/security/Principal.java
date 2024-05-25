package com.wei.starter.security;

import java.util.Collection;

/**
 * 主要信息接口
 */
public interface Principal {

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取权限
     *
     * @return
     */
    Collection<String> getAuthorities();

}
