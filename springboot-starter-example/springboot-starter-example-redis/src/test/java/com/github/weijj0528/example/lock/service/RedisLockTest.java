package com.github.weijj0528.example.lock.service;

import com.github.weijj0528.example.RedisExampleApplicationTest;
import com.wei.starter.lock.WeiLock;
import com.wei.starter.lock.service.LockService;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author William
 * @Date 2019/4/2
 * @Description 锁测试
 */
public class RedisLockTest extends RedisExampleApplicationTest {

    @Resource
    private Redisson redisson;
    @Resource
    private LockService lockService;

    @Test
    public void lockTest() throws IOException, InterruptedException {
        System.out.println(redisson);
        CountDownLatch latch = new CountDownLatch(100);

        MyTask task = new MyTask(latch);
        for (int i = 0; i < 100; i++) {
            new Thread(task).start();
        }
        latch.await();
        System.out.print("LockTest finish!");
    }

    public class MyTask implements Runnable {

        private int stock = 0;

        private final CountDownLatch latch;

        public MyTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            WeiLock redisLock = lockService.getRedisLock("addStock");
            try {
                String name = Thread.currentThread().getName();
                boolean b = redisLock.tryLock(3, 3, TimeUnit.SECONDS);
                if (!b) {
                    latch.countDown();
                    return;
                }
                stock++;
                System.err.println(name + " add --------------------now:" + stock);
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                redisLock.unlock();
            }
        }
    }

}
