package com.wei.starter.redis.lock;

import org.springframework.data.redis.connection.RedisConnectionFactory;

public class LockService {

    /**
     * 连接工厂
     */
    private RedisConnectionFactory redisConnectionFactory;

    public LockService(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 获取Redis锁
     *
     * @param lockKey
     * @return
     */
    public RedisLock getRedisLock(String lockKey) {
        return new RedisLock(lockKey, redisConnectionFactory);
    }

}
