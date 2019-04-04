package com.wei.springboot.starter.exception;

/**
 * 异常基类
 *
 * @author William
 */
public abstract class BaseException extends Exception {

    /**
     * @author William
     */
    private static final long serialVersionUID = -5420974096907472856L;

    /**
     * 错误 编码
     */
    private int code;

    /**
     * 错误 信息
     */
    private String message;


    public BaseException(ErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getMsg());
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
