package com.wei.starter.cache.advice;

import com.wei.starter.cache.CacheTtlContext;
import com.wei.starter.cache.annotation.CacheTtl;
import com.wei.starter.lock.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * The type Lock aspect.
 * 声明式锁
 *
 * @author William.Wei
 */
@Slf4j
@Aspect
public class CacheTtlAspect implements Ordered {
    @Override
    public int getOrder() {
        // 最外层执行
        return Integer.MIN_VALUE;
    }

    @Around("@annotation(com.wei.starter.cache.annotation.CacheTtl)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                Method method = methodSignature.getMethod();
                CacheTtl expireAt = method.getAnnotation(CacheTtl.class);
                if (expireAt == null) {
                    Class<?> declaringType = methodSignature.getDeclaringType();
                    expireAt = declaringType.getAnnotation(CacheTtl.class);
                }
                long ttl = expireAt.value();
                CacheTtlContext.setTtl(ttl);
                log.debug("set ttl:{}", ttl);
            }
            return ((MethodInvocationProceedingJoinPoint) joinPoint).proceed(joinPoint.getArgs());
        } finally {
            CacheTtlContext.remove();
        }
    }
}
