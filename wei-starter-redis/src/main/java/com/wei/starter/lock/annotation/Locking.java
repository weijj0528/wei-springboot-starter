package com.wei.starter.lock.annotation;


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
public @interface Locking {


    /**
     * Value lock [ ].
     *
     * @return the lock [ ]
     */
    Lock[] value() default {};

}
