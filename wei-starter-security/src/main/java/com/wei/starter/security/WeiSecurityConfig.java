package com.wei.starter.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

/**
 * The type Wei security config.
 * 安全配置
 *
 * @author William.Wei
 */
@Slf4j
@Configuration
public class WeiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.enable:false}")
    private boolean enable;
    @Value("${spring.security.open-apis:}")
    private List<String> openApis;
    @Value("${spring.security.cors.path-pattern:/**}")
    private String pathPattern;
    @Value("${spring.security.cors.path-pattern:*}")
    private String[] origins;
    @Value("${spring.security.cors.path-pattern:*}")
    private String[] headers;
    @Value("${spring.security.cors.path-pattern:*}")
    private String[] methods;

    /**
     * Token authentication filter wei token filter.
     *
     * @return the wei token filter
     */
    @Bean
    @ConditionalOnProperty(value = "spring.security.enable", havingValue = "true")
    public WeiTokenFilter tokenAuthenticationFilter() {
        return new WeiTokenFilter(openApis);
    }

    @Bean
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(pathPattern)
                        .allowedOrigins(origins)
                        .allowedHeaders(headers)
                        .allowedMethods(methods)
                        .maxAge(3600)
                        .allowCredentials(true);
                log.info("addCorsMappings success!");
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("SecurityConfig {}", enable);
        // CSRF关闭
        http.csrf().disable().cors();
        if (!enable) {
            http.authorizeRequests().anyRequest().permitAll();
            return;
        }
        http.formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, authException) -> {
                    resp.setStatus(401);
                    resp.setCharacterEncoding("UTF-8");
                    resp.setContentType("application/json; charset=utf-8");
                    resp.getWriter().write("{\"code\": 401, \"msg\": \"Authentication failed, please login again!\"}");
                    resp.getWriter().flush();
                }).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable()
                .and().headers().cacheControl().disable();
        // 默认开放的接口
        String[] antPatterns = {"/webjars/**", "/", "/index", "/docs", "/v2/**", "/swagger", "/swagger2", "/swagger-resources"};
        http.authorizeRequests().antMatchers(antPatterns).permitAll();
        // 按配置开放接口
        for (String api : openApis) {
            String[] split = api.split(":");
            if (split.length > 1) {
                HttpMethod httpMethod = HttpMethod.valueOf(split[0].toUpperCase());
                http.authorizeRequests().antMatchers(httpMethod, split[1]).permitAll();
            } else {
                http.authorizeRequests().antMatchers(split[0]).permitAll();
            }
        }
        // 其他接口开启认证
        http.authorizeRequests().anyRequest().authenticated();
        // 添加过滤器
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
