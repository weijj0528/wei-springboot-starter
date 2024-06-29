package com.wei.starter.security;

import java.util.Collection;

/**
 * 主要信息接口
 *
 * @author Weijj0528
 */
public interface Principal {

    /**
     * 获取名称
     *
     * @return name
     */
    String getName();

    /**
     * 获取租户
     *
     * @return the tenant
     */
    String getTenant();

    /**
     * 获取权限
     *
     * @return authorities authorities
     */
    Collection<String> getAuthorities();

}
