package com.github.weijj0528.example.sequence.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "sys_id")
public class SysId implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 租户ID
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * 系统名称
     */
    @Column(name = "sys_name")
    private String sysName;

    /**
     * 业务类型
     */
    @Column(name = "biz_type")
    private String bizType;

    /**
     * 下一次开始值
     */
    @Column(name = "next_start")
    private Long nextStart;

    /**
     * 步长
     */
    private Long step;

    @Column(name = "is_del")
    private Boolean isDel;

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

    public static final String SYS_NAME = "sysName";

    public static final String BIZ_TYPE = "bizType";

    public static final String NEXT_START = "nextStart";

    public static final String STEP = "step";

    public static final String IS_DEL = "isDel";

    public static final String UPDATER = "updater";

    public static final String UTIME = "utime";

    public static final String CREATER = "creater";

    public static final String CTIME = "ctime";
}