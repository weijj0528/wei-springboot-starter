package com.wei.starter.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author William.Wei
 */
public class WeiToken implements Authentication {


    private String token;

    private boolean authenticated = true;

    private List<SimpleGrantedAuthority> authorities;

    /**
     * 主要信息由具体应用定义
     */
    private Principal principal;

    public WeiToken() {
    }

    public WeiToken(String token, Principal principal) {
        this.token = token;
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null && principal != null && principal.getAuthorities() != null) {
            authorities = principal.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return this;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal.getName();
    }
}
