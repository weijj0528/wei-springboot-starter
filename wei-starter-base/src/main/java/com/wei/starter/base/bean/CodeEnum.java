package com.wei.starter.base.bean;

import lombok.Getter;

/**
 * V1  编号枚举定义
 *
 * @author Weijj0528
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
    BAD_REQUEST("400", "错误请求，参数不完整"),

    /**
     * 身份验证失败，请重新登录！
     */
    UNAUTHORIZED("401", "身份验证失败，请重新登录！"),

    /**
     * 账户禁用
     */
    FORBIDDEN("403", "账户禁用，请联系管理员"),

    /**
     * Not found code enum.
     */
    NOT_FOUND("404", "不存在"),

    /**
     * 账户 未登录  需要重新登录
     */
    OVERTIME("408", "请求超时"),

    /**
     * 系统错误 请稍候重新尝试
     */
    SYSTEM_ERROR("50000", "系统开小差，请稍候尝试"),

    /**
     * 加锁异常
     */
    LOCK_ERROR("50100", "网络拥堵，请稍候尝试"),

    /**
     * 重复处理
     */
    REPEAT_ERROR("50110", "业务已处理"),
    ;


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
