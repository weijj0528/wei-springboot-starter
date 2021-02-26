package com.wei.starter.base.bean;

import lombok.Getter;

/**
 * V1  编号枚举定义
 *
 * @author Johnson.Jia
 */
@Getter
public enum CodeEnum {

    /**
     * 操作处理成功
     */
    SUCCESS("20000", "操作处理成功"),

    /**
     * 错误请求，参数不完整
     */
    BadRequestException("400", "错误请求，参数不完整"),

    /**
     * 没有权限
     */
    UnauthorizedException("401", "没有权限"),

    /**
     * 账户禁用
     */
    ForbiddenException("403", "账户禁用，请联系管理员"),

    /**
     * 账户 未登录  需要重新登录
     */
    OvertimeException("408", "请求超时"),

    /**
     * 系统错误 请稍候重新尝试
     */
    ERROR_SERVER("50000", "系统错误,请稍候尝试");


    /**
     * 编码
     */
    private final String code;

    /**
     * 说明
     */
    private final String msg;

    CodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
