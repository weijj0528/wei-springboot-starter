package com.wei.starter.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private Set<RequestMappingInfo> requestMappingInfos;

    private List<String> openApis;

    private Map<String, List<String>> openApiMap = new HashMap<>();

    public TokenFilter(List<String> openApis) {
        this.openApis = new ArrayList<>(openApis.size());
        for (String openApi : openApis) {
            String[] split = openApi.split(":");
            if (split.length > 1) {
                String url = split[1];
                this.openApis.add(url);
                List<String> methods = openApiMap.get(url);
                if (methods == null) {
                    methods = new ArrayList<>();
                    methods.add(split[0].toUpperCase());
                    openApiMap.put(url, methods);
                } else {
                    methods.add(split[0].toUpperCase());
                }
            } else {
                // 所有方法开放
                this.openApis.add(split[0]);
                ArrayList<String> methods = new ArrayList<>();
                methods.add("*");
                openApiMap.put(split[0], methods);
            }
        }
    }

    @PostConstruct
    public void init() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        requestMappingInfos = handlerMethods.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String token = request.getHeader("token");
            if (StringUtils.isNotEmpty(token) && requestMappingInfos != null && !requestMappingInfos.isEmpty()) {
                RequestMappingInfo match = null;
                for (RequestMappingInfo requestMappingInfo : requestMappingInfos) {
                    match = requestMappingInfo.getMatchingCondition(request);
                    if (match != null) {
                        break;
                    }
                }
                if (match != null) {
                    TokenInfo tokenInfo = tokenService.getToken(request, token);
                    // 找到匹配的接口签名与执行器
                    Set<RequestMethod> methods = match.getMethodsCondition().getMethods();
                    List<String> methodNames = methods.stream().map(RequestMethod::name).collect(Collectors.toList());
                    if (methodNames.isEmpty()) {
                        // 所有方法开放
                        methodNames.add("*");
                    }
                    Set<String> patterns = match.getPatternsCondition().getPatterns();
                    boolean hasPermission = false;
                    for (String pattern : patterns) {
                        // 是否为开放接口
                        hasPermission = openApis.contains(pattern);
                        if (hasPermission) {
                            List<String> openMethods = openApiMap.get(pattern);
                            // 所有请求方法匀开放
                            hasPermission = openMethods.contains("*");
                            if (hasPermission) {
                                break;
                            }
                            // 部分方法开放，如有并集则开放
                            Set<String> allMethods = new HashSet<>(openMethods);
                            allMethods.addAll(methodNames);
                            if (allMethods.size() < openMethods.size() + methodNames.size()) {
                                break;
                            }
                        }
                        for (String methodName : methodNames) {
                            hasPermission = tokenService.permissionCheck(tokenInfo, methodName, pattern);
                            log.info("UserPermissionCheck:[{}:{}] {} {}", methodName, pattern, hasPermission, token);
                            if (hasPermission) {
                                break;
                            }
                        }
                    }
                    // 添加权限信息，给到后续处理
                    if (hasPermission && tokenInfo != null) {
                        SecurityContextHolder.getContext().setAuthentication(tokenInfo);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Filter Error:", e);
        }
    }

}
