package com.github.weijj0528.example.base.dto;

import com.wei.starter.base.bean.Range;
import lombok.Data;

import java.util.Date;

@Data
public class QueryDto {

    Range<Integer> range1;

    Range<Long> range2;

    Range<Date> range3;

}
