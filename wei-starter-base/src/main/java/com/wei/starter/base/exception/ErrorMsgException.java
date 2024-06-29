package com.wei.starter.base.exception;

import com.wei.starter.base.bean.Code;
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
        super(Code.SYSTEM_ERROR);
    }

    public ErrorMsgException(String msg) {
        super(Code.SYSTEM_ERROR.getCode(), msg);
    }

    public ErrorMsgException(Code code) {
        super(code.getCode(), code.getMsg());
    }

    public ErrorMsgException(String code, String msg) {
        super(code, msg);
    }

}
