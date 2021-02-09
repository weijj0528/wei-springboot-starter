package com.wei.starter.sequence.incr;

/**
 * The type Space factory.
 * 空间工厂
 */
public abstract class SpaceFactory {


    /**
     * Create space space.
     * 创建新的空间
     *
     * @return the space
     * @param sysName
     * @param bizKey
     */
    public abstract Space createSpace(String sysName, String bizKey);

}
