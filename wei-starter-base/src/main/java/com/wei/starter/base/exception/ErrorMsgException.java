package com.wei.starter.base.exception;

import com.wei.starter.base.bean.CodeEnum;
import lombok.Data;

/**
 * 返回自定义错误信息 错误问题
 *
 * @author William
 */
@Data
public class ErrorMsgException extends BaseException {

    private static final long serialVersionUID = 1452976038790619628L;

    public ErrorMsgException() {
        super(CodeEnum.ERROR_SERVER);
    }

    public ErrorMsgException(String msg) {
        super(CodeEnum.ERROR_SERVER.getCode(), msg);
    }

    public ErrorMsgException(String code, String msg) {
        super(code, msg);
    }

}
