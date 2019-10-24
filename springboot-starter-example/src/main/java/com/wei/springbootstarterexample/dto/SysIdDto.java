package com.wei.springbootstarterexample.dto;

import com.wei.springboot.starter.dto.BaseDto;
import com.wei.springbootstarterexample.model.SysId;
import io.swagger.annotations.ApiModelProperty;
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
public class SysIdDto extends BaseDto<SysId> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("ID")
    private Long id;

    @NotNull
    @ApiModelProperty("租户ID")
    private Long tenantId;

    @NotNull
    @ApiModelProperty("系统名称")
    private String sysName;

    @NotNull
    @ApiModelProperty("业务类型")
    private String bizType;

    @NotNull
    @ApiModelProperty("下一次开始值")
    private Long nextStart;

    @NotNull
    @ApiModelProperty("步长")
    private Long step;

    @NotNull
    @ApiModelProperty("")
    private Boolean isDel;

    @NotNull
    @ApiModelProperty("更新人")
    private String updater;

    @NotNull
    @ApiModelProperty("最后更新时间")
    private Date utime;

    @NotNull
    @ApiModelProperty("创建人")
    private String creater;

    @NotNull
    @ApiModelProperty("记录时间")
    private Date ctime;

}
