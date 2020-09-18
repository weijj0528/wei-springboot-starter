package com.wei.starter.mybatis.service;


import com.wei.starter.base.bean.Page;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * The interface Base service.
 *
 * @param <T> the type parameter
 * @author William
 * @Date 2019 /3/1
 * @Description 基础服务
 */
public interface BaseService<T> {

    /**
     * Select by primary key t.
     *
     * @param pk the pk
     * @return the t
     */
    T selectByPrimaryKey(Object pk);

    /**
     * Select one by example record.
     *
     * @param example the example
     * @return the record
     */
    T selectOneByExample(Example example);

    /**
     * Select one t.
     *
     * @param t the t
     * @return the t
     */
    T selectOne(T t);

    /**
     * Select count by example int.
     *
     * @param example the example
     * @return the int
     */
    int selectCountByExample(Example example);

    /**
     * Select list.
     *
     * @param t the t
     * @return the list
     */
    List<T> select(T t);

    /**
     * Select by example list.
     *
     * @param example the example
     * @return the list
     */
    List<T> selectByExample(Example example);

    /**
     * Select page by example page info.
     *
     * @param example the example
     * @param page    the page
     * @return the page info
     */
    Page<T> selectPageByExample(Example example, Page<T> page);

}
