package com.github.weijj0528.example.base.dto;

import com.wei.starter.base.valid.Add;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
@Data
@ApiModel(description = "")
public class UserAuthPhoneDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(groups = Add.class, message = "ID不能为空")
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 租户ID
     */
    @NotNull(groups = Add.class, message = "租户ID不能为空")
    @ApiModelProperty("租户ID")
    private Long tenantId;

    /**
     * 电话
     */
    @NotNull(groups = Add.class, message = "电话不能为空")
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 密码
     */
    @NotNull(groups = Add.class, message = "密码不能为空")
    @ApiModelProperty("密码")
    private String pwd;

    /**
     * 用户ID
     */
    @NotNull(groups = Add.class, message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 是否删除
     */
    @NotNull(groups = Add.class, message = "是否删除不能为空")
    @ApiModelProperty("是否删除")
    private Boolean del;

    /**
     * 更新人
     */
    @NotNull(groups = Add.class, message = "更新人不能为空")
    @ApiModelProperty("更新人")
    private String updater;

    /**
     * 最后更新时间
     */
    @NotNull(groups = Add.class, message = "最后更新时间不能为空")
    @ApiModelProperty("最后更新时间")
    private Date utime;

    /**
     * 创建人
     */
    @NotNull(groups = Add.class, message = "创建人不能为空")
    @ApiModelProperty("创建人")
    private String creater;

    /**
     * 记录时间
     */
    @NotNull(groups = Add.class, message = "记录时间不能为空")
    @ApiModelProperty("记录时间")
    private Date ctime;

}
