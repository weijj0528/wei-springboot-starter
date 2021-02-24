package com.github.weijj0528.example.sequence.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @createTime 2019-10-24 21:42:09
 * @description
 */
@Data
public class SysIdDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;

    @NotNull
    private Long tenantId;

    @NotNull
    private String sysName;

    @NotNull
    private String bizType;

    @NotNull
    private Long nextStart;

    @NotNull
    private Long step;

    @NotNull
    private Boolean isDel;

    @NotNull
    private String updater;

    @NotNull
    private Date utime;

    @NotNull
    private String creater;

    @NotNull
    private Date ctime;

}
