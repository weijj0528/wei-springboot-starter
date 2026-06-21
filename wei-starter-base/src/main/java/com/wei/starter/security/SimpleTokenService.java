package com.wei.starter.security;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Collections;

/**
 * The type Simple token service.
 * <p>
 * ⚠️ 警告 / WARNING：此为不安全的简化实现，仅供本地开发与调试使用！
 * <ul>
 *   <li>getToken() 无论传入何种 token，均返回相同的空 Principal，无法区分用户身份。</li>
 *   <li>saveToken() 直接以用户名作为 token，没有任何加密或签名，极易被伪造。</li>
 *   <li>permissionCheck() 始终返回 true，所有权限校验直接放行。</li>
 * </ul>
 * 生产环境【必须】提供自定义 {@link TokenService} Bean 以覆盖此默认实现，
 * 否则将导致严重的安全漏洞！
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
