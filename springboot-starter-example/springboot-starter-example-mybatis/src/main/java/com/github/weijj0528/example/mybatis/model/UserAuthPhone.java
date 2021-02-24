package com.github.weijj0528.example.mybatis.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "user_auth_phone")
public class UserAuthPhone implements Serializable {
    /**
     * ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 租户ID
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 电话
     */
    private String phone;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 是否删除
     */
    @Column(name = "is_del")
    private Boolean del;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 最后更新时间
     */
    private Date utime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 记录时间
     */
    private Date ctime;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String TENANT_ID = "tenantId";

    public static final String PHONE = "phone";

    public static final String PWD = "pwd";

    public static final String USER_ID = "userId";

    public static final String IS_DEL = "del";

    public static final String UPDATER = "updater";

    public static final String UTIME = "utime";

    public static final String CREATER = "creater";

    public static final String CTIME = "ctime";
}