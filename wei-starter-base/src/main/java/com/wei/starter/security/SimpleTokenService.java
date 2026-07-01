package com.wei.starter.security;

import cn.hutool.core.util.StrUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

/**
 * 简化但安全的默认 Token 服务（开发/调试默认实现）。
 * <p>
 * 采用无状态 HMAC-SHA256 签名方案：
 * <ul>
 *   <li>Token 格式：base64url(payloadJson) + "." + base64url(hmacSha256(payloadJson, secret))</li>
 *   <li>payload 携带 principal name + 过期时间（epoch millis）</li>
 *   <li>验证时使用常量时间比较 HMAC，校验签名与过期时间</li>
 *   <li>未配置 tokenSecret 时 fail-closed：getToken 返回 null，并仅在初始化时告警一次</li>
 *   <li>无状态实现无法撤销已签发的 Token，deleteToken 将抛出 {@link UnsupportedOperationException}</li>
 * </ul>
 * 生产环境建议提供自定义 {@link TokenService} Bean 以覆盖此默认实现。
 * 配置项：{@code spring.security.custom.token-secret}
 *
 * @author William.Wei
 */
public class SimpleTokenService implements TokenService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_DELIMITER = ".";
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private final WeiSecurityProperties properties;
    private volatile boolean secretWarned = false;

    public SimpleTokenService(WeiSecurityProperties properties) {
        this.properties = properties;
        if (StrUtil.isBlank(properties.getTokenSecret())) {
            logWarnOnce();
        }
    }

    private void logWarnOnce() {
        if (!secretWarned) {
            synchronized (this) {
                if (!secretWarned) {
                    secretWarned = true;
                    System.err.println("[SimpleTokenService] token-secret is not configured. "
                            + "getToken() will return null (fail-closed). "
                            + "Set 'spring.security.custom.token-secret' to enable token validation.");
                }
            }
        }
    }

    @Override
    public String saveToken(Principal principal, Long timeout) {
        String secret = properties.getTokenSecret();
        if (StrUtil.isBlank(secret)) {
            logWarnOnce();
            return null;
        }
        long expiry = System.currentTimeMillis() + (timeout != null ? timeout * 1000L : 0L);
        // exp 以字符串形式存储，与 extractJsonField 的带引号解析保持一致
        String payloadJson = String.format("{\"name\":\"%s\",\"exp\":\"%d\"}", escapeJson(principal.getName()), expiry);
        String payloadB64 = URL_ENCODER.encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String sig = hmacSha256(payloadJson, secret);
        return payloadB64 + TOKEN_DELIMITER + sig;
    }

    @Override
    public void deleteToken(String token) {
        throw new UnsupportedOperationException(
                "SimpleTokenService is stateless (HMAC-signed) and cannot revoke tokens. "
                        + "Provide a custom TokenService Bean for revocation support.");
    }

    @Override
    public Principal getToken(String token) {
        String secret = properties.getTokenSecret();
        if (StrUtil.isBlank(secret)) {
            logWarnOnce();
            return null;
        }
        if (StrUtil.isBlank(token) || !token.contains(TOKEN_DELIMITER)) {
            return null;
        }
        String[] parts = token.split("\\.", 2);
        if (parts.length != 2) {
            return null;
        }
        String payloadB64 = parts[0];
        String sig = parts[1];
        String payloadJson;
        try {
            payloadJson = new String(URL_DECODER.decode(payloadB64), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null;
        }
        String expectedSig = hmacSha256(payloadJson, secret);
        if (!constantTimeEquals(sig, expectedSig)) {
            return null;
        }
        String name = extractJsonField(payloadJson, "name");
        String expStr = extractJsonField(payloadJson, "exp");
        if (name == null || expStr == null) {
            return null;
        }
        long expiry;
        try {
            expiry = Long.parseLong(expStr);
        } catch (NumberFormatException e) {
            return null;
        }
        if (System.currentTimeMillis() >= expiry) {
            return null;
        }
        final String principalName = name;
        return new Principal() {
            @Override
            public String getName() {
                return principalName;
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
    }

    @Override
    public boolean permissionCheck(Principal principal, String httpMethod, String pattern) {
        return principal != null;
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            byte[] hmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return URL_ENCODER.encodeToString(hmac);
        } catch (Exception e) {
            throw new IllegalStateException("HMAC-SHA256 computation failed", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    private String extractJsonField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key);
        if (start < 0) {
            return null;
        }
        start += key.length();
        int end = json.indexOf("\"", start);
        if (end < 0) {
            return null;
        }
        return json.substring(start, end);
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
