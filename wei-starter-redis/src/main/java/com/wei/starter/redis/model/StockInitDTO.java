package com.wei.starter.redis.model;

import com.wei.starter.base.valid.Setting;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * The type Stock init dto.
 *
 * @author William.Wei
 */
@Data
public class StockInitDTO {

    /**
     * 库存KEY
     */
    @NotEmpty(groups = {Setting.class}, message = "库存KEY不能为空")
    private String key;

    /**
     * 可用数量
     */
    @NotEmpty(groups = {Setting.class}, message = "可用库存不能为空")
    private Integer usable;

    /**
     * 过期时间
     */
    private Integer expire;

}
