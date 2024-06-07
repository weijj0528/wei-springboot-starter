package com.github.weijj0528.example.security.security;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.StrPool;
import cn.hutool.json.JSONUtil;
import com.github.weijj0528.example.security.config.SecurityConfig;
import com.wei.starter.base.exception.ErrorMsgException;
import com.wei.starter.base.exception.UnauthorizedException;
import com.wei.starter.security.Principal;
import com.wei.starter.security.TokenService;
import com.wei.starter.security.WeiSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Set;

/**
 * JWT token service
 * 标准JWTToken 实现
 *
 * @author William.Wei
 */
@Slf4j
@Service
public class JwtTokenServiceImpl implements TokenService {

    @Resource
    private KeyPair keyPair;
    @Resource
    private SecurityConfig securityConfig;

    @Override
    public String saveToken(Principal principal, Long timeout) {
        String userInfo = JSONUtil.toJsonStr(principal);
        HashMap<String, Object> map = JSONUtil.toBean(userInfo, new TypeReference<HashMap<String, Object>>() {
        }, true);
        // 创建JWT
        JwtClaims claims = new JwtClaims();
        // 设置过期时间
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(1);
        claims.setExpirationTimeMinutesInTheFuture(60 * 24);
        claims.setGeneratedJwtId();
        // 设置自定义声明
        map.forEach(claims::setClaim);
        // 签名
        String token;
        try {
            JsonWebSignature jws = new JsonWebSignature();
            jws.setKey(keyPair.getPrivate());
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            token = jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new ErrorMsgException("JWT Token 生成异常");
        }
        return token;
    }

    @Override
    public void deleteToken(String token) {
        // JWT Token 无状态
        UserToken userToken = WeiSecurityUtil.getPrincipal();
        log.info("JWT Token 删除：{}", JSONUtil.toJsonStr(userToken));
    }

    @Override
    public Principal getToken(String token) {
        try {
            JsonWebSignature jws = new JsonWebSignature();
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            jws.setKey(keyPair.getPublic());
            jws.setCompactSerialization(token.substring(7));
            if (jws.verifySignature()) {
                // JWT签名验证成功，可以安全地使用jws.getPayload()获取声明
                String payload = jws.getPayload();
                return JSONUtil.toBean(payload, UserToken.class);
            } else {
                log.info("JWT Token 验证失败：{}", token);
            }
        } catch (JoseException e) {
            // 处理解码或验证过程中的异常
            log.info("JWT Token 验证异常：{}", token);
        }
        throw new UnauthorizedException();
    }

    @Override
    public boolean permissionCheck(Principal principal, String httpMethod, String pattern) {
        // TODO 按用户权限分配情况检查
        Set<String> set = securityConfig.getUserPermission().get(principal.getName());
        if (set != null) {
            return set.contains(httpMethod + StrPool.COLON + pattern) || set.contains(pattern);
        }
        return true;
    }
}
