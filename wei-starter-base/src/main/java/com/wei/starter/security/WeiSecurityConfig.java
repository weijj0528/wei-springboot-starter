package com.wei.starter.security;

import cn.hutool.core.text.StrPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * The type Wei security config.
 * 安全配置
 *
 * @author William.Wei
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnClass(WeiSecurityProperties.class)
public class WeiSecurityConfig {

    @Resource
    private WeiSecurityProperties weiSecurityProperties;

    public static final String ALL = "*";

    /**
     * Gets role prefix.
     * 获取角色前缀
     *
     * @return the role prefix
     */
    public String getRolePrefix() {
        return weiSecurityProperties.getRolePrefix();
    }

    /**
     * Token authentication filter wei token filter.
     *
     * @return the wei token filter
     */
    @Bean
    // @ConditionalOnProperty(value = "spring.security.enable", havingValue = "true")
    public WeiTokenFilter tokenAuthenticationFilter() {
        return new WeiTokenFilter();
    }

    /**
     * Expression handler default web security expression handler.
     *
     * @return the default web security expression handler
     */
    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setDefaultRolePrefix(weiSecurityProperties.getRolePrefix());
        return expressionHandler;
    }

    /**
     * Cors config web mvc configurer.
     *
     * @return the web mvc configurer
     */
    @Bean
    public WebMvcConfigurer corsConfig() {
        WeiSecurityProperties.Cors cors = weiSecurityProperties.getCors();
        String[] origins = cors.getOrigins();
        String pathPattern = cors.getPathPattern();
        String[] headers = cors.getHeaders();
        String[] methods = cors.getMethods();
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                AtomicBoolean allowCredentials = new AtomicBoolean(true);
                Optional.ofNullable(origins).map(Stream::of)
                        .map(s -> s.findFirst().orElse(ALL))
                        .filter(ALL::equals)
                        .ifPresent(s -> allowCredentials.set(false));
                registry.addMapping(pathPattern)
                        .allowedOrigins(origins)
                        .allowedHeaders(headers)
                        .allowedMethods(methods)
                        .maxAge(3600)
                        .allowCredentials(allowCredentials.get());
                log.info("addCorsMappings success: {} {} {} {} {}", pathPattern, origins, headers, methods, allowCredentials.get());
            }
        };
    }

    /**
     * Configure security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, WeiTokenFilter weiTokenFilter) throws Exception {
        boolean enable = weiSecurityProperties.isEnable();
        List<String> openApis = weiSecurityProperties.getOpenApis();
        log.info("SecurityConfig {}", enable);
        // CSRF关闭
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable()
                .and().headers().cacheControl().disable()
                .and().formLogin().disable()
                .logout().disable()
                .exceptionHandling().authenticationEntryPoint((req, resp, authException) -> {
                    resp.setStatus(401);
                    resp.setCharacterEncoding("UTF-8");
                    resp.setContentType("application/json; charset=utf-8");
                    resp.getWriter().write("{\"code\": 401, \"msg\": \"Authentication failed, please login again!\"}");
                    resp.getWriter().flush();
                });
        // 未开启权限检查
        if (!enable) {
            http.authorizeRequests().anyRequest().permitAll();
            return http.build();
        }
        // 默认开放的接口
        String[] antPatterns = {"/webjars/**", "/", "/index", "/docs", "/v2/**", "/swagger", "/swagger2", "/swagger-resources"};
        http.authorizeRequests().antMatchers(antPatterns).permitAll();
        // 按配置开放接口
        for (String api : openApis) {
            // uri /{xxx}/123 ->/**/123
            String replaceAll = api.replaceAll("\\{\\w+\\}", "**");
            String[] split = replaceAll.split(StrPool.COLON);
            if (split.length > 1) {
                HttpMethod httpMethod = HttpMethod.valueOf(split[0].toUpperCase());
                http.authorizeRequests().antMatchers(httpMethod, split[1]).permitAll();
            } else {
                http.authorizeRequests().antMatchers(split[0]).permitAll();
            }
            log.info("open api: {} {}", api, replaceAll);
        }
        // 其他接口开启认证
        http.authorizeRequests().expressionHandler(expressionHandler()).anyRequest().authenticated();
        // 添加过滤器
        http.addFilterBefore(weiTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
