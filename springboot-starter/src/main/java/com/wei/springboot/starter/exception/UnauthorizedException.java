package com.wei.springboot.starter.exception;


/**
 * 用户已在其他设备登录  当前认证无效	   抛出 401
 *
 * @author William
 * @date 2017年3月30日 下午12:32:11
 */
public class UnauthorizedException extends BaseException {

    private static final long serialVersionUID = 8088934606432865810L;

    public UnauthorizedException() {
        super(ErrorEnum.UnauthorizedException);
    }

}
