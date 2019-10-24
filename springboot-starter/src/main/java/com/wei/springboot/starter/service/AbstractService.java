package com.wei.springboot.starter.service;

import com.github.pagehelper.PageHelper;
import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.mybatis.XMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 * @author William
 * @Date 2019 /2/28
 * @Description 抽象基础服务 ，提供通用的Mapper方法接入
 */
public abstract class AbstractService<T> implements BaseService<T> {

    /**
     * The Mapper.
     */
    @Autowired
    protected XMapper<T> mapper;

    /**
     * Select by primary key t.
     *
     * @param pk the pk
     * @return the t
     */
    @Override
    public T selectByPrimaryKey(Object pk) {
        return mapper.selectByPrimaryKey(pk);
    }

    /**
     * Select one by example record.
     *
     * @param example the example
     * @return the record
     */
    @Override
    public T selectOneByExample(Example example) {
        return mapper.selectOneByExample(example);
    }

    /**
     * Select one t.
     *
     * @param t the t
     * @return the t
     */
    @Override
    public T selectOne(T t) {
        return mapper.selectOne(t);
    }

    /**
     * Select count by example int.
     *
     * @param example the example
     * @return the int
     */
    @Override
    public int selectCountByExample(Example example) {
        return mapper.selectCountByExample(example);
    }

    /**
     * Select list.
     *
     * @param t the t
     * @return
     */
    @Override
    public List<T> select(T t) {
        return mapper.select(t);
    }

    /**
     * Select by example list.
     *
     * @param example the example
     * @return the list
     */
    @Override
    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    @Override
    public Page<T> selectPageByExample(Example example, Page<T> page) {
        PageHelper.startPage(page.getPage(), page.getSize());
        com.github.pagehelper.Page<T> tPage = (com.github.pagehelper.Page<T>) mapper.selectByExample(example);
        page.setList(tPage);
        page.setTotal(tPage.getTotal());
        page.setPage(tPage.getPageNum());
        page.setSize(tPage.getPageSize());
        return page;
    }

}
