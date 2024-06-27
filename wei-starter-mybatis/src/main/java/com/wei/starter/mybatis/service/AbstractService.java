package com.wei.starter.mybatis.service;

import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.starter.base.bean.CodeEnum;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 * @author William
 * @Date 2019 /2/28
 * @Description 抽象基础服务 ，提供通用的Mapper方法接入
 */
public abstract class AbstractService<T> extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * The Mapper.
     *
     * @return the mapper
     */
    public abstract XMapper<T> getMapper();

    @Override
    public BaseMapper<T> getBaseMapper() {
        return getMapper();
    }

    @Override
    public int insertSelective(T t) {
        return getMapper().insert(t);
    }

    @Override
    public int insertList(List<T> list) {
        List<BatchResult> results = getMapper().insert(list);
        return results.stream().flatMap(b -> Stream.of(b.getUpdateCounts()))
                .mapToInt(m -> Arrays.stream(m).sum()).sum();
    }

    @Override
    public int deleteByPrimaryKey(Serializable id) {
        return getMapper().deleteById(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return getMapper().updateById(t);
    }

    /**
     * Select by primary key t.
     *
     * @param pk the pk
     * @return the t
     */
    @Override
    public T selectByPrimaryKey(Serializable pk) {
        return getMapper().selectById(pk);
    }

    /**
     * Select one t.
     *
     * @param t the t
     * @return the t
     */
    @Override
    public T selectOne(T t) {
        QueryWrapper<T> wrapper = new QueryWrapper<>(t);
        return getMapper().selectOne(wrapper);
    }

    /**
     * Select list.
     *
     * @param t the t
     * @return
     */
    @Override
    public List<T> select(T t) {
        QueryWrapper<T> wrapper = new QueryWrapper<>(t);
        return getMapper().selectList(wrapper);
    }

    /**
     * Select one by example record.
     *
     * @param wrapper the wrapper
     * @return the record
     */
    @Override
    public T selectOneByExample(Wrapper<T> wrapper) {
        return getMapper().selectOne(wrapper);
    }

    /**
     * Select by example list.
     *
     * @param wrapper the example
     * @return the list
     */
    @Override
    public List<T> selectByExample(Wrapper<T> wrapper) {
        return getMapper().selectList(wrapper);
    }


    /**
     * Select count by example int.
     *
     * @param wrapper the example
     * @return the int
     */
    @Override
    public Long selectCountByExample(Wrapper<T> wrapper) {
        return getMapper().selectCount(wrapper);
    }

    /**
     * Select page by example page info.
     *
     * @param wrapper the wrapper
     * @param page    the page
     * @return the page info
     */
    @Override
    public Page<T> selectPageByExample(Wrapper<T> wrapper, Page<T> page) {
        IPage<T> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        getMapper().selectPage(iPage, wrapper);
        page.setList(iPage.getRecords());
        page.setTotal(iPage.getTotal());
        page.setPage(iPage.getCurrent());
        page.setSize(iPage.getSize());
        return page;
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
