package com.wei.starter.security;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
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
@Order
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
            apiDataInit(split[1], split[0], openFixedApis, openMutableApis);
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
            String method = String.join(StrPool.COMMA, methods);
            PathPatternsRequestCondition pathCondition = mappingInfo.getPathPatternsCondition();
            if (pathCondition != null) {
                Set<PathPattern> patterns = pathCondition.getPatterns();
                for (PathPattern pattern : patterns) {
                    String patternString = pattern.getPatternString();
                    apiDataInit(patternString, method, fixedApis, mutableApis);
                }
            } else {
                PatternsRequestCondition patternsCondition = mappingInfo.getPatternsCondition();
                if (patternsCondition != null) {
                    for (String pattern : patternsCondition.getPatterns()) {
                        apiDataInit(pattern, method, fixedApis, mutableApis);
                    }
                }
            }

        }
    }

    private void apiDataInit(String uri, String method, Map<String, String> fixedApis, Map<String, String> mutableApis) {
        boolean open = fixedApis == openFixedApis;
        if (fixedApis.containsKey(uri) || mutableApis.containsKey(uri)) {
            String nm = fixedApis.containsKey(uri) ? fixedApis.get(uri) : mutableApis.get(uri);
            method = method + StrPool.COMMA + nm;
        }
        if (uri.contains(StrPool.DELIM_START)) {
            mutableApis.put(uri, method);
            log.debug("mutableApis put: {} {} {}", open, uri, method);
        } else {
            fixedApis.put(uri, method);
            log.debug("fixedApis put: {} {} {}", open, uri, method);
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
            log.info("open uriMatchPattern: {} {} {}", uri, method, pattern);
            if (StrUtil.isBlank(pattern)) {
                String token = request.getHeader(Header.AUTHORIZATION.toString());
                if (StringUtils.isNotEmpty(token)) {
                    // 非开放接口，验证用户权限
                    Principal principal = tokenService.getToken(token);
                    if (principal != null) {
                        pattern = uriMatchPattern(uri, method, fixedApis, mutableApis);
                        log.info("uriMatchPattern: {} {} {}", uri, method, pattern);
                        if (StrUtil.isNotBlank(pattern)) {
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
        Predicate<String> methodPredicate = s -> {
            log.debug("methodPredicate: {} {}", s, method);
            return FLAG_ALL_METHOD.equals(s) || s.toUpperCase().contains(method.toUpperCase());
        };
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
