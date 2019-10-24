package com.wei.springboot.starter.service;


import com.wei.springboot.starter.bean.Page;
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
     * Select count by example int.
     *
     * @param example the example
     * @return the int
     */
    int selectCountByExample(Example example);

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
     * @return the page info
     */
    Page<T> selectPageByExample(Example example, Page<T> page);

}
