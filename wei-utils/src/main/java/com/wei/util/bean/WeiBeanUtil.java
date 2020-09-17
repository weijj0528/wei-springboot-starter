package com.wei.util.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

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
        T target = null;
        try {
            target = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtil.copyProperties(source, target);
        return target;

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
        T target = null;
        try {
            target = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtil.copyProperties(source, target, ignoreProperties);
        return target;

    }

    /**
     * To list list.
     *
     * @param <T>     the type parameter
     * @param sources the sources
     * @param clazz   the clazz
     * @return the list
     */
    public static <T> List<T> toList(Collection<?> sources, Class<T> clazz) {
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
     * To list list.
     *
     * @param <T>     the type parameter
     * @param sources the sources
     * @param clazz   the clazz
     * @return the list
     */
    public static <T> List<T> toList(Collection<?> sources, Class<T> clazz, String... ignoreProperties) {
        if (CollectionUtil.isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> results = new ArrayList<>(sources.size());
        for (Object source : sources) {
            T t = toBean(source, clazz, ignoreProperties);
            results.add(t);
        }
        return results;
    }

    /**
     * <p>获取到对象中属性为null的属性名  </P>
     *
     * @param source 要拷贝的对象
     * @return 属性为null的属性名列表
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
