package com.wei.starter.redis.service;


import com.wei.starter.base.bean.Result;
import com.wei.starter.redis.model.StockDTO;
import com.wei.starter.redis.model.StockInitDTO;
import com.wei.starter.redis.model.StockModifyDTO;
import com.wei.starter.redis.model.StockUpdateDTO;

import java.util.Collection;
import java.util.List;

/**
 * The interface Redis common stock service.
 * 基于Redis 的通用库存服务
 * 库存数据结构设计
 * {key}:单商品库存KEY
 * |--usable:count 可用库存
 * |--lock:count 锁定库存
 * |--used:count 使用库存
 * |--total:count 总库存
 * 库存流转使用场景
 * usable->lock:剩余库存锁定
 * lock->usable:锁定库存解锁
 * lock->used:锁定库存消耗
 * usable->used:剩余消耗
 * 库存数量约定
 * total = count + lock + used
 *
 * @author William.Wei
 */
public interface IRedisCommonStockService {
    /**
     * Stock set
     * 库存设置，返回与设置前的变化量，新设置量应大于已使用量，设置出错自动回滚
     *
     * @param initDTO the init dto
     * @return the result
     */
    Result<StockModifyDTO> stockSet(StockInitDTO initDTO);

    /**
     * Stock set result.
     *
     * @param initDTO the init dto
     * @return the result
     */
    Result<List<StockModifyDTO>> stockSet(Collection<StockInitDTO> initDTO);

    /**
     * Gets stock.
     *
     * @param key the key
     * @return the stock
     */
    Result<StockDTO> getStock(String key);

    /**
     * Gets stock.
     *
     * @param key the key
     * @return the stock
     */
    Result<List<StockDTO>> getStock(Collection<String> key);

    /**
     * Stock add result.
     *
     * @param updateDTO the update dto
     * @return the result
     */
    Result<StockDTO> usableAdd(StockUpdateDTO updateDTO);

    /**
     * Usable add result.
     *
     * @param updateDTOList the update dto list
     * @return the result
     */
    Result<List<StockDTO>> usableAdd(Collection<StockUpdateDTO> updateDTOList);

    /**
     * Stock sub result.
     *
     * @param updateDTO the update dto
     * @return the result
     */
    Result<StockDTO> usableSub(StockUpdateDTO updateDTO);

    /**
     * Usable sub result.
     *
     * @param updateDTOList the update dto list
     * @return the result
     */
    Result<List<StockDTO>> usableSub(Collection<StockUpdateDTO> updateDTOList);

    /**
     * Stock lock result.
     *
     * @param updateDTO the update dto
     * @return the result
     */
    Result<StockDTO> lock(StockUpdateDTO updateDTO);

    /**
     * Lock result.
     *
     * @param updateDTOList the update dto list
     * @return the result
     */
    Result<List<StockDTO>> lock(Collection<StockUpdateDTO> updateDTOList);

    /**
     * Stock un lock result.
     *
     * @param updateDTO the update dto
     * @return the result
     */
    Result<StockDTO> unLock(StockUpdateDTO updateDTO);

    /**
     * Un lock result.
     *
     * @param updateDTOList the update dto list
     * @return the result
     */
    Result<List<StockDTO>> unLock(Collection<StockUpdateDTO> updateDTOList);

    /**
     * Batch update result.
     *
     * @param updateDTOList the update dto list
     * @return the result
     */
    Result<List<StockDTO>> batchUpdate(Collection<StockUpdateDTO> updateDTOList);

}
