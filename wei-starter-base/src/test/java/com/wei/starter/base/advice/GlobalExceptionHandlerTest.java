package com.wei.starter.base.advice;

import com.wei.starter.base.bean.Code;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.exception.ErrorMsgException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * GlobalExceptionHandler 单元测试
 * <p>
 * 覆盖 401/403/500 状态码映射、AccessDeniedException 修复、getStackTrace 空栈守卫。
 */
@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("unauthorizedExceptionHandle")
    class UnauthorizedExceptionHandle {

        @Test
        @DisplayName("returns 401 code for UnauthorizedException")
        void unauthorizedExceptionHandle_returnsUnauthorizedCode() {
            Result<Void> result = handler.unauthorizedExceptionHandle();

            assertEquals(Code.UNAUTHORIZED.getCode(), result.getCode());
        }
    }

    @Nested
    @DisplayName("forbiddenExceptionHandle")
    class ForbiddenExceptionHandle {

        @Test
        @DisplayName("returns 403 code")
        void forbiddenExceptionHandle_returnsForbiddenCode() {
            Result<Void> result = handler.forbiddenExceptionHandle();

            assertEquals(Code.FORBIDDEN.getCode(), result.getCode());
        }

        @Test
        @DisplayName("handles AccessDeniedException (fix: added to @ExceptionHandler)")
        void forbiddenExceptionHandle_annotationIncludesAccessDeniedException() throws NoSuchMethodException {
            Method method = GlobalExceptionHandler.class.getMethod("forbiddenExceptionHandle");
            ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
            Class<?>[] handledTypes = annotation.value();

            boolean includesAccessDenied = false;
            for (Class<?> type : handledTypes) {
                if (type == AccessDeniedException.class) {
                    includesAccessDenied = true;
                    break;
                }
            }

            assertTrue(includesAccessDenied,
                    "forbiddenExceptionHandle should handle AccessDeniedException");
        }
    }

    @Nested
    @DisplayName("exceptionHandle")
    class ExceptionHandle {

        @Test
        @DisplayName("ErrorMsgException sets status 500 (code 50000 out of HTTP range, the fix)")
        void errorMsgException_setsStatus500() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            ErrorMsgException ex = new ErrorMsgException("System error!");

            Result<Void> result = handler.exceptionHandle(request, response, ex);

            verify(response).setStatus(500);
            assertEquals(Code.SYSTEM_ERROR.getCode(), result.getCode());
        }

        @Test
        @DisplayName("non-BaseException is wrapped and sets status 500")
        void nonBaseException_wrappedAndSetsStatus500() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            when(request.getMethod()).thenReturn("GET");
            when(request.getHeader("Content-Type")).thenReturn("application/json");
            when(request.getRequestURI()).thenReturn("/api/test");

            RuntimeException ex = new RuntimeException("unexpected");

            Result<Void> result = handler.exceptionHandle(request, response, ex);

            verify(response).setStatus(500);
            assertEquals(Code.SYSTEM_ERROR.getCode(), result.getCode());
        }

        @Test
        @DisplayName("BaseException with valid HTTP status code sets that status")
        void baseException_withValidHttpStatus_setsThatStatus() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            ErrorMsgException ex = new ErrorMsgException("404", "not found");

            Result<Void> result = handler.exceptionHandle(request, response, ex);

            verify(response).setStatus(404);
            assertEquals("404", result.getCode());
            assertEquals("not found", result.getMsg());
        }

        @Test
        @DisplayName("BaseException with non-numeric code falls back to 500 (NumberFormatException fix)")
        void baseException_nonNumericCode_fallsBackTo500() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            ErrorMsgException ex = new ErrorMsgException("ERR_CODE", "some error");

            Result<Void> result = handler.exceptionHandle(request, response, ex);

            verify(response).setStatus(500);
            assertEquals("ERR_CODE", result.getCode());
        }
    }

    @Nested
    @DisplayName("getStackTrace guard (fix: empty stack trace no longer throws AIOOBE)")
    class StackTraceGuard {

        @Test
        @DisplayName("handleHttpMessageNotReadableException does not throw on empty stack trace")
        void handleHttpMessageNotReadableException_emptyStackTrace_doesNotThrow() {
            HttpMessageNotReadableException ex =
                    new HttpMessageNotReadableException("bad", (HttpInputMessage) null);
            ex.setStackTrace(new StackTraceElement[0]);

            Result<Void> result = handler.handleHttpMessageNotReadableException(ex);

            assertNotNull(result);
            assertEquals(Code.BAD_REQUEST.getCode(), result.getCode());
        }

        @Test
        @DisplayName("handleHttpRequestMethodNotSupportedException does not throw on empty stack trace")
        void handleHttpRequestMethodNotSupportedException_emptyStackTrace_doesNotThrow() {
            HttpRequestMethodNotSupportedException ex =
                    new HttpRequestMethodNotSupportedException("POST");
            ex.setStackTrace(new StackTraceElement[0]);

            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/api/test");

            Result<Void> result = handler.handleHttpRequestMethodNotSupportedException(ex, request);

            assertNotNull(result);
        }

        @Test
        @DisplayName("handleHttpRequestMethodNotSupportedException does not throw on single-element stack trace")
        void handleHttpRequestMethodNotSupportedException_singleElementStackTrace_doesNotThrow() {
            HttpRequestMethodNotSupportedException ex =
                    new HttpRequestMethodNotSupportedException("POST");
            ex.setStackTrace(new StackTraceElement[]{
                    new StackTraceElement("com.example.Foo", "bar", "Foo.java", 10)
            });

            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/api/test");

            Result<Void> result = handler.handleHttpRequestMethodNotSupportedException(ex, request);

            assertNotNull(result);
        }
    }
}
