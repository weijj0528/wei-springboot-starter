package com.wei.starter.sequence;

import com.wei.starter.sequence.incr.SpaceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class SequenceAutoConfig {

    @Bean
    @ConditionalOnBean(SpaceFactory.class)
    public DbSequence dbSequence(SpaceFactory spaceFactory) {
        return new DbSequence(spaceFactory);
    }

    @Bean
    public RedisSequence redisSequence(RedisConnectionFactory redisConnectionFactory) {
        return new RedisSequence(redisConnectionFactory);
    }
}
