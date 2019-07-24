package com.wei.springboot.starter.bean;

import lombok.Data;

/**
 * The type Page bean.
 * 通用分页查询参数封装
 *
 * @author William.Wei
 */
@Data
public class PageBean {

    private Integer pageNum = 1;

    private Integer pageSize = 20;

    /**
     * SQL 排序 ASC 数组
     */
    private String[] ascs;
    /**
     * SQL 排序 DESC 数组
     */
    private String[] descs;

}
