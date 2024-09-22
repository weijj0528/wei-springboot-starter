package com.wei.starter.security;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Wei token filter.
 *
 * @author William.Wei
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "spring.security.enable", havingValue = "true")
public class WeiTokenFilter extends OncePerRequestFilter {

    @Resource
    private TokenService tokenService;
    @Resource
    private WeiSecurityProperties weiSecurityProperties;
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Map<String, String> openFixedApis = new HashMap<>();
    private final Map<String, String> openMutableApis = new HashMap<>();
    private final Map<String, String> fixedApis = new HashMap<>();
    private final Map<String, String> mutableApis = new HashMap<>();

    private final static String FLAG_ALL_METHOD = "*";

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        for (String openApi : weiSecurityProperties.getOpenApis()) {
            if (!openApi.contains(StrPool.COLON)) {
                openApi = String.format("%s:%s", FLAG_ALL_METHOD, openApi);
            }
            String[] split = openApi.split(StrPool.COLON);
            if (openApi.contains(StrPool.DELIM_START)) {
                this.openMutableApis.put(split[0], split[1]);
            } else {
                this.openFixedApis.put(split[0], split[1]);
            }
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> requestMappingInfos = handlerMethods.keySet();
        for (RequestMappingInfo mappingInfo : requestMappingInfos) {
            RequestMethodsRequestCondition methodsCondition = mappingInfo.getMethodsCondition();
            List<String> methods = Optional.of(methodsCondition)
                    .map(RequestMethodsRequestCondition::getMethods)
                    .map(m -> m.stream().map(RequestMethod::name).collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            if (methods.isEmpty()) {
                methods.add(FLAG_ALL_METHOD);
            }
            PathPatternsRequestCondition pathCondition = mappingInfo.getPathPatternsCondition();
            if (pathCondition != null) {
                Set<PathPattern> patterns = pathCondition.getPatterns();
                for (PathPattern pattern : patterns) {
                    String patternString = pattern.getPatternString();
                    String method = methods.stream().collect(Collectors.joining(StrPool.COMMA));
                    if (patternString.contains(StrPool.DELIM_START)) {
                        mutableApis.put(patternString, method);
                    } else {
                        fixedApis.put(patternString, method);
                    }
                }
            }
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        try {
            // 是否为开放接口
            String uri = request.getRequestURI();
            String method = request.getMethod();
            String pattern = uriMatchPattern(uri, method, openFixedApis, openMutableApis);
            if (StrUtil.isBlank(pattern)) {
                String token = request.getHeader(Header.AUTHORIZATION.toString());
                if (StringUtils.isNotEmpty(token)) {
                    // 非开放接口，验证用户权限
                    Principal principal = tokenService.getToken(token);
                    if (principal != null) {
                        uriMatchPattern(uri, method, fixedApis, mutableApis);
                        if (pattern != null) {
                            boolean hasPermission = tokenService.permissionCheck(principal, method, pattern);
                            log.info("UserPermissionCheck:[{}:{}] {} {}", method, pattern, hasPermission, token);
                            // 添加权限信息，给到后续处理
                            if (hasPermission) {
                                SecurityContextHolder.getContext().setAuthentication(new WeiToken(token, principal));
                            }
                        }
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Filter Error:", e);
        }
    }

    private String uriMatchPattern(String uri, String method, Map<String, String> fixedApis, Map<String, String> mutableApis) {
        // 固定方法匹配
        Predicate<String> methodPredicate = s -> FLAG_ALL_METHOD.equals(s) || s.contains(method);
        String exit = Optional.ofNullable(fixedApis.get(uri)).filter(methodPredicate).orElse(StrUtil.EMPTY);
        if (StrUtil.isNotBlank(exit)) {
            return uri;
        }
        for (String api : mutableApis.keySet()) {
            if (antPathMatcher.match(api, uri) && methodPredicate.test(mutableApis.get(api))) {
                return api;
            }
        }
        return StrUtil.EMPTY;
    }

}
