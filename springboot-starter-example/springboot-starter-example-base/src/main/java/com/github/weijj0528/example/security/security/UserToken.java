package com.github.weijj0528.example.security.security;

import com.wei.starter.security.Principal;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * 用户token
 *
 * @author William.Wei
 */
@Data
public class UserToken implements Principal {

    private String name;

    private List<String> authorities;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<String> getAuthorities() {
        return authorities;
    }
}
