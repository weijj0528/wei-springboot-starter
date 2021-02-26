package com.wei.starter.redis.model;

import lombok.Data;

/**
 * The type Stock update dto.
 *
 * @author William.Wei
 */
@Data
public class StockModifyDTO {

    /**
     * 库存KEY
     */
    private String key;

    /**
     * 可用数量
     */
    private Integer usable;

}
