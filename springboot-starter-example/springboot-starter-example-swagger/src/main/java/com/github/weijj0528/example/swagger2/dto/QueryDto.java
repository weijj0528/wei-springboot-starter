package com.github.weijj0528.example.swagger2.dto;

import com.wei.starter.base.bean.Range;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * The type Query dto.
 *
 * @author William.Wei
 */
@Data
@ApiModel(description = "查询条件")
public class QueryDto {

    /**
     * The Range 1.
     */
    @ApiModelProperty(name = "范围", notes = "年龄范围")
    Range<Integer> range1;

    /**
     * The Range 2.
     */
    Range<Long> range2;

    /**
     * The Range 3.
     */
    Range<Date> range3;

}
