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

    private List<T> list = Collections.emptyList();

    private int page = 1;

    private int size = 20;

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
