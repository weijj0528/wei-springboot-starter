package com.wei.starter.base.util;

import cn.hutool.core.thread.ThreadUtil;
import com.wei.starter.base.bean.Code;
import com.wei.starter.base.exception.ErrorMsgException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * 通用工具类
 *
 * @author Weijj0528
 */
@Slf4j
public class WeiComUtils {

    private static final long BASE_BACKOFF_MS = 100L;

    /**
     * Operation retry t.
     * 操作重试（指数退避 + 抖动）
     *
     * @param <T>      the type parameter
     * @param supplier the supplier
     * @param key      the key
     * @param retry    当前重试次数（从 0 开始）
     * @param max      最大重试次数
     * @return the t
     */
    public static <T> T operationRetry(Supplier<T> supplier, String key, int retry, int max) {
        int attempt = retry;
        while (true) {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.error("operationRetry：{} {}", key, attempt, e);
                attempt++;
                if (attempt >= max) {
                    throw new ErrorMsgException(Code.SYSTEM_ERROR);
                }
                long backoff = BASE_BACKOFF_MS * attempt + ThreadLocalRandom.current().nextLong(50);
                ThreadUtil.sleep(backoff);
            }
        }
    }

}
