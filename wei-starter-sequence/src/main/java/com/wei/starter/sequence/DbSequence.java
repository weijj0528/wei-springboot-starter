package com.wei.starter.sequence;

import cn.hutool.core.util.StrUtil;
import com.wei.starter.sequence.incr.DbSequenceSpace;
import com.wei.starter.sequence.incr.SpaceFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @createTime 2019/4/27 15:43
 * @description 基于数据库实现的序列
 */
public class DbSequence {

    ConcurrentHashMap<String, DoubleBufferSequence> sequenceMap = new ConcurrentHashMap<>();

    protected SpaceFactory spaceFactory;

    public DbSequence(SpaceFactory spaceFactory) {
        this.spaceFactory = spaceFactory;
    }

    private static final String KEY_TMP = "Sequence:{}:{}";

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
