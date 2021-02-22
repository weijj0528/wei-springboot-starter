package com.github.weijj0528.example.base.sequence;

import com.github.weijj0528.example.base.BaseExampleApplicationTest;
import com.wei.starter.sequence.DbSequence;
import com.wei.starter.sequence.RedisSequence;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 * @createTime 2019/4/27 15:48
 * @description
 */
public class SequenceTest extends BaseExampleApplicationTest {

    @Autowired
    private RedisSequence redisSequence;
    @Autowired
    private DbSequence dbSequence;

    @Test
    public void redisSequenceTest() {
        String sysName = "example";
        String key = "test";
        long andIncrement = redisSequence.getAndIncrement(sysName, key);
        System.out.println(andIncrement);
    }

    @Test
    public void dbSequenceTest() throws InterruptedException {
        String sysName = "example";
        String key = "test";
        long andIncrement = dbSequence.getAndIncrement(sysName, key);
        System.out.println(andIncrement);
        final CountDownLatch latch = new CountDownLatch(10);
        HashSet<String> idSet = new HashSet<>(10 * 1000);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        long andIncrement1 = dbSequence.getAndIncrement(sysName, key);
                        System.err.println(Thread.currentThread().getName() + ":" + andIncrement1);
                        idSet.add(String.valueOf(andIncrement1));
                    }
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
        System.out.println(idSet.size());
        System.out.println("DbSequenceTest finish!");
    }

}
