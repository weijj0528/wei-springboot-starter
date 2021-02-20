package com.wei.starter.lock;

import com.wei.starter.lock.advice.LockAspect;
import com.wei.starter.lock.service.LockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * The type Lock auto config.
 * 锁相关配置
 *
 * @author William.Wei
 */
@Configuration
public class LockAutoConfig {

    @Bean
    public LockAspect lockAspect() {
        return new LockAspect();
    }


    @Bean
    public LockService lockService(RedisConnectionFactory redisConnectionFactory) {
        return new LockService(redisConnectionFactory);
    }

}
