package com.github.weijj0528.example.base.security;

import com.wei.starter.security.Principal;

import java.util.Collection;

public class UserToken implements Principal {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<String> getAuthorities() {
        return null;
    }
}
