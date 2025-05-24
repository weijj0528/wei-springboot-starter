package com.github.weijj0528.example.mybatis.dto;

import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import com.wei.starter.mybatis.entity.Entity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;

/**
 * @author
 * @createTime 2020-09-18 18:02:56
 * @description
 */
@Data
public class UserAuthPhoneDto extends Entity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 租户ID
     */
    @NotNull(groups = Add.class, message = "租户ID不能为空")
    private Long tenant;

    /**
     * 电话
     */
    @NotNull(groups = Add.class, message = "电话不能为空")
    private String phone;

    /**
     * 密码
     */
    @NotNull(groups = Add.class, message = "密码不能为空")
    private String pwd;

    /**
     * 用户ID
     */
    @NotNull(groups = Add.class, message = "用户ID不能为空")
    private Long userId;

    /**
     * 是否删除
     */
    @NotNull(groups = Update.class, message = "是否删除不能为空")
    private Boolean deleted;

}
