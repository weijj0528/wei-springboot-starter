package com.wei.starter.redis.service;


import com.wei.starter.base.bean.Result;

import java.util.Map;

/**
 * The interface Redis incr service.
 * Redis lua 原子incr操作 实践
 *
 * @author William.Wei
 */
public interface IRedisIncrService {

    /**
     * Incr result.
     * 批量Key增量操作，保证操作后数量>=0
     * 如出现<0情况本次操作失败已操作Key回滚
     * 正数为增加负数为扣减
     * 注意：批量操作的Key必须在同一节点 可通过HashTag来保证
     *
     * @param hashKey the hash key
     * @param incr    the incr
     * @param expire  the expire
     * @return the result
     */
    Result<Map<String, Long>> incr(String[] hashKey, Long[] incr, Integer[] expire);

    /**
     * Incr result.
     * Key不过期
     *
     * @param keyArgs the key args
     * @return the result
     * @see IRedisIncrService#incr(String[], Long[], Integer[]) IRedisIncrService#incr(String[], Long[], Integer[])
     */
    Result<Map<String, Long>> incr(Map<String, Long> keyArgs);

    /**
     * Gets incr.
     * 获取当前值
     *
     * @param keys the keys
     * @return the incr
     */
    Result<Map<String, Long>> getIncr(String[] keys);

    /**
     * Hash key incr
     * 批量HashKey增量操作，保证操作后数量>=0
     * 如出现<0情况本次操作失败已操作Key回滚
     * 正数为增加负数为扣减
     * 并更新Key过期时间
     *
     * @param key     the key
     * @param expire  the expire
     * @param keyArgs the params
     * @return the result
     */
    Result<Map<String, Long>> hasIncr(String key, Integer expire, Map<String, Long> keyArgs);

    /**
     * Hincr result.
     * Key不过期
     *
     * @param key     the key
     * @param keyArgs the key args
     * @return the result
     * @see IRedisIncrService#hasIncr(String, Integer, Map) IRedisIncrService#hincr(String, Integer, Map)IRedisIncrService#hincr(String, Long, Map)
     */
    Result<Map<String, Long>> hasIncr(String key, Map<String, Long> keyArgs);

    /**
     * Gets hincr.
     * 获取当前值
     *
     * @param key      the key
     * @param hashKeys the hash keys
     * @return the hincr
     */
    Result<Map<String, Long>> getHasIncr(String key, String[] hashKeys);
}
