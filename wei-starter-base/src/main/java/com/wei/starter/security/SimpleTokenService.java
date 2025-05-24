package com.wei.starter.security;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Collections;

/**
 * The type Simple token service.
 *
 * @author William.Wei
 */
public class SimpleTokenService implements TokenService {

    private final Principal simplePrincipal = new Principal() {
        @Override
        public String getName() {
            return StrUtil.EMPTY;
        }

        @Override
        public String getTenant() {
            return StrUtil.EMPTY;
        }

        @Override
        public Collection<String> getAuthorities() {
            return Collections.emptyList();
        }
    };

    @Override
    public String saveToken(Principal principal, Long timeout) {
        return principal.getName();
    }

    @Override
    public void deleteToken(String token) {

    }

    @Override
    public Principal getToken(String token) {
        return simplePrincipal;
    }

    @Override
    public boolean permissionCheck(Principal principal, String httpMethod, String pattern) {
        return true;
    }
}
