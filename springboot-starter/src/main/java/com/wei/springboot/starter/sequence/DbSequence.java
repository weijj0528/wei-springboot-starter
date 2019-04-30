package com.wei.springboot.starter.sequence;

import cn.hutool.core.util.StrUtil;
import com.wei.springboot.starter.config.SpringContext;
import com.wei.springboot.starter.sequence.incr.DbSequenceSpace;
import com.wei.springboot.starter.sequence.incr.SpaceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @createTime 2019/4/27 15:43
 * @description 基于数据库实现的序列
 */
@Configuration
@ConditionalOnBean(SpaceFactory.class)
@AutoConfigureAfter
public class DbSequence {

    SpaceFactory spaceFactory;

    ConcurrentHashMap<String, DoubleBufferSequence> sequenceMap = new ConcurrentHashMap<>();

    private static final String KEY_TMP = "Sequence:{}:{}";

    public DbSequence() {
        spaceFactory = SpringContext.getBean(SpaceFactory.class);
    }

    public long getAndIncrement(String sysName, String bizKey) {
        DoubleBufferSequence dbs = getOrNewSequence(sysName, bizKey);
        return dbs.getAndIncrement(sysName, bizKey);
    }

    private DoubleBufferSequence getOrNewSequence(String sysName, String bizKey) {
        String key = StrUtil.format(KEY_TMP, sysName, bizKey);
        DoubleBufferSequence doubleBufferSequence = sequenceMap.get(key);
        if (doubleBufferSequence == null) {
            doubleBufferSequence = new DoubleBufferSequence(new DbSequenceSpace(), new DbSequenceSpace(), spaceFactory);
            sequenceMap.put(key, doubleBufferSequence);
        }
        return doubleBufferSequence;
    }

}
