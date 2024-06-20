package com.wei.starter.cache;

import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * The type Cache key generator.
 * 缓存 KEY 生成策略
 * 如没有配置缓存KEY 则使用该策略生成
 * eg. [Prefix:]ClassSimpleName:methodName:{paramsHash}
 *
 * @author William
 */
@Slf4j
public class WeiKeyGenerator implements KeyGenerator {
    /**
     * The constant NO_PARAM_KEY.
     */
    public static final int NO_PARAM_KEY = 0;
    /**
     * The constant NULL_PARAM_KEY.
     */
    public static final int NULL_PARAM_KEY = 99;

    /**
     * 缓存 KEY 生成策略
     * 如没有配置缓存KEY 则使用该策略生成
     * eg. [Prefix:]SimpleName:methodName:{paramsHash}
     *
     * @param target the target object
     * @param method the method
     * @param params the params
     * @return
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName())
                .append(StrUtil.COLON).append(method.getName())
                .append(StrUtil.COLON);
        StringBuilder args = new StringBuilder();
        for (Object param : params) {
            if (param == null) {
                log.warn("input null param for Spring cache, use default key={}", NULL_PARAM_KEY);
                args.append(NULL_PARAM_KEY);
            } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                int length = Array.getLength(param);
                for (int i = 0; i < length; i++) {
                    args.append(Array.get(param, i));
                    args.append(StrUtil.COMMA);
                }
            } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                args.append(param);
            } else {
                log.warn("Using an object as a cache key may lead to unexpected results. " +
                        "Either use @Cacheable(key=..) or implement CacheKey. Method is " + target.getClass() + "#" + method.getName());
                args.append(param.hashCode());
            }
            args.append(StrUtil.DASHED);
        }
        if (args.length() == 0) {
            args.append(NO_PARAM_KEY);
        }
        String argsKey = args.toString();
        long argsKeyHash = HashUtil.mixHash(argsKey);
        key.append(argsKeyHash);
        String kkk = key.toString();
        log.debug("using cache Key={} argsKey={}", kkk, argsKey);
        return kkk;
    }
}
