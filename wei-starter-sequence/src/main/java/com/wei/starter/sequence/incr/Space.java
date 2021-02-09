package com.wei.starter.sequence.incr;

import lombok.Data;

/**
 * The type Space.
 * 空间
 */
@Data
public class Space {

    /**
     * The Start.
     * 开始值
     */
    private long start;

    /**
     * The End.
     * 结束值（包含可使用）
     */
    private long end;

    /**
     * Instantiates a new Space.
     *
     * @param start the start
     * @param end   the end
     */
    public Space(long start, long end) {
        this.start = start;
        this.end = end;
        if (start < 0 || end < start) {
            throw new RuntimeException("Space error!");
        }
    }
}
