package com.wei.springbootstarterexample.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "com_id")
public class ComId implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private Boolean del;

    private Date utime;

    /**
     * 记录时间
     */
    private Date ctime;

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    public static final String SYS_NAME = "sysName";

    public static final String BIZ_TYPE = "bizType";

    public static final String NEXT_START = "nextStart";

    public static final String STEP = "step";

    public static final String DEL = "del";

    public static final String UTIME = "utime";

    public static final String CTIME = "ctime";
}