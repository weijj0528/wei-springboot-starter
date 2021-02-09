package com.wei.starter.sequence;

import com.wei.starter.sequence.incr.SequenceSpace;
import com.wei.starter.sequence.incr.SpaceFactory;

/**
 * The type Double buffer sequence.
 * 双缓冲序列
 */
public class DoubleBufferSequence {

    private SequenceSpace sequenceSpace0;

    private SequenceSpace sequenceSpace1;

    private SequenceSpace currentSpace;

    private SpaceFactory spaceFactory;

    private static final double SPACE_RESET_RATIO = 0.2;

    public DoubleBufferSequence(SequenceSpace sequenceSpace0, SequenceSpace sequenceSpace1, SpaceFactory spaceFactory) {
        this.sequenceSpace0 = sequenceSpace0;
        this.sequenceSpace1 = sequenceSpace1;
        this.spaceFactory = spaceFactory;
        currentSpace = sequenceSpace0;
    }


    public synchronized long getAndIncrement(String sysName, String bizKey) {
        if (currentSpace.isEmpty()) {
            currentSpace.reset(spaceFactory.createSpace(sysName, bizKey));
        }
        long next = currentSpace.next();
        SequenceSpace leisureSpace = getLeisureSpace();
        if (currentSpace.useRatio() >= SPACE_RESET_RATIO && leisureSpace.isEmpty()) {
            leisureSpace.reset(spaceFactory.createSpace(sysName, bizKey));
        }
        if (currentSpace.isEmpty()) {
            // 交换当前使用的序列空间
            currentSpace = leisureSpace;
        }
        return next;
    }

    private SequenceSpace getLeisureSpace() {
        // 获取空闲的那个序列空间
        if (currentSpace == sequenceSpace0) {
            return sequenceSpace1;
        } else {
            return sequenceSpace0;
        }
    }


}
