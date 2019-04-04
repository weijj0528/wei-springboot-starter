package com.wei.springboot.starter.exception;


/**
 * 访问异常  抛出 408
 * 请求超时 异常
 */
public class OvertimeException extends BaseException {
    private static final long serialVersionUID = -7303374187724238548L;

    public OvertimeException() {
        super(ErrorEnum.OvertimeException);
    }

}
