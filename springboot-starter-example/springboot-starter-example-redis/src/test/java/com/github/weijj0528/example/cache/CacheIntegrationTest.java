package com.github.weijj0528.example.cache;

import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.github.weijj0528.example.cache.entity.UserInfo;
import com.github.weijj0528.example.cache.service.WeiCacheService;
import com.wei.starter.base.bean.Result;
import com.wei.starter.redis.service.IRedisIncrService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 缓存 + 原子操作集成测试：基于 Testcontainers 真实 Redis。
 * <p>
 * 复用示例模块已有的 {@link WeiCacheService} 实现验证声明式缓存，
 * 验证 {@code @RedisCacheable}/Spring Cache 命中与 {@code @CacheTtl} 动态 TTL。
 *
 * @author William.Wei
 */
@DisplayName("缓存与原子操作集成测试（Testcontainers Redis）")
public class CacheIntegrationTest extends RedisExampleApplicationTest {

    @Resource(name = "defaultCacheService")
    private WeiCacheService defaultCacheService;

    @Resource(name = "ttlCacheService")
    private WeiCacheService ttlCacheService;

    @Resource
    private IRedisIncrService redisIncrService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @org.junit.jupiter.api.BeforeEach
    void flushRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("@Cacheable: 相同 key 第二次命中缓存")
    public void cacheable_secondCallHitsCache() {
        UserInfo query = new UserInfo();
        query.setId(1L);
        UserInfo first = defaultCacheService.addCache(query);
        UserInfo second = defaultCacheService.addCache(query);
        assertNotNull(first);
        assertEquals(first.getName(), second.getName(), "应返回缓存中的同一结果");
    }

    @Test
    @DisplayName("@CachePut: 更新后缓存被刷新")
    public void cachePut_updatesCache() {
        UserInfo query = new UserInfo();
        query.setId(2L);
        defaultCacheService.addCache(query);
        UserInfo updated = defaultCacheService.updateCache(query);
        assertEquals("updateCache", updated.getName(), "updateCache 应写入 updateCache 结果");
    }

    @Test
    @DisplayName("@CacheEvict: 删除后缓存失效")
    public void cacheEvict_removesCache() {
        UserInfo query = new UserInfo();
        query.setId(3L);
        defaultCacheService.addCache(query);
        defaultCacheService.removeCache(query);
        // 删除后再次查询，应重新执行方法返回 addCache 结果
        UserInfo after = defaultCacheService.addCache(query);
        assertEquals("addCache", after.getName(), "删除缓存后应重新执行方法");
    }

    @Test
    @DisplayName("@CacheTtl: ttlCacheService 缓存可正常读写")
    public void cacheTtl_worksOnTtlCacheService() {
        UserInfo query = new UserInfo();
        query.setId(4L);
        UserInfo first = ttlCacheService.addCache(query);
        UserInfo second = ttlCacheService.addCache(query);
        assertNotNull(first);
        assertEquals(first.getName(), second.getName(), "TTL 缓存命中应返回一致结果");
    }

    @Test
    @DisplayName("IRedisIncrService.incr: 原子自增与读取")
    public void incrService_atomicIncrement() {
        String[] keys = {"{it}:incr:k1", "{it}:incr:k2"};
        Long[] deltas = {5L, 3L};
        Integer[] expires = {0, 0};
        redisIncrService.incr(keys, deltas, expires);

        Result<Map<String, Long>> result = redisIncrService.getIncr(keys);
        assertTrue(result.successfully(), "getIncr 应成功");
        assertEquals(5L, result.getData().get("{it}:incr:k1"));
        assertEquals(3L, result.getData().get("{it}:incr:k2"));
    }

    @Test
    @DisplayName("IRedisIncrService.incr: 扣减至负数时回滚，保持 >=0")
    public void incrService_rollbackOnNegative() {
        String[] keys = {"{it}:rollback:k1"};
        Long[] deltas = {2L};
        Integer[] expires = {0};
        // 先加 2
        redisIncrService.incr(keys, deltas, expires);
        // 尝试扣减 5，应失败回滚（最终值仍为 2）
        redisIncrService.incr(keys, new Long[]{-5L}, new Integer[]{0});

        Result<Map<String, Long>> result = redisIncrService.getIncr(keys);
        assertEquals(2L, result.getData().get("{it}:rollback:k1"),
                "扣减至负数应回滚，值保持 2");
    }
}
