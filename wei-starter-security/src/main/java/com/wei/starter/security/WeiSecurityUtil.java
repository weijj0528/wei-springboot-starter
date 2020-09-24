package com.wei.starter.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class WeiSecurityUtil {

    public static TokenInfo getToken() {
        if (SecurityContextHolder.getContext() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication() == null ? null : (TokenInfo) SecurityContextHolder.getContext().getAuthentication();
    }

    public static <T> T getPrincipal() {
        return (T) getToken().getPrincipal();
    }
}
