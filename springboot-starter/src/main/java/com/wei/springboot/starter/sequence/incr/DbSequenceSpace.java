package com.wei.springboot.starter.sequence.incr;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Db sequence space.
 */
public class DbSequenceSpace implements SequenceSpace {

    private long start = 0;

    private long end = 0;

    private AtomicLong current = new AtomicLong(0);


    @Override
    public long next() {
        return current.incrementAndGet();
    }

    @Override
    public boolean isEmpty() {
        return current.get() == end;
    }

    @Override
    public double useRatio() {
        return (current.get() - start) * 1.0 / (end - start);
    }

    @Override
    public void reset(Space space) {
        if (space == null || space.getStart() < start) {
            throw new RuntimeException("DbSequenceSpace.reset error!");
        }
        start = space.getStart();
        end = space.getEnd();
        current = new AtomicLong(start);
    }
}
