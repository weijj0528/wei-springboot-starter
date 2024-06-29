package com.wei.starter.security;

import cn.hutool.core.text.StrPool;
import cn.hutool.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Wei token filter.
 *
 * @author William.Wei
 */
@Slf4j
public class WeiTokenFilter extends OncePerRequestFilter {

    @Resource
    private TokenService tokenService;
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final List<String> openApis;
    private final Set<String> fixedApis = new HashSet<>();
    private final Set<String> mutableApis = new HashSet<>();

    private final static String FLAG_ALL_METHOD = "*";

    /**
     * Instantiates a new Wei token filter.
     *
     * @param openApis the open apis
     */
    public WeiTokenFilter(List<String> openApis) {
        this.openApis = new ArrayList<>(openApis.size());
        for (String openApi : openApis) {
            if (openApi.contains(StrPool.COLON)) {
                this.openApis.add(openApi);
            } else {
                // 所有方法开放
                String ura = String.format("%s:%s", FLAG_ALL_METHOD, openApi);
                this.openApis.add(ura);
            }
        }
    }

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
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
                    Stream<String> stream = methods.stream().map(m -> String.format("%s:%s", m, patternString));
                    if (patternString.contains(StrPool.DELIM_START)) {
                        stream.forEach(mutableApis::add);
                    } else {
                        stream.forEach(fixedApis::add);
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
            String url = String.format("%s:%s", method, uri);
            String ura = String.format("%s:%s", FLAG_ALL_METHOD, uri);
            boolean isOpenApi = openApis.contains(uri);
            isOpenApi = isOpenApi || openApis.contains(ura);
            if (!isOpenApi) {
                String token = request.getHeader(Header.AUTHORIZATION.toString());
                if (StringUtils.isNotEmpty(token)) {
                    // 非开放接口，验证用户权限
                    boolean hasPermission = false;
                    Principal principal = tokenService.getToken(token);
                    if (principal != null) {
                        boolean isFixedApi = fixedApis.contains(url);
                        isFixedApi = isFixedApi || fixedApis.contains(ura);
                        String pattern = null;
                        if (isFixedApi) {
                            pattern = uri;
                        } else {
                            for (String api : mutableApis) {
                                String patternTemp = api.substring(api.indexOf(StrPool.COLON) + 1);
                                if (antPathMatcher.match(patternTemp, uri)) {
                                    pattern = patternTemp;
                                    break;
                                }
                            }
                        }
                        if (pattern != null) {
                            hasPermission = tokenService.permissionCheck(principal, method, pattern);
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

}
