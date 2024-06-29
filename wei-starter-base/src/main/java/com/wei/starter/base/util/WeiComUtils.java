package com.wei.starter.base.util;

import cn.hutool.core.thread.ThreadUtil;
import com.wei.starter.base.bean.Code;
import com.wei.starter.base.exception.ErrorMsgException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 通用工具类
 *
 * @author Weijj0528
 */
@Slf4j
public class WeiComUtils {


    /**
     * Operation retry t.
     * 操作重试
     *
     * @param <T>      the type parameter
     * @param supplier the supplier
     * @param key      the key
     * @param retry    the retry
     * @param max      the max
     * @return the t
     */
    public <T> T operationRetry(Supplier<T> supplier, String key, int retry, int max) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("operationRetry：{} {}", key, retry, e);
            retry++;
            if (retry >= max) {
                throw new ErrorMsgException(Code.SYSTEM_ERROR);
            }
            ThreadUtil.sleep(100);
            return operationRetry(supplier, key, retry, max);
        }
    }

}
