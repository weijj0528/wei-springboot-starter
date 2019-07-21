package com.wei.springboot.starter.intercepter;

import cn.hutool.core.util.IdUtil;
import com.wei.springboot.starter.utils.RequestIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @createTime 2019/7/20 17:14
 * @description Pv日志打印
 */
@Slf4j
public class PvLogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 收到请求
        String requestId = IdUtil.simpleUUID();
        RequestIdUtils.add(requestId);
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String contentType = request.getHeader("Content-Type");
        log.info("[PV-{}] {}({}){}", requestId, method, contentType, requestURI);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 视图渲染前执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 视图渲染后执行
        // 移除防止OOM
        RequestIdUtils.remove();
    }
}
