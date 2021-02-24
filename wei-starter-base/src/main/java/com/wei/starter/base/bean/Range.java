package com.wei.starter.base.bean;

import com.wei.starter.base.config.SpringContext;
import lombok.Data;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * The type Range.
 * 范围查询条件封装
 *
 * @author William.Wei
 */
@Data
public class Range<T> {

    /**
     * 开始
     */
    private T from;

    /**
     * 结束
     */
    private T to;

    /**
     * 范围能数转换
     * integer,10,100->
     * long,10,100->
     * datetime,2021-01-01 00:00:00,2022-01-01 00:00:00->
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> Range<T> converter(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }
        final ConversionService conversionService = SpringContext.getBean(ConversionService.class);
        final Range<T> range = new Range<>();
        final String[] split = source.split(",");
        if (split.length > 0) {
            Class<T> clazz = getType(split[0]);
            if (split.length > 1) {
                range.setFrom(conversionService.convert(split[1], clazz));
            }
            if (split.length > 2) {
                range.setTo(conversionService.convert(split[2], clazz));
            }
        }
        return range;
    }

    private static <T> Class<T> getType(String s) {
        switch (s) {
            case "integer":
                return (Class<T>) Integer.class;
            case "long":
                return (Class<T>) Long.class;
            case "double":
                return (Class<T>) Double.class;
            case "datetime":
                return (Class<T>) Date.class;
            default:
        }
        return (Class<T>) String.class;
    }

}
