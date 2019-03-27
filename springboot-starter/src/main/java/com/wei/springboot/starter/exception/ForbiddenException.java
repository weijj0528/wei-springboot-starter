package com.wei.springboot.starter.exception;


/**
 * 用户禁用异常
 */
public class ForbiddenException extends BaseException {
    private static final long serialVersionUID = 8199514192443835496L;

    @Override
    public String causedBy() {
        return ErrorEnum.ForbiddenException.getMsg();
    }

}
