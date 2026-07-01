package com.wei.starter.mybatis.service;

import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.starter.base.bean.Code;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.mybatis.xmapper.XMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * The type Abstract service.
 *
 * @param <T> the type parameter
 * @author William
 * @Date 2019 /2/28
 * @Description 抽象基础服务 ，提供通用的Mapper方法接入
 */
@Slf4j
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
    @Transactional(rollbackFor = Exception.class)
    public int insertSelective(T t) {
        return getMapper().insert(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertList(List<T> list) {
        // MP 批量插入失败会抛异常，成功即代表全部插入；MySQL 默认返回 SUCCESS_NO_INFO(-2)，直接按条数返回
        getMapper().insert(list);
        return list.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Serializable id) {
        return getMapper().deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Page<T> selectPageByExample(Wrapper<T> wrapper, Page<T> page) {
        IPage<T> innerPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getPage(), page.getSize());
        getMapper().selectPage(innerPage, wrapper);
        page.setList(innerPage.getRecords());
        page.setTotal(innerPage.getTotal());
        page.setPage(innerPage.getCurrent());
        page.setSize(innerPage.getSize());
        return page;
    }

    @Override
    public <R> void cursorOperator(String method, int batchSize, Object params, Consumer<List<R>> consumer) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize must be > 0, otherwise the whole result set buffers into memory");
        }
        Class<?> mapperClass = Arrays.stream(getMapper().getClass().getInterfaces()).filter(
                XMapper.class::isAssignableFrom
        ).findAny().orElseThrow(() -> new ErrorMsgException(Code.SYSTEM_ERROR.getCode(), "Mapper不存在"));
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
                } catch (IOException e) {
                    log.warn("close cursor failed: {}", e.getMessage());
                }
            }
            sqlSession.close();
        }
    }

}
