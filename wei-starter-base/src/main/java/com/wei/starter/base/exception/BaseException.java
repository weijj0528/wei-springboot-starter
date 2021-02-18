package com.wei.starter.base.exception;

/**
 * 异常基类
 *
 * @author William
 */
public abstract class BaseException extends RuntimeException {

    /**
     * 错误 编码
     */
    private String code;

    /**
     * 错误 信息
     */
    private String message;

    public BaseException() {
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getMsg());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}