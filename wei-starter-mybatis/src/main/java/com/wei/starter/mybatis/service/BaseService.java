package com.wei.starter.mybatis.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.starter.base.bean.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * The interface Base service.
 *
 * @param <T> the type parameter
 * @author William
 * @Date 2019 /3/1
 * @Description 基础服务
 */
public interface BaseService<T> extends IService<T> {

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
     * Delete by primary key int.
     *
     * @param id the id
     * @return the int
     */
    int deleteByPrimaryKey(Serializable id);

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
     * @param id the id
     * @return the t
     */
    T selectByPrimaryKey(Serializable id);

    /**
     * Select one t.
     *
     * @param t the t
     * @return the t
     */
    T selectOne(T t);

    /**
     * Select list.
     *
     * @param t the t
     * @return the list
     */
    List<T> select(T t);

    /**
     * Select one by example record.
     *
     * @param wrapper the wrapper
     * @return the record
     */
    T selectOneByExample(Wrapper<T> wrapper);

    /**
     * Select by example list.
     *
     * @param wrapper the wrapper
     * @return the list
     */
    List<T> selectByExample(Wrapper<T> wrapper);

    /**
     * Select count by example int.
     *
     * @param wrapper the wrapper
     * @return the int
     */
    Long selectCountByExample(Wrapper<T> wrapper);

    /**
     * Select page by example page info.
     *
     * @param wrapper the wrapper
     * @param page    the page
     * @return the page info
     */
    Page<T> selectPageByExample(Wrapper<T> wrapper, Page<T> page);

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
