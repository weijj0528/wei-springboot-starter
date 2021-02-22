package com.github.weijj0528.example.base.dto;

import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "用户信息")
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Update.class, message = "ID不能为空")
    @ApiModelProperty("主键")
    private Long id;

    @NotNull
    @ApiModelProperty("编号")
    private String no;

    @NotNull(groups = {Add.class, Update.class}, message = "名称不能为空")
    @ApiModelProperty("会员名")
    private String name;

    @NotNull
    @ApiModelProperty("昵称")
    private String nickname;

    @NotNull
    @ApiModelProperty("性别 0/1 女/男")
    private Integer gender;

    @NotNull
    @ApiModelProperty("手机")
    private String phone;

    @NotNull
    @ApiModelProperty("qq号")
    private String qq;

    @NotNull
    @ApiModelProperty("微信号")
    private String wechat;

    @NotNull
    @ApiModelProperty("会员积分")
    private Integer point;

    @NotNull
    @ApiModelProperty("头像地址")
    private String avatar;

    @NotNull
    @ApiModelProperty("个性签名")
    private String signature;

    @NotNull
    @ApiModelProperty("用户生日")
    private Date birthday;

    @NotNull
    @ApiModelProperty("省")
    private Integer province;

    @NotNull
    @ApiModelProperty("市")
    private Integer city;

    @NotNull
    @ApiModelProperty("来源 1/2/3/4 pc/android/weixin/ios")
    private Integer source;

    @NotNull
    @ApiModelProperty("实名")
    private String fullname;

    @NotNull
    @ApiModelProperty("身份证号码")
    private String idnumber;

    @NotNull
    @ApiModelProperty("审核状态 0/1/2/3 初始/审核中/审核成功/审核失败")
    private Integer audit;

    @NotNull
    @ApiModelProperty("审核备注")
    private String auditComment;

    @NotNull
    @ApiModelProperty("是否有签到，每天3点重置")
    private Integer isSign;

    @NotNull
    @ApiModelProperty("冻结0/1 正常/冻结")
    private Integer freeze;

    @NotNull
    @ApiModelProperty("最后登录IP")
    private String lastLoginIp;

    @NotNull
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

    @NotNull
    @ApiModelProperty("状态 0/1 有效/删除")
    private Integer isDel;

    @NotNull
    @ApiModelProperty("更新时间")
    private Date utime;

    @NotNull
    @ApiModelProperty("创建时间")
    private Date ctime;

}
