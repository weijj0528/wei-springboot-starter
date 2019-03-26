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
    SUCCESS(200, "操作处理成功"),

    /**
     * 错误请求，参数不完整
     */
    BadRequestException(400, "错误请求，参数不完整"),

    /**
     * 用户已在其他设备登录  当前认证无效	   抛出 401
     */
    LogoutException(401, "设备登出"),

    /**
     * 账户 未登录  需要重新登录
     */
    NoAuthException(403, "账户未登录，需要重新登录"),

    /**
     * 账户 未登录  需要重新登录
     */
    OvertimeException(408, "请求超时"),

    /**
     * 系统错误 请稍候重新尝试
     */
    ERROR_SERVER(500, "系统错误,请稍候尝试");


    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public static ErrorEnum valueOfCode(int code) {
        switch (code) {
            case 400:
                return BadRequestException;
            case 401:
                return LogoutException;
            case 403:
                return NoAuthException;
            case 408:
                return OvertimeException;
            case 500:
                return ERROR_SERVER;
            default:
                return SUCCESS;
        }
    }
}
