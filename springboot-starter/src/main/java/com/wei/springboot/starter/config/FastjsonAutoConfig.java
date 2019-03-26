package com.wei.springboot.starter.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 * @createTime 2019/3/17 16:00
 * @description Fasthjson自动配置类
 */
@Slf4j
@Configuration
public class FastjsonAutoConfig {

    @PostConstruct
    public void init() {
        log.info("FastJson global parser config init!");
        // 自动进行类型转换
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // 禁用循环引用
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    @Bean
    public FastJsonRedisSerializer fastJsonRedisSerializer() {
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        FastJsonConfig config = fastJsonRedisSerializer.getFastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteClassName);
        return fastJsonRedisSerializer;
    }

}
