package com.wei.util.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The type Wei util.
 */
public class WeiBeanUtil {

    /**
     * To bean t.
     *
     * @param <T>    the type parameter
     * @param source the source
     * @param cls    the cls
     * @return the t
     */
    public static <T> T toBean(Object source, Class<T> cls) {
        Object target = null;
        try {
            target = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtil.copyProperties(source, target);
        return (T) target;

    }

    /**
     * To bean t.
     *
     * @param <T>              the type parameter
     * @param source           the source
     * @param cls              the cls
     * @param ignoreProperties the ignore properties
     * @return the t
     */
    public static <T> T toBean(Object source, Class<T> cls, String... ignoreProperties) {
        Object target = null;
        try {
            target = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtil.copyProperties(source, target, ignoreProperties);
        return (T) target;

    }

    /**
     * To list list.
     *
     * @param <T>     the type parameter
     * @param sources the sources
     * @param clazz   the clazz
     * @return the list
     */
    public static <T> List<T> toList(Collection sources, Class<T> clazz) {
        if (CollectionUtil.isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> results = new ArrayList<>(sources.size());
        for (Object source : sources) {
            T t = toBean(source, clazz);
            results.add(t);
        }
        return results;
    }

    /**
     * Gets client ip.
     *
     * @param request the request
     * @return the client ip
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
