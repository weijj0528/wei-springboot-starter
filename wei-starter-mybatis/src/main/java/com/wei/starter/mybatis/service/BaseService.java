package com.wei.starter.mybatis.service;


import com.wei.starter.base.bean.Page;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
     * Insert selective int.
     *
     * @param t the t
     * @return the int
     */
    int insertSelective(T t);

    /**
     * Insert list int.
     *
     * @param list the list
     * @return the int
     */
    int insertList(List<T> list);

    /**
     * Update by primary key selective int.
     *
     * @param t the t
     * @return the int
     */
    int updateByPrimaryKeySelective(T t);

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

    /**
     * Cursor operator.
     * 游标操作
     * 注意参数应与SQL中参数一致
     *
     * @param <R>       the type parameter
     * @param method    the method
     * @param batchSize the batch size
     * @param params    the params
     * @param consumer  the consumer
     */
    <R> void cursorOperator(String method, int batchSize, Object params, Consumer<List<R>> consumer);

}
