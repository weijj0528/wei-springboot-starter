package com.wei.starter.base.exception;


import com.wei.starter.base.bean.CodeEnum;

/**
 * 用户禁用异常
 */
public class ForbiddenException extends BaseException {
    private static final long serialVersionUID = 8199514192443835496L;

    public ForbiddenException() {
        super(CodeEnum.FORBIDDEN);
    }
}
