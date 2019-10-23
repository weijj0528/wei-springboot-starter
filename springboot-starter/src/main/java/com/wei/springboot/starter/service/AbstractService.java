package com.wei.springboot.starter.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    @Override
    public int insertSelective(T record) {
        return mapper.insertSelective(record);
    }

    /**
     * Delete by primary key int.
     *
     * @param pk the pk
     * @return the int
     */
    @Override
    public int deleteByPrimaryKey(Object pk) {
        return mapper.deleteByPrimaryKey(pk);
    }

    /**
     * Delete by example int.
     *
     * @param example the example
     * @return the int
     */
    @Override
    public int deleteByExample(Example example) {
        return mapper.deleteByExample(example);
    }

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    @Override
    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * Update by example selective int.
     *
     * @param example the example
     * @param record  the record
     * @return the int
     */
    @Override
    public int updateByExampleSelective(Example example, T record) {
        return mapper.updateByExampleSelective(record, example);
    }

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
     * Select all list.
     *
     * @return the list
     */
    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
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
     * Select by example list.
     *
     * @param example the example
     * @return the list
     */
    @Override
    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    /**
     * Page by example page info.
     * 分页查询
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param example  the example
     * @return
     */
    @Override
    public PageInfo<T> pageByExample(int pageNum, int pageSize, Example example) {
        PageHelper.startPage(pageNum, pageSize);
        // 开启分页查询后反回为 com.github.pagehelper.Page
        List<T> list = selectByExample(example);
        return new PageInfo<>(list);
    }


}
