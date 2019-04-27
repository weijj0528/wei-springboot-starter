package com.wei.springbootstarterexample.sequence;

import com.wei.springboot.starter.sequence.RedisSequence;
import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 * @createTime 2019/4/27 15:48
 * @description
 */
public class SequenceTest extends SpringbootStarterExampleApplicationTests {

    @Autowired
    private RedisSequence redisSequence;

    @Test
    public void redisSequenceTest() {
        String sysName = "example";
        String key = "test";
        long andIncrement = redisSequence.getAndIncrement(sysName, key);
        System.out.println(andIncrement);
    }

}
