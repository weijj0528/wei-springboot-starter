package com.wei.starter.base.bean;

import lombok.Data;

/**
 * The type Range.
 * 范围查询条件封装
 *
 * @author William.Wei
 */
@Data
public class Range<T> {

    /**
     * 开始
     */
    private T from;

    /**
     * 结束
     */
    private T to;

}
