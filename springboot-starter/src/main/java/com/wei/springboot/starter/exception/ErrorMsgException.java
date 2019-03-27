package com.wei.springboot.starter.exception;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 返回自定义错误信息 错误问题
 *
 * @author William
 */
@Data
public class ErrorMsgException extends BaseException {

    private static final long serialVersionUID = 1452976038790619628L;

    /**
     * 错误 信息
     */
    private String msg;
    /**
     * 错误 编码
     */
    private int code;

    public ErrorMsgException() {
    }

    public ErrorMsgException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }


    public ErrorMsgException(String msg) {
        this.code = ErrorEnum.ERROR_SERVER.getCode();
        this.msg = msg;
        if (StrUtil.isBlank(msg)) {
            this.msg = ErrorEnum.ERROR_SERVER.getMsg();
        }
    }

    public ErrorMsgException(ErrorEnum errorEnum) {
        this.msg = errorEnum.getMsg();
        this.code = errorEnum.getCode();
    }


    public ErrorMsgException(ErrorEnum errorEnum, String msg) {
        this.msg = msg;
        this.code = errorEnum.getCode();
    }


    @Override
    public String causedBy() {
        return ErrorEnum.ERROR_SERVER.getMsg();
    }
}
