package com.wei.springboot.starter.controller;

import cn.hutool.core.util.StrUtil;
import com.wei.springboot.starter.bean.ResultBean;
import com.wei.springboot.starter.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultBean handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败 {}:{}", e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName(), e);
        return ResultBean.failure(ErrorEnum.BadRequestException.getCode(), "参数解析失败");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultBean handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("{} {}.{} 不支持当前请求方法 {}, 支持方法 {}",
                request.getRequestURI(), e.getStackTrace()[0].getClassName(),
                e.getStackTrace()[1].getMethodName(), e.getMethod(),
                e.getSupportedMethods());
        return ResultBean.failure(HttpStatus.METHOD_NOT_ALLOWED.value(), "不支持的请求方法");
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultBean handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.error("{} 不支持当前媒体类型 {}, 支持类型 {}",
                request.getRequestURI(), e.getContentType(), e.getSupportedMediaTypes());
        return ResultBean.failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "不支持当前媒体类型");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBean omsExceptionHandle(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String message = error.getDefaultMessage();
        log.error("{} 参数校验失败 {}:{}", ex.getParameter().getMethod(), field, message);
        return ResultBean.failure(ErrorEnum.BadRequestException.getCode(), message);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResultBean unauthorizedExceptionHandle() {
        return ResultBean.failure(ErrorEnum.UnauthorizedException.getCode(), ErrorEnum.UnauthorizedException.getMsg());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    public ResultBean forbiddenExceptionHandle() {
        return ResultBean.failure(ErrorEnum.ForbiddenException.getCode(), ErrorEnum.ForbiddenException.getMsg());
    }

    @ResponseBody
    @ExceptionHandler()
    public ResultBean exceptionHandle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (!(ex instanceof BaseException)) {
            String method = request.getMethod();
            String contentType = request.getHeader("Content-Type");
            String requestURI = request.getRequestURI();
            String errorMsg = StrUtil.format("{}({}):{} error.", method, contentType, requestURI);
            log.error(errorMsg, ex);
            ex = new ErrorMsgException("System error!");
        }
        BaseException e = (BaseException) ex;
        ResultBean resultBean = new ResultBean(e);
        if (!(e instanceof ErrorMsgException)) {
            response.setStatus(e.getCode());
        }
        return resultBean;
    }

}
