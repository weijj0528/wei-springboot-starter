package com.wei.springboot.starter.utils;

/**
 * @author Administrator
 * @createTime 2019/7/21 9:45
 * @description request id 工具类，使用ThreadLocal存储
 */
public class RequestIdUtils {

    private static final ThreadLocal<String> requestIdLocal = new ThreadLocal<>();

    public static void add(String requestId) {
        requestIdLocal.set(requestId);
    }

    public static String get() {
        return requestIdLocal.get();
    }

    public static void remove() {
        requestIdLocal.remove();
    }

}
