package com.wei.springboot.starter.exception.handler;

import cn.hutool.core.util.StrUtil;
import com.wei.springboot.starter.bean.ResultBean;
import com.wei.springboot.starter.exception.BaseException;
import com.wei.springboot.starter.exception.ErrorMsgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ExceptionHandler()
    public ResultBean omsExceptionHandle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
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
