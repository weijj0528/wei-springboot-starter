package com.wei.springboot.starter.service;


import com.github.pagehelper.PageInfo;
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
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(T record);

    /**
     * Delete by primary key int.
     *
     * @param pk the pk
     * @return the int
     */
    int deleteByPrimaryKey(Object pk);

    /**
     * Delete by example int.
     *
     * @param example the example
     * @return the int
     */
    int deleteByExample(Example example);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * Update by example selective int.
     *
     * @param example the example
     * @param record  the record
     * @return the int
     */
    int updateByExampleSelective(Example example, T record);

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
     * Select all list.
     *
     * @return the list
     */
    List<T> selectAll();

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
     * Page by example page info.
     * 分页查询
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param example  the example
     * @return the page info
     */
    PageInfo<T> pageByExample(int pageNum, int pageSize, Example example);

}
