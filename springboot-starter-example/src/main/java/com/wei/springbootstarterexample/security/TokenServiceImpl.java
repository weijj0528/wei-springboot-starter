package com.wei.springbootstarterexample.security;

import com.wei.starter.security.TokenInfo;
import com.wei.starter.security.TokenService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public TokenInfo createToken(HttpServletRequest request, String token) {
        return null;
    }

    @Override
    public TokenInfo getToken(HttpServletRequest request, String token) {
        return new TokenInfo(request);
    }

    @Override
    public boolean permissionCheck(TokenInfo tokenInfo, String httpMethod, String pattern) {
        return false;
    }
}
