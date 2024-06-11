package com.wei.starter.mybatis.service;

import cn.hutool.core.text.StrPool;
import com.github.pagehelper.PageHelper;
import com.wei.starter.base.bean.CodeEnum;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 * @author William
 * @Date 2019 /2/28
 * @Description 抽象基础服务 ，提供通用的Mapper方法接入
 */
public abstract class AbstractService<T> implements BaseService<T> {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * The Mapper.
     */
    public abstract XMapper<T> getMapper();

    @Override
    public int insertSelective(T t) {
        return getMapper().insertSelective(t);
    }

    @Override
    public int insertList(List<T> list) {
        return getMapper().insertList(list);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return getMapper().updateByPrimaryKeySelective(t);
    }

    /**
     * Select by primary key t.
     *
     * @param pk the pk
     * @return the t
     */
    @Override
    public T selectByPrimaryKey(Object pk) {
        return getMapper().selectByPrimaryKey(pk);
    }

    /**
     * Select one by example record.
     *
     * @param example the example
     * @return the record
     */
    @Override
    public T selectOneByExample(Example example) {
        return getMapper().selectOneByExample(example);
    }

    /**
     * Select one t.
     *
     * @param t the t
     * @return the t
     */
    @Override
    public T selectOne(T t) {
        return getMapper().selectOne(t);
    }

    /**
     * Select count by example int.
     *
     * @param example the example
     * @return the int
     */
    @Override
    public int selectCountByExample(Example example) {
        return getMapper().selectCountByExample(example);
    }

    /**
     * Select list.
     *
     * @param t the t
     * @return
     */
    @Override
    public List<T> select(T t) {
        return getMapper().select(t);
    }

    /**
     * Select by example list.
     *
     * @param example the example
     * @return the list
     */
    @Override
    public List<T> selectByExample(Example example) {
        return getMapper().selectByExample(example);
    }

    /**
     * Select page by example page info.
     *
     * @param example the example
     * @param page    the page
     * @return the page info
     */
    @Override
    public Page<T> selectPageByExample(Example example, Page<T> page) {
        try (com.github.pagehelper.Page<T> tPage = PageHelper.startPage(page.getPage(), page.getSize())) {
            getMapper().selectByExample(example);
            page.setList(tPage);
            page.setTotal(tPage.getTotal());
            page.setPage(tPage.getPageNum());
            page.setSize(tPage.getPageSize());
            return page;
        }
    }

    @Override
    public <R> void cursorOperator(String method, int batchSize, Object params, Consumer<List<R>> consumer) {
        Class<?> mapperClass = Arrays.stream(getMapper().getClass().getInterfaces()).filter(
                XMapper.class::isAssignableFrom
        ).findAny().orElseThrow(() -> new ErrorMsgException(CodeEnum.ERROR_SERVER.getCode(), "Mapper不存在"));
        String s = mapperClass.getName() + StrPool.DOT + method;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Cursor<R> cursor = sqlSession.selectCursor(s, params);
        try {
            Iterator<R> iterator = cursor.iterator();
            List<R> buffer = new ArrayList<>(batchSize);
            while (iterator.hasNext()) {
                buffer.add(iterator.next());
                if (buffer.size() == batchSize) {
                    consumer.accept(buffer);
                    buffer.clear();
                }
            }
            if (!buffer.isEmpty()) {
                consumer.accept(buffer);
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (IOException ignored) {
                }
            }
            sqlSession.close();
        }
    }

}
