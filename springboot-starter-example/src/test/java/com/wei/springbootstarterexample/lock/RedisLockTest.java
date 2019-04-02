package com.wei.springbootstarterexample.lock;

import com.wei.springboot.starter.lock.RedisLock;
import com.wei.springbootstarterexample.SpringbootStarterExampleApplicationTests;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author William
 * @Date 2019/4/2
 * @Description 锁测试
 */
public class RedisLockTest extends SpringbootStarterExampleApplicationTests {

    public class MyTask implements Runnable {

        private int stock = 0;

        @Override
        public void run() {
            RedisLock redisLock = new RedisLock("addStock");
            try {
                String name = Thread.currentThread().getName();
                boolean b = redisLock.tryLock(1, 1, TimeUnit.SECONDS);
                if (!b) {
                    return;
                }
                stock++;
                System.err.println(name + " add --------------------now:" + stock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                redisLock.unlock();
            }
        }
    }

    @Test
    public void lockTest() throws IOException {
        MyTask task = new MyTask();
        for (int i = 0; i < 100; i++) {
            new Thread(task).start();
        }
        System.in.read();
    }

}
