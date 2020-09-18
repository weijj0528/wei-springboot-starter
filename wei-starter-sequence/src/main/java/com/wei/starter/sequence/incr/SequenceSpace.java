package com.wei.starter.sequence.incr;

/**
 * 序列空间
 *
 * @author william.wei
 */
public interface SequenceSpace {


    /**
     * 获取下一个序号
     *
     * @return long long
     */
    long next();


    /**
     * Is empty boolean.
     * 是否空
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Use ratio double.
     * 使用情况
     *
     * @return the double
     */
    double useRatio();


    /**
     * Reset.
     * 重置空间
     */
    void reset(Space space);

}
