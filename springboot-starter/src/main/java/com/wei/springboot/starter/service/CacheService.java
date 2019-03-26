package com.wei.springboot.starter.service;

/**
 * The interface Cache service.
 *
 * @author Administrator
 * @createTime 2019 /3/17 15:58
 * @description 缓存服务 ，提供缓存的统一实现
 */
public interface CacheService {


    /**
     * 添加  元素到 cache  并设置过期时间
     *
     * @param key            the key
     * @param value          the value
     * @param expirationTime ： 缓存过期时间（单位秒）
     */
    void set(String key, Object value, int expirationTime);


    /**
     * 获取 cache内元素
     *
     * @param <T> the type parameter
     * @param key the key
     * @return t t
     */
    <T> T get(String key);


    /**
     * 删除
     *
     * @param key the key
     */
    void remove(String key);


    /**
     * Remove pattern.
     *
     * @param pattern the pattern
     */
    void removePattern(String pattern);

    /**
     * 删除全部 数据
     */
    void removeAll();

}
