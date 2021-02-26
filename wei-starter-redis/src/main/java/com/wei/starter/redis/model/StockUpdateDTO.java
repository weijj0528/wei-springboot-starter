package com.wei.starter.redis.model;

import lombok.Data;

/**
 * The type Stock update dto.
 * 库存更新
 *
 * @author William.Wei
 */
@Data
public class StockUpdateDTO {

    /**
     * 库存KEY
     */
    private String key;

    /**
     * 库存数量
     * 正：添加，返还
     * 负：消耗
     */
    private Integer usable;

    /**
     * 锁定数量
     * 正：锁定
     * 负：解锁、消耗
     */
    private Integer lock;

    /**
     * 更新是否成功标识
     */
    private Boolean flag;

}
