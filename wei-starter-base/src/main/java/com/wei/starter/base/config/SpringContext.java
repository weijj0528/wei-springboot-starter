package com.wei.starter.base.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;

/**
 * @author William
 */
@Configuration
public class SpringContext implements ApplicationContextAware {


    private static ApplicationContext context = null;

    public SpringContext() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return context.getBean(clazz);
    }

    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }

}
