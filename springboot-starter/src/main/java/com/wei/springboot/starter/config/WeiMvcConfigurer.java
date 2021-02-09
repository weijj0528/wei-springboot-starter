package com.wei.springboot.starter.config;

import com.wei.springboot.starter.intercepter.MdcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @createTime 2019/3/17 16:02
 * @description
 */
@Configuration
public class WeiMvcConfigurer implements WebMvcConfigurer {

    public static final String SUCCESS = "success.html";
    public static final String SWAGGER = "doc.html";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName(SUCCESS);
        registry.addViewController("/docs").setViewName(SWAGGER);
        registry.addViewController("/").setViewName("redirect:/index");
        registry.addViewController("/swagger").setViewName("redirect:/docs");
        registry.addViewController("/swagger2").setViewName("redirect:/docs");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MdcInterceptor());
    }

}
