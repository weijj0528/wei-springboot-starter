package com.wei.springboot.starter.exception;

import lombok.Getter;

/**
 * V1  错误枚举列表
 *
 * @author Johnson.Jia
 */
@Getter
public enum ErrorEnum {

    /**
     * 操作处理成功
     */
    SUCCESS("20000", "操作处理成功"),

    /**
     * 错误请求，参数不完整
     */
    BadRequestException("40000", "错误请求，参数不完整"),

    /**
     * 没有权限
     */
    UnauthorizedException("40100", "没有权限"),

    /**
     * 账户禁用
     */
    ForbiddenException("40300", "账户禁用，请联系管理员"),

    /**
     * 账户 未登录  需要重新登录
     */
    OvertimeException("40800", "请求超时"),

    /**
     * 系统错误 请稍候重新尝试
     */
    ERROR_SERVER("50000", "系统错误,请稍候尝试");


    private String code;

    private String msg;

    ErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
