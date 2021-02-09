package com.wei.springbootstarterexample.security;

import com.wei.starter.security.Principal;
import com.wei.starter.security.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {


    @Override
    public void saveToken(String token, Principal principal, Long timeout) {

    }

    @Override
    public void deleteToken(String token) {

    }

    @Override
    public Principal getToken(String token) {
        return new UserToken();
    }

    @Override
    public boolean permissionCheck(Principal principal, String httpMethod, String pattern) {
        return false;
    }
}
