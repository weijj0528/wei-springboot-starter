package com.wei.starter.base.config;

import com.wei.starter.base.bean.Range;
import com.wei.starter.base.intercepter.MdcInterceptor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Administrator
 * @createTime 2019/3/17 16:02
 * @description
 */
@Configuration
public class WeiMvcConfigurer implements WebMvcConfigurer {

    public static final String SUCCESS = "success.html";
    public static final String SWAGGER = "doc.html";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName(SUCCESS);
        registry.addViewController("/docs").setViewName(SWAGGER);
        registry.addViewController("/").setViewName("redirect:/index");
        registry.addViewController("/swagger").setViewName("redirect:/docs");
        registry.addViewController("/swagger2").setViewName("redirect:/docs");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MdcInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[]{
                        DateTimeFormat.forPattern("yyyy-MM-dd").getParser(),
                        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser(),
                        DateTimeFormat.forPattern("yyyy/MM/dd").getParser(),
                        DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").getParser(),
                }).toFormatter();
        registry.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (!StringUtils.hasText(source)) {
                    return null;
                }
                DateTime d = DateTime.parse(source);
                return LocalDateTime.of(d.getYear(), d.getMonthOfYear(), d.getDayOfMonth(), d.getHourOfDay(), d.getMinuteOfHour(), d.getSecondOfMinute(), d.getMillisOfSecond());
            }
        });
        registry.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if (!StringUtils.hasText(source)) {
                    return null;
                }
                DateTime d = DateTime.parse(source);
                return LocalDate.of(d.getYear(), d.getMonthOfYear(), d.getDayOfMonth());
            }
        });
        registry.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                if (!StringUtils.hasText(source)) {
                    return null;
                }
                return DateTime.parse(source, fmt).toDate();
            }
        });
        registry.addConverter(new Converter<String, DateTime>() {
            @Override
            public DateTime convert(String source) {
                if (!StringUtils.hasText(source)) {
                    return null;
                }
                return DateTime.parse(source, fmt);
            }
        });
        registry.addConverter(new Converter<String, Range<?>>() {
            @Override
            public Range<?> convert(String source) {
                if (!StringUtils.hasText(source)) {
                    return null;
                }
                return Range.converter(source);
            }
        });
    }
}
