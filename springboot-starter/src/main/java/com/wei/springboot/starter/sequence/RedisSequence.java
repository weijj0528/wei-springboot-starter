package com.wei.springboot.starter.sequence;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * @author Administrator
 * @createTime 2019/4/27 15:43
 * @description 基于Redis实现的序列
 */
@Configuration
public class RedisSequence {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private static final String KEY_TMP = "Sequence:{}:{}";

    public long getAndIncrement(String sysName, String bizKey) {
        String key = StrUtil.format(KEY_TMP, sysName, bizKey);
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisConnectionFactory);
        Long increment = entityIdCounter.getAndIncrement();
        // 从0开始的
        if (increment == 0) {
            increment = entityIdCounter.getAndIncrement();
        }
        return increment;
    }

}
