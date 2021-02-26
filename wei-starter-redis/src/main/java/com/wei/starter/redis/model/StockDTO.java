package com.wei.starter.redis.model;

import lombok.Data;

/**
 * The type Stock update dto.
 * 库存更新
 *
 * @author William.Wei
 */
@Data
public class StockDTO {

    /**
     * 库存KEY
     */
    private String key;

    /**
     * 库存数量
     */
    private Integer usable;

    /**
     * 锁定数量
     */
    private Integer lock;

    /**
     * 使用数量
     */
    private Integer used;

    /**
     * 总数量
     */
    private Integer total;

}
