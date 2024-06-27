package com.wei.starter.base.bean;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * The type Page bean.
 * 通用分页查询参数封装
 *
 * @author William.Wei
 */
@Data
public class Page<T> {

    /**
     * 分页数据，默认为空集
     */
    private List<T> list = Collections.emptyList();

    /**
     * 页码，从1开始
     */
    private long page = 1;

    /**
     * 页容量，默认为20
     */
    private long size = 20;

    /**
     * 总数默认为0
     */
    private long total = 0;

    /**
     * SQL 排序 ASC 数组
     */
    private String[] ascs;

    /**
     * SQL 排序 DESC 数组
     */
    private String[] descs;

    /**
     * 是否进行 count 查询
     */
    private boolean count = true;

    public long offset() {
        return page > 0L ? (page - 1L) * size : 0L;
    }

}
