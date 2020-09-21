package com.wei.springbootstarterexample.service;

import com.alibaba.fastjson.JSON;
import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import com.wei.starter.base.bean.Result;
import com.wei.starter.redis.service.IRedisIncrService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class IRedisIncrServiceTest extends SpringbootStarterExampleApplicationTests {

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
        Result<Map<String, Long>> result = redisIncrService.hincr("stock", keyArgs);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void hincr() {
    }

    @Test
    public void hStockSet() {
        HashMap<String, Long> keyArgs = new HashMap<>();
        keyArgs.put("test0", 1L);
        keyArgs.put("test1", 200L);
        Result<Map<String, Long>> result = redisIncrService.hStockSet("stock", keyArgs);
        System.out.println(JSON.toJSONString(result));
    }
}