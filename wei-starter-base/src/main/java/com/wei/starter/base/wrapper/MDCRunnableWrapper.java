package com.wei.starter.base.wrapper;

import org.slf4j.MDC;

import java.util.Map;

/**
 * The type Mdc runnable wrapper.
 * Runnable Mdc 扩展
 * @author William.Wei
 */
public class MDCRunnableWrapper implements Runnable {

    /**
     * 包装的目标对象
     */
    private final Runnable runnable;

    /**
     * 保存MDC中的值
     */
    private final Map<String, String> map;

    /**
     * Instantiates a new Mdc runnable wrapper.
     *
     * @param runnable the runnable
     */
    public MDCRunnableWrapper(Runnable runnable) {
        this.runnable = runnable;
        this.map = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        // 传入已保存的MDC值
        for (Map.Entry<String, String> entry : map.entrySet()) {
            MDC.put(entry.getKey(), entry.getValue());
        }
        runnable.run();
        // 移除已保存的MDC值
        MDC.clear();
    }
}
