package com.wei.starter.security;

/**
 * The interface Token service.
 *
 * @author Weijj0528
 */
public interface TokenService {

    /**
     * Create token token.
     *
     * @param principal the principal
     * @param timeout   the timeout
     * @return the string
     */
    String saveToken(Principal principal, Long timeout);

    /**
     * Delete token principal.
     *
     * @param token the token
     */
    void deleteToken(String token);

    /**
     * Gets token.
     *
     * @param token the token
     * @return the token
     */
    Principal getToken(String token);

    /**
     * Permission check boolean.
     *
     * @param principal  the principal
     * @param httpMethod the http method
     * @param pattern    the pattern
     * @return the boolean
     */
    boolean permissionCheck(Principal principal, String httpMethod, String pattern);
}
