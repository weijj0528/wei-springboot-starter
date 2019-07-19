package com.wei.springboot.starter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 返回结果封装
 *
 * @author
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T> implements Serializable, Cloneable {
    /**
     * @author
     */
    private static final long serialVersionUID = 8632964679110167394L;
    /**
     * 错误编号
     */
    private int code = 200;
    /**
     * 错误信息
     */
    private String msg = "success";
    /**
     * 业务数据
     */
    private T data;

    public ResultBean(T data) {
        this.data = data;
    }

    public static <T> ResultBean<T> success() {
        return new ResultBean<>();
    }

    public static <T> ResultBean<T> success(T data) {
        return new ResultBean<>(data);
    }

    public static <T> ResultBean<T> failure(int code, String msg) {
        return new ResultBean<>(code, msg, null);
    }

}
