package com.wei.starter.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 基础安全工具，获取授权信息
 *
 * @author Weijj0528
 */
@Slf4j
public class WeiSecurityUtil {

    /**
     * Gets token.
     *
     * @return the token
     */
    public static WeiToken getToken() {
        if (SecurityContextHolder.getContext() == null) {
            return null;
        }
        Authentication token = SecurityContextHolder.getContext().getAuthentication();
        if (!(token instanceof WeiToken)) {
            return null;
        }
        return (WeiToken) token;
    }

    /**
     * Gets principal.
     *
     * @param <T> the type parameter
     * @return the principal
     */
    public static <T> T getPrincipal() {
        WeiToken token = getToken();
        if (token == null) {
            return null;
        }
        return (T) token.getPrincipal();
    }
}
