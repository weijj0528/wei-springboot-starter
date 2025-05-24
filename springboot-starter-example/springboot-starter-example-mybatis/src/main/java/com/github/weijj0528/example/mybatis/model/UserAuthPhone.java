package com.github.weijj0528.example.mybatis.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wei.starter.mybatis.entity.Entity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_auth_phone")
public class UserAuthPhone extends Entity<Long> {

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
    @TableField("user_id")
    private Long userId;

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String PHONE = "phone";

    public static final String PWD = "pwd";

    public static final String USER_ID = "userId";
}