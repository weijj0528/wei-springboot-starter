package com.github.weijj0528.example;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 集成测试 Redisson 配置：显式声明指向 Testcontainers 容器的 {@link RedissonClient}，
 * 以 @Primary 覆盖 redisson-spring-boot-starter 自动配置（其读取 application-dev.yml 的
 * 外部密码配置在测试环境下连不上容器）。
 *
 * @author William.Wei
 */
@TestConfiguration
public class RedisTestConfig {

    @Bean(destroyMethod = "shutdown")
    @Primary
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String host,
                                         @Value("${spring.redis.port}") int port) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(0);
        return Redisson.create(config);
    }
}
