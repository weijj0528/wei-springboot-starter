package com.wei.starter.base.exception;


import com.wei.starter.base.bean.CodeEnum;

/**
 * 访问异常  抛出 400
 * 坏的请求   参数不完整
 */
public class BadRequestException extends BaseException {

    private static final long serialVersionUID = -7303374187724238548L;

    public BadRequestException() {
        super(CodeEnum.BadRequestException.getCode(), CodeEnum.BadRequestException.getMsg());
    }

}
