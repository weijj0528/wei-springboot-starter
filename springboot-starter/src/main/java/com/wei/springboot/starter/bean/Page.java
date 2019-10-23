package com.wei.springboot.starter.bean;

import lombok.Data;

import java.util.List;

/**
 * The type Page bean.
 * 通用分页查询参数封装
 *
 * @author William.Wei
 */
@Data
public class Page<T> {

    private long pageNum;

    private long pageSize;

    private long total;

    private List<T> list;

    private long offset() {
        return pageNum > 0L ? (pageNum - 1L) * pageSize : 0L;
    }

}
