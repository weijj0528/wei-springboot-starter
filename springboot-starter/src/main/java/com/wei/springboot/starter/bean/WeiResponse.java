package com.wei.springboot.starter.bean;

import com.wei.springboot.starter.exception.BaseException;
import com.wei.springboot.starter.exception.ErrorEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 结果响应
 *
 * @author
 */
@Data
public class WeiResponse implements Serializable, Cloneable {
    /**
     * @author
     */
    private static final long serialVersionUID = 8632964679110167394L;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 错误编号
     */
    private int code;
    /**
     * 业务返回数据
     */
    private Object data;

    public WeiResponse() {
        this.msg = ErrorEnum.SUCCESS.getMsg();
        this.code = ErrorEnum.SUCCESS.getCode();
    }

    public WeiResponse(Object data) {
        this();
        this.data = data;
    }


    public WeiResponse(ErrorEnum msgEnum) {
        this.msg = msgEnum.getMsg();
        this.code = msgEnum.getCode();
    }

    public WeiResponse(ErrorEnum errorEnum, String msg) {
        this(errorEnum);
        this.msg = msg;
    }

    public WeiResponse(BaseException exception) {
        this.msg = exception.getMessage();
        this.code = exception.getCode();
    }


    @Override
    public WeiResponse clone() {
        try {
            return (WeiResponse) super.clone();
        } catch (Exception e) {
            return null;
        }
    }

}
