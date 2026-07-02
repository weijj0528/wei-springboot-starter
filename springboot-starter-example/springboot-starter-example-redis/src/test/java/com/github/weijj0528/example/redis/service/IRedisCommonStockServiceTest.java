package com.github.weijj0528.example.redis.service;

import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.wei.starter.base.bean.Result;
import com.wei.starter.redis.model.StockDTO;
import com.wei.starter.redis.model.StockInitDTO;
import com.wei.starter.redis.model.StockUpdateDTO;
import com.wei.starter.redis.service.IRedisCommonStockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 库存服务集成测试：基于 Testcontainers 真实 Redis。
 * <p>
 * 库存流转模型（见 stock_update.lua）：
 * <ul>
 *   <li>stockSet(usable): 设可用库存，total=usable，used=0</li>
 *   <li>lock(+n): usable -= n, lock += n（可用→锁定）</li>
 *   <li>unLock(-n, usable=+n): lock -= n, usable += n（锁定→可用）</li>
 *   <li>usableSub(usable=-n): usable -= n, used += n（可用→已用）</li>
 *   <li>usableAdd(usable=+n): usable += n, used -= n（已用→可用，需 used>=n）</li>
 * </ul>
 * 每个用例独立初始化库存并清理，避免用例间相互依赖。
 *
 * @author William.Wei
 */
@DisplayName("库存服务集成测试（Testcontainers Redis）")
public class IRedisCommonStockServiceTest extends RedisExampleApplicationTest {

    private static final String KEY = "it:stock:123";

    @Resource
    private IRedisCommonStockService redisCommonStockService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void cleanup() {
        redisTemplate.delete(KEY);
    }

    private void initStock(int usable) {
        StockInitDTO initDTO = new StockInitDTO();
        initDTO.setKey(KEY);
        initDTO.setUsable(usable);
        initDTO.setExpire(-1);
        redisCommonStockService.stockSet(initDTO);
    }

    private StockDTO current() {
        return redisCommonStockService.getStock(KEY).getData();
    }

    @Test
    @DisplayName("stockSet: 初始化库存")
    public void stockSet() {
        initStock(100);
        StockDTO stock = current();
        assertNotNull(stock);
        assertEquals(100, stock.getUsable());
        assertEquals(100, stock.getTotal());
        // used 在初始化时未设置，可能为 null 或 0
        Integer used = stock.getUsed();
        assertTrue(used == null || used == 0, "初始化后 used 应为 0 或未设置");
    }

    @Test
    @DisplayName("lock: 可用库存锁定")
    public void lock() {
        initStock(100);
        StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey(KEY);
        updateDTO.setLock(10);
        Result<StockDTO> result = redisCommonStockService.lock(updateDTO);
        assertTrue(result.successfully(), "lock 应成功");
        assertEquals(90, result.getData().getUsable());
        assertEquals(10, result.getData().getLock());
    }

    @Test
    @DisplayName("unlock: 锁定库存解锁回可用")
    public void unlock() {
        initStock(100);
        // 先锁定 10
        StockUpdateDTO lockDTO = new StockUpdateDTO();
        lockDTO.setKey(KEY);
        lockDTO.setLock(10);
        redisCommonStockService.lock(lockDTO);
        // 解锁 10 回到可用
        StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey(KEY);
        updateDTO.setLock(-10);
        updateDTO.setUsable(-updateDTO.getLock());
        Result<StockDTO> result = redisCommonStockService.unLock(updateDTO);
        assertTrue(result.successfully(), "unlock 应成功");
        assertEquals(100, result.getData().getUsable());
        assertEquals(0, result.getData().getLock());
    }

    @Test
    @DisplayName("usableSub: 可用库存消耗为已用")
    public void usableSub() {
        initStock(100);
        StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey(KEY);
        updateDTO.setUsable(-10);
        Result<StockDTO> result = redisCommonStockService.usableSub(updateDTO);
        assertTrue(result.successfully(), "usableSub 应成功");
        assertEquals(90, result.getData().getUsable());
        assertEquals(10, result.getData().getUsed());
    }

    @Test
    @DisplayName("usableAdd: 已用库存返还为可用")
    public void usableAdd() {
        initStock(100);
        // 先消耗 10 到 used
        StockUpdateDTO subDTO = new StockUpdateDTO();
        subDTO.setKey(KEY);
        subDTO.setUsable(-10);
        redisCommonStockService.usableSub(subDTO);
        // 再将 10 从 used 返还到 usable
        StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey(KEY);
        updateDTO.setUsable(10);
        Result<StockDTO> result = redisCommonStockService.usableAdd(updateDTO);
        assertTrue(result.successfully(), "usableAdd 应成功");
        assertEquals(100, result.getData().getUsable());
        assertEquals(0, result.getData().getUsed());
    }

    @Test
    @DisplayName("lockSub: 锁定量不足时解锁消耗失败")
    public void lockSub() {
        initStock(100);
        // 锁定 10
        StockUpdateDTO lockDTO = new StockUpdateDTO();
        lockDTO.setKey(KEY);
        lockDTO.setLock(10);
        redisCommonStockService.lock(lockDTO);
        // 尝试解锁 20（超出锁定量 10），应失败抛出 ErrorMsgException
        StockUpdateDTO updateDTO = new StockUpdateDTO();
        updateDTO.setKey(KEY);
        updateDTO.setLock(-20);
        updateDTO.setUsable(6);
        org.junit.jupiter.api.Assertions.assertThrows(
                com.wei.starter.base.exception.ErrorMsgException.class,
                () -> redisCommonStockService.unLock(updateDTO),
                "锁定量不足应抛出 ErrorMsgException");
    }
}
