package com.wei.starter.base.advice;

import cn.hutool.core.util.StrUtil;
import com.wei.starter.base.bean.Code;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author William
 * @Date 2019/3/13
 * @Description 异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败 {}:{}", e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName(), e);
        return Result.failure(Code.BAD_REQUEST.getCode(), "参数解析失败");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("{} {}.{} 不支持当前请求方法 {}, 支持方法 {}",
                request.getRequestURI(), e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[1].getMethodName(), e.getMethod(),
                e.getSupportedMethods());
        return Result.failure(HttpStatus.METHOD_NOT_ALLOWED.toString(), "不支持的请求方法");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.error("{} 不支持当前媒体类型 {}, 支持类型 {}",
                request.getRequestURI(), e.getContentType(), e.getSupportedMediaTypes());
        return Result.failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(), "不支持的请求方式");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> omsExceptionHandle(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        FieldError error = result.getFieldError();
        String field = null;
        String message = null;
        if (error != null) {
            field = error.getField();
            message = error.getDefaultMessage();
        }
        log.error("{} 参数校验失败 {}:{}", ex.getParameter().getMethod(), field, message);
        return Result.failure(Code.BAD_REQUEST.getCode(), message);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class, AccessDeniedException.class})
    public Result<Void> unauthorizedExceptionHandle() {
        return Result.failure(Code.UNAUTHORIZED.getCode(), Code.UNAUTHORIZED.getMsg());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public Result<Void> forbiddenExceptionHandle() {
        return Result.failure(Code.FORBIDDEN.getCode(), Code.FORBIDDEN.getMsg());
    }

    @ResponseBody
    @ExceptionHandler()
    public Result<Void> exceptionHandle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (!(ex instanceof BaseException)) {
            String method = request.getMethod();
            String contentType = request.getHeader("Content-Type");
            String requestURI = request.getRequestURI();
            String errorMsg = StrUtil.format("{}({}):{} error.", method, contentType, requestURI);
            log.error(errorMsg, ex);
            ex = new ErrorMsgException("System error!");
        }
        BaseException e = (BaseException) ex;
        Result<Void> result = new Result<>(e.getCode(), e.getMessage(), null);
        if (!(e instanceof ErrorMsgException)) {
            response.setStatus(Integer.parseInt(e.getCode()));
        }
        return result;
    }

}
