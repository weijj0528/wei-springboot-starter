package com.wei.springboot.starter.advice;

import com.alibaba.fastjson.JSON;
import com.wei.starter.base.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @createTime 2019/7/20 22:40
 * @description Pv日志打印，请求参数与响应
 */
@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    /**
     * 返回 Result 的接口拦截
     *
     * @param joinPoint 切点
     * @return
     * @throws Throwable
     */
    @Around("execution(public com.wei.starter.base.bean.Result *..*(..)) && (" +
            "@within(org.springframework.stereotype.Controller) ||" +
            "@within(org.springframework.web.bind.annotation.RestController))")
    public Result<Object> around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 请求参数
        String params = null;
        Object[] args = joinPoint.getArgs();
        // 文件上传请求不做处理
        for (Object arg : args) {
            if (arg instanceof MultipartFile ||
                    arg instanceof MultipartHttpServletRequest) {
                return (Result<Object>) joinPoint.proceed();
            }
        }
        if (args.length == 0) {
            params = "no params!";
        } else if (args.length == 1) {
            params = JSON.toJSONString(args[0]);
        } else {
            Map<String, Object> paramsMap = new HashMap<>();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Parameter[] parameters = methodSignature.getMethod().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (!(args[i] instanceof ServletRequest) &&
                        !(args[i] instanceof ServletResponse) &&
                        !(args[i] instanceof BindingResult)) {
                    paramsMap.put(parameters[i].getName(), args[i]);
                }
            }
            params = JSON.toJSONString(paramsMap);
        }
        log.info("[RQ] {}", params);
        Result<Object> result = (Result<Object>) joinPoint.proceed();
        // 响应
        log.info("[RP] {}", JSON.toJSONString(result));
        return result;
    }

}
