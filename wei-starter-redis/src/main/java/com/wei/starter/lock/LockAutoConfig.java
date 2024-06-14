package com.wei.starter.lock;

import com.wei.starter.lock.advice.LockAspect;
import com.wei.starter.lock.service.LockService;
import com.wei.starter.redis.RedisAutoConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * The type Lock auto config.
 * 锁相关配置
 *
 * @author William.Wei
 */
@Slf4j
@Configuration
@AutoConfigureAfter({RedisAutoConfig.class, RedissonAutoConfiguration.class})
public class LockAutoConfig {


    /**
     * Redisson lock service
     *
     * @param redisson the redisson
     * @return the lock service
     */
    @Bean
    @Primary
    @ConditionalOnBean(RedissonClient.class)
    public LockService redissonLockService(RedissonClient redisson) {
        log.info("Redisson lock service init");
        return new LockService((Redisson) redisson);
    }

    /**
     * RedisConnectionFactory lock service
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the lock service
     */
    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnBean(RedisConnectionFactory.class)
    public LockService redisLockService(RedisConnectionFactory redisConnectionFactory) {
        log.info("RedisConnectionFactory lock service init");
        return new LockService(redisConnectionFactory);
    }

    /**
     * Lock aspect
     *
     * @return the lock aspect
     */
    @Bean
    @ConditionalOnBean(LockService.class)
    public LockAspect lockAspect() {
        log.info("Lock aspect init");
        return new LockAspect();
    }

}
