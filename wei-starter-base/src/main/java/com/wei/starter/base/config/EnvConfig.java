package com.wei.starter.base.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * The type Env config.
 * 环境
 *
 * @author Weijj0528
 */
@Configuration
public class EnvConfig implements ApplicationContextAware {

    /**
     * 开发环境定义
     */
    public static final String DEV = "dev";
    /**
     * 测试环境定义
     */
    public static final String TEST = "test";
    /**
     * 预发环境定义
     */
    public static final String UAT = "uat";
    /**
     * 生产环境定义
     */
    public static final String PROD = "prod";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets active profile.
     *
     * @return the active profile
     */
    public String getActiveProfile() {
        Environment env = applicationContext.getEnvironment();
        return Optional.of(env.getActiveProfiles())
                .filter(p -> p.length > 0).map(p -> p[0])
                .orElse(StrUtil.EMPTY);
    }

    /**
     * Env check boolean.
     *
     * @param env the env
     * @return the boolean
     */
    public boolean envCheck(String env) {
        return getActiveProfile().equals(env);
    }

    /**
     * Not prod boolean.
     * 非生产环境
     *
     * @return the boolean
     */
    public boolean notProd() {
        return !envCheck(PROD);
    }


}
