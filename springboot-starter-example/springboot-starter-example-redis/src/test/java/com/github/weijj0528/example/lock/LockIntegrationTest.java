package com.github.weijj0528.example.lock;

import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.wei.starter.lock.WeiLock;
import com.wei.starter.lock.impl.RedisLock;
import com.wei.starter.lock.service.LockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 锁集成测试：基于 Testcontainers 真实 Redis，验证 RedisLock/RedissonLock 端到端行为。
 *
 * @author William.Wei
 */
@DisplayName("锁集成测试（Testcontainers Redis）")
public class LockIntegrationTest extends RedisExampleApplicationTest {

    @Resource
    private LockService lockService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("RedissonLock: 加锁成功后可释放，再次加锁成功")
    public void redissonLock_acquireReleaseReacquire() throws InterruptedException {
        WeiLock lock = lockService.getRedisLock("it:redisson:basic", 3, 10);
        assertTrue(lock.tryLock(2, TimeUnit.SECONDS), "首次加锁应成功");
        lock.unlock();
        assertTrue(lock.tryLock(2, TimeUnit.SECONDS), "释放后应可再次加锁");
        lock.unlock();
    }

    @Test
    @DisplayName("RedissonLock: 可重入，同线程嵌套加锁不死锁")
    public void redissonLock_reentrant() throws InterruptedException {
        WeiLock lock = lockService.getRedisLock("it:redisson:reentrant", 3, 30);
        assertTrue(lock.tryLock(2, TimeUnit.SECONDS));
        assertTrue(lock.tryLock(1, TimeUnit.SECONDS), "RedissonLock 应支持同线程可重入");
        lock.unlock();
        lock.unlock();
    }

    @Test
    @DisplayName("RedissonLock: 互斥——并发线程仅一个能持锁")
    public void redissonLock_mutualExclusion() throws InterruptedException {
        WeiLock lock = lockService.getRedisLock("it:redisson:mutex", 3, 30);
        assertTrue(lock.tryLock(2, TimeUnit.SECONDS));

        AtomicInteger heldByOther = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(1);
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(() -> {
            try {
                WeiLock other = lockService.getRedisLock("it:redisson:mutex", 0, 30);
                if (other.tryLock(0, 10, TimeUnit.SECONDS)) {
                    heldByOther.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            done.countDown();
        });
        assertTrue(done.await(5, TimeUnit.SECONDS), "另一线程应在 waitTime=0 时快速返回");
        assertEquals(0, heldByOther.get(), "持锁期间其他线程不应获取到锁");
        pool.shutdownNow();
        lock.unlock();
    }

    @Test
    @DisplayName("RedisLock(原生 Redis): 加锁/释放/再获取")
    public void redisLock_acquireReleaseReacquire() throws InterruptedException {
        RedisLock lock = new RedisLock("it:redis:basic", 0, 10, redisTemplate.getConnectionFactory());
        assertTrue(lock.tryLock(2, TimeUnit.SECONDS), "首次加锁应成功");
        lock.unlock();
        assertTrue(lock.tryLock(2, TimeUnit.SECONDS), "释放后应可再次加锁");
        lock.unlock();
    }

    @Test
    @DisplayName("RedisLock: 互斥——waitTime=0 时另一实例拿不到锁返回 false")
    public void redisLock_mutualExclusion() throws InterruptedException {
        RedisLock holder = new RedisLock("it:redis:mutex", 0, 30, redisTemplate.getConnectionFactory());
        assertTrue(holder.tryLock(2, TimeUnit.SECONDS));

        RedisLock other = new RedisLock("it:redis:mutex", 0, 30, redisTemplate.getConnectionFactory());
        assertFalse(other.tryLock(0, TimeUnit.SECONDS), "持锁期间其他实例应获取失败");
        holder.unlock();
    }

    @Test
    @DisplayName("RedissonLock: 租约过期后其他实例可获取")
    public void redissonLock_leaseExpiry() throws InterruptedException {
        WeiLock holder = lockService.getRedisLock("it:redisson:expire", 0, 1);
        assertTrue(holder.tryLock(1, TimeUnit.SECONDS));
        // 等待租约过期
        Thread.sleep(1500);

        WeiLock other = lockService.getRedisLock("it:redisson:expire", 0, 10);
        assertTrue(other.tryLock(2, TimeUnit.SECONDS), "租约过期后其他实例应可获取");
        other.unlock();
    }
}
