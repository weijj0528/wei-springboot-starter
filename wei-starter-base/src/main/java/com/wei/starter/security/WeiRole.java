package com.wei.starter.security;

/**
 * 常用角色定义
 *
 * @author William.Wei
 */
public enum WeiRole {

    /**
     * User wei role.
     */
    USER("User", "用户"),
    /**
     * Admin wei role.
     */
    ADMIN("Admin", "管理员"),
    /**
     * Customer wei role.
     */
    CUSTOMER("Customer", "客户"),
    /**
     * Super admin wei role.
     */
    SUPER_ADMIN("SuperAdmin", "超级管理员"),
    /**
     * Tenant wei role.
     */
    TENANT("Tenant", "租户"),
    /**
     * Tenant user wei role.
     */
    TENANT_USER("TenantUser", "租户用户"),
    /**
     * Tenant admin wei role.
     */
    TENANT_ADMIN("TenantAdmin", "租户管理员"),
    ;

    private final String role;
    private final String desc;

    WeiRole(String role, String desc) {
        this.role = role;
        this.desc = desc;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }
}
