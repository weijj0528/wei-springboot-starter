package com.wei.starter.security;

import javax.servlet.http.HttpServletRequest;

/**
 * The interface Token service.
 */
public interface TokenService {

    /**
     * Create token token.
     *
     * @param request the request
     * @param token   the token
     * @return the token
     */
    TokenInfo createToken(HttpServletRequest request, String token);

    /**
     * Gets token.
     *
     * @param request
     * @param token   the token
     * @return the token
     */
    TokenInfo getToken(HttpServletRequest request, String token);

    /**
     * Permission check boolean.
     *
     * @param tokenInfo  the token info
     * @param httpMethod the http method
     * @param pattern    the pattern
     * @return the boolean
     */
    boolean permissionCheck(TokenInfo tokenInfo, String httpMethod, String pattern);
}
