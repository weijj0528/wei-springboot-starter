package com.wei.starter.cache;

import java.util.ArrayList;
import java.util.Optional;

public class CacheTtlContext {

    /**
     * 考虑可能有嵌套情况 使用 ArrayList<Long> 存放TTL
     */
    private static final ThreadLocal<ArrayList<Long>> TTL = new ThreadLocal<>();

    /**
     * 设置当前线程的TTL
     *
     * @param ttl ttl second
     */
    public static void setTtl(long ttl) {
        ArrayList<Long> list = Optional.ofNullable(TTL.get()).orElse(new ArrayList<>(2));
        list.add(ttl);
        TTL.set(list);
    }

    /**
     * 重置当前线程的TTL
     *
     * @param ttl ttl second
     */
    public static void resetTtl(long ttl) {
        ArrayList<Long> list = TTL.get();
        if (list != null && list.size() > 0) {
            list.set(list.size() - 1, ttl);
        }
    }

    /**
     * 获取当前线程的TTL
     *
     * @return ttl second
     */
    public static long getTtl() {
        return Optional.ofNullable(TTL.get()).map(l -> l.size() > 0 ? l.get(l.size() - 1) : 0L).orElse(0L);
    }

    /**
     * 移除当前线程的TTL（生存时间）。
     * 该方法用于在线程执行特定操作后，取消其原有的TTL限制，使其不再受到之前设置的TTL的约束。
     * 这对于需要动态调整线程生存时间的场景非常有用，比如在某些紧急情况下需要立即终止线程的执行。
     */
    public static void remove() {
        // 获取当前线程关联的TTL列表
        ArrayList<Long> list = TTL.get();
        // 如果列表存在且不为空，则移除最后一个元素，即最近设置的TTL
        if (list != null && list.size() > 0) {
            list.remove(list.size() - 1);
        }
        // 如果列表为空，说明已经没有需要维护的TTL，因此移除这个列表
        if (list != null && list.isEmpty()) {
            TTL.remove();
        }
    }
}
