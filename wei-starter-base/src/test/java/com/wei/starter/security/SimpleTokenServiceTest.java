package com.wei.starter.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SimpleTokenService 单元测试
 * <p>
 * 覆盖 HMAC-SHA256 无状态 Token 方案：签名/验证、fail-closed、篡改检测、过期校验。
 */
@DisplayName("SimpleTokenService")
class SimpleTokenServiceTest {

    private static final String SECRET = "test-secret-key-for-hmac-signing";

    private WeiSecurityProperties properties;
    private SimpleTokenService service;

    @BeforeEach
    void setUp() {
        properties = new WeiSecurityProperties();
        properties.setTokenSecret(SECRET);
        service = new SimpleTokenService(properties);
    }

    private Principal principal(String name) {
        return new Principal() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getTenant() {
                return "";
            }

            @Override
            public Collection<String> getAuthorities() {
                return Collections.emptyList();
            }
        };
    }

    @Nested
    @DisplayName("getToken")
    class GetToken {

        @Test
        @DisplayName("returns null when tokenSecret is blank (fail-closed)")
        void getToken_returnsNull_whenSecretBlank() {
            properties.setTokenSecret("");
            SimpleTokenService blankService = new SimpleTokenService(properties);

            assertNull(blankService.getToken("any.token"));
        }

        @Test
        @DisplayName("saveToken then getToken round-trips a valid Principal")
        void saveToken_thenGetToken_roundTripsPrincipal() {
            String token = service.saveToken(principal("alice"), 3600L);
            assertNotNull(token);

            Principal parsed = service.getToken(token);
            assertNotNull(parsed);
            assertEquals("alice", parsed.getName());
        }

        @Test
        @DisplayName("returns null for tampered signature (one char changed)")
        void getToken_returnsNull_whenSignatureTampered() {
            String token = service.saveToken(principal("alice"), 3600L);
            assertNotNull(token);

            String[] parts = token.split("\\.", 2);
            char[] sigChars = parts[1].toCharArray();
            sigChars[0] = sigChars[0] == 'A' ? 'B' : 'A';
            String tamperedToken = parts[0] + "." + new String(sigChars);

            assertNull(service.getToken(tamperedToken));
        }

        @Test
        @DisplayName("returns null for expired token (timeout=0 means expiry=now)")
        void getToken_returnsNull_whenTokenExpired() {
            String token = service.saveToken(principal("alice"), 0L);
            assertNotNull(token);

            assertNull(service.getToken(token));
        }

        @Test
        @DisplayName("returns null for token without delimiter")
        void getToken_returnsNull_whenNoDelimiter() {
            assertNull(service.getToken("no-delimiter-here"));
        }

        @Test
        @DisplayName("returns null for blank token")
        void getToken_returnsNull_whenTokenBlank() {
            assertNull(service.getToken(""));
            assertNull(service.getToken(null));
        }
    }

    @Nested
    @DisplayName("deleteToken")
    class DeleteToken {

        @Test
        @DisplayName("throws UnsupportedOperationException (stateless implementation)")
        void deleteToken_throwsUnsupportedOperationException() {
            assertThrows(UnsupportedOperationException.class, () -> service.deleteToken("any"));
        }
    }

    @Nested
    @DisplayName("permissionCheck")
    class PermissionCheck {

        @Test
        @DisplayName("returns false for null principal")
        void permissionCheck_returnsFalse_whenPrincipalNull() {
            assertFalse(service.permissionCheck(null, "GET", "/api/test"));
        }

        @Test
        @DisplayName("returns true for non-null principal")
        void permissionCheck_returnsTrue_whenPrincipalNotNull() {
            assertTrue(service.permissionCheck(principal("alice"), "GET", "/api/test"));
        }
    }
}
