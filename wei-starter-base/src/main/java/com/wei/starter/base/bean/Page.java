package com.wei.starter.base.bean;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * The type Page bean.
 * 通用分页查询参数封装
 *
 * @param <T> the type parameter
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

    /**
     * Offset long.
     *
     * @return the long
     */
    public long offset() {
        return page > 0L ? (page - 1L) * size : 0L;
    }

    /**
     * Convert page.
     *
     * @param <R>    the type parameter
     * @param mapper the mapper
     * @return the page
     */
    public <R> Page<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getList().stream().map(mapper).collect(toList());
        ((Page<R>) this).setList(collect);
        return (Page<R>) this;
    }

}
