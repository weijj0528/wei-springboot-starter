package com.wei.springbootstarterexample.dto;

import com.wei.springboot.starter.dto.BaseDto;
import com.wei.springbootstarterexample.model.UserAuthPhone;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * @author
 * @createTime 2019-04-10 16:53:42
 * @description
 */
@Data
public class UserAuthPhoneDto extends BaseDto<UserAuthPhone> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("")
    private Long id;

    @NotNull
    @ApiModelProperty("")
    private String phone;

    @NotNull
    @ApiModelProperty("")
    private String pwd;

    @NotNull
    @ApiModelProperty("")
    private Long userId;

    @NotNull
    @ApiModelProperty("")
    private Date ctime;

}
