package com.github.weijj0528.example.redis.service;

import com.alibaba.fastjson.JSON;
import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.wei.starter.base.bean.Result;
import com.wei.starter.redis.service.IRedisIncrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class IRedisIncrServiceTest extends RedisExampleApplicationTest {

    @Autowired
    private IRedisIncrService redisIncrService;

    @Test
    public void getIncr() {

    }

    @Test
    public void incr() {
        HashMap<String, Long> keyArgs = new HashMap<>();
        keyArgs.put("test0", 0L);
        keyArgs.put("test1", 100L);
        Result<Map<String, Long>> result = redisIncrService.incr(keyArgs);
        System.out.println(JSON.toJSONString(result));
        Result<Map<String, Long>> mapResult = redisIncrService.getIncr(keyArgs.keySet().toArray(new String[0]));
        System.out.println(JSON.toJSONString(mapResult));
    }

    @Test
    public void getHincr() {
        HashMap<String, Long> keyArgs = new HashMap<>();
        keyArgs.put("test0", 100L);
        keyArgs.put("test1", 100L);
        Result<Map<String, Long>> result = redisIncrService.hasIncr("stock", keyArgs);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void hincr() {
    }
}