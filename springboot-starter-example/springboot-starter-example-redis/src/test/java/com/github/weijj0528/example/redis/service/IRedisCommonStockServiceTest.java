package com.github.weijj0528.example.redis.service;

import com.github.weijj0528.example.redis.RedisExampleApplicationTest;
import com.wei.starter.base.bean.Result;
import com.wei.starter.redis.model.StockDTO;
import com.wei.starter.redis.model.StockInitDTO;
import com.wei.starter.redis.model.StockModifyDTO;
import com.wei.starter.redis.model.StockUpdateDTO;
import com.wei.starter.redis.service.IRedisCommonStockService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class IRedisCommonStockServiceTest extends RedisExampleApplicationTest {

    @Resource
    private IRedisCommonStockService redisCommonStockService;

    @Test
    public void stockSet() {
        final StockInitDTO initDTO = new StockInitDTO();
        initDTO.setKey("123");
        // initDTO.setUsable(101);
        initDTO.setExpire(-1);
        final Result<StockModifyDTO> result = redisCommonStockService.stockSet(initDTO);
        System.out.println(result);
    }

    @Test
    public void usableSub() {
        final StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey("123");
        updateDTO.setUsable(-10);
        final Result<StockDTO> result = redisCommonStockService.usableSub(updateDTO);
        System.out.println(result);
    }

    @Test
    public void usableAdd() {
        final StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey("123");
        updateDTO.setUsable(10);
        final Result<StockDTO> result = redisCommonStockService.usableAdd(updateDTO);
        System.out.println(result);
    }

    @Test
    public void lock() {
        final StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey("123");
        updateDTO.setLock(10);
        final Result<StockDTO> result = redisCommonStockService.lock(updateDTO);
        System.out.println(result);
    }

    @Test
    public void unlock() {
        final StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey("123");
        updateDTO.setLock(-10);
        updateDTO.setUsable(-updateDTO.getLock());
        final Result<StockDTO> result = redisCommonStockService.unLock(updateDTO);
        System.out.println(result);
    }

    @Test
    public void lockSub() {
        final StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey("123");
        updateDTO.setLock(-10);
        updateDTO.setUsable(6);
        final Result<StockDTO> result = redisCommonStockService.unLock(updateDTO);
        System.out.println(result);
    }

}
