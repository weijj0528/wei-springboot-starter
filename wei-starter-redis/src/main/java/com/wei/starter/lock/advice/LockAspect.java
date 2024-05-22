package com.wei.starter.lock.advice;

import com.wei.starter.lock.WeiLock;
import com.wei.starter.lock.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

import javax.annotation.Resource;

/**
 * The type Lock aspect.
 * 声明式锁
 *
 * @author William.Wei
 */
@Slf4j
@Aspect
public class LockAspect implements Ordered {

    @Resource
    private LockService lockService;

    @Override
    public int getOrder() {
        // 最外层执行
        return Integer.MAX_VALUE;
    }

    @Around("@annotation(com.wei.starter.lock.annotation.Lock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        WeiLock weiLock = lockService.getWeiLock(joinPoint);
        try {
            // 加锁
            weiLock.lock();
            return joinPoint.proceed();
        } finally {
            // 解锁
            weiLock.unlock();
        }
    }
}
