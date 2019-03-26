package com.wei.springboot.starter.exception;


/**
 * 用户已在其他设备登录  当前认证无效	   抛出 401
 *
 * @author Johnson.Jia
 * @date 2017年3月30日 下午12:32:11
 */
public class LogoutException extends BaseException {

    private static final long serialVersionUID = 8088934606432865810L;

    @Override
    public String causedBy() {
        return ErrorEnum.LogoutException.getMsg();
    }
}
