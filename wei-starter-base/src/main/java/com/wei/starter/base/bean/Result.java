package com.wei.starter.base.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回结果封装
 *
 * @author
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable, Cloneable {
    /**
     * @author
     */
    private static final long serialVersionUID = 8632964679110167394L;
    /**
     * 错误编号
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 业务数据
     */
    private T data;

    public Result(T data) {
        this("20000", "success", data);
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> failure(String code, String msg) {
        return new Result<>(code, msg, null);
    }

}
