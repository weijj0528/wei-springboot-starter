package com.wei.starter.base.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;

/**
 * The type Spring context.
 *
 * @author William
 */
@Configuration
public class SpringContext implements ApplicationContextAware {


    private static ApplicationContext context = null;

    /**
     * Instantiates a new Spring context.
     */
    public SpringContext() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Gets bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * Gets bean.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the bean
     * @throws BeansException the beans exception
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return context.getBean(clazz);
    }

    /**
     * Publish event.
     *
     * @param event the event
     */
    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }

}
