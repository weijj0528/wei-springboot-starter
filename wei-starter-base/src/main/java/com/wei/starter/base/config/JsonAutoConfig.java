package com.wei.starter.base.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wei.starter.base.util.WeiJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeParser;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.TimeZone;

/**
 * The type Json auto config.
 * JSON序列化相关配置
 *
 * @author Administrator
 * @createTime 2019 /3/17 16:00
 */
@Slf4j
@Configuration
public class JsonAutoConfig {

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        //  FastJson配置
        // 禁用循环引用
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        log.info("FastJson global parser config init!");
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .timeZone(TimeZone.getTimeZone("GMT+:08:00"))
                .modules(new JavaTimeModule()
                                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatterBuilder().toFormatter()))
                                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                        .withZone(ZoneId.of("Asia/Shanghai")))),
                        new SimpleModule()
                                .addDeserializer(Date.class, new DateDeserializer(jodaTimeFormatterBuilder()))
                                .addSerializer(Date.class, new DateSerializer(org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                                        .withZone(DateTimeZone.forID("Asia/Shanghai")))))
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 将 Spring 容器中配置好的 ObjectMapper 同步到 WeiJsonUtils 静态工具类，
     * 使其共享 JavaTimeModule 与时区配置。
     */
    @Bean("weiJsonUtilsInitializer")
    public Runnable weiJsonUtilsInitializer(ObjectMapper objectMapper) {
        WeiJsonUtils.setObjectMapper(objectMapper);
        return () -> {
        };
    }


    private DateTimeFormatterBuilder dateTimeFormatterBuilder() {
        return new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private org.joda.time.format.DateTimeFormatter jodaTimeFormatterBuilder() {
        return new org.joda.time.format.DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[]{
                        DateTimeFormat.forPattern("yyyy-MM-dd").getParser(),
                        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").getParser(),
                        DateTimeFormat.forPattern("yyyy/MM/dd").getParser(),
                        DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").getParser(),
                }).toFormatter();
    }

}
