package com.wei.starter.lock.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * The interface Lock.
 *
 * @author William.Wei
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lock {

    /**
     * Value string [ ].
     *
     * @return the string [ ]
     */
    @AliasFor("lockName")
    String value() default "";


    /**
     * Cache names string [ ].
     *
     * @return the string [ ]
     */
    @AliasFor("value")
    String lockName() default "";


    /**
     * Key string.
     *
     * @return the string
     */
    String key() default "";

    /**
     * Expired time long.
     * 过期时间，默认10秒
     *
     * @return long long
     */
    long expiredTime() default 10;

    /**
     * Wait time long.
     * 等待时间，默认0
     *
     * @return the long
     */
    long waitTime() default 0;

}
