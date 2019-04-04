package com.wei.springboot.starter.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wei.springboot.starter.exception.BaseException;
import com.wei.springboot.starter.exception.ErrorEnum;
import com.wei.springboot.starter.utils.ObjUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果封装
 *
 * @author
 */
@Data
public class ResultBean implements Serializable, Cloneable {
    /**
     * @author
     */
    private static final long serialVersionUID = 8632964679110167394L;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 错误编号
     */
    private int code;
    /**
     * 业务返回值
     */
    private JSONObject result;

    public ResultBean() {
        super();
        this.msg = ErrorEnum.SUCCESS.getMsg();
        this.code = ErrorEnum.SUCCESS.getCode();
        this.result = new JSONObject();
    }

    public ResultBean(BaseException exception) {
        this.msg = exception.getMessage();
        this.code = exception.getCode();
        this.result = new JSONObject();
    }

    public ResultBean(ErrorEnum msgEnum) {
        this.msg = msgEnum.getMsg();
        this.code = msgEnum.getCode();
        this.result = new JSONObject();
    }

    public ResultBean(ErrorEnum errorEnum, String msg) {
        this(errorEnum);
        this.result = new JSONObject();
    }

    public ResultBean(Object result) {
        this.msg = ErrorEnum.SUCCESS.getMsg();
        this.code = ErrorEnum.SUCCESS.getCode();
        if (result == null) {
            this.result = new JSONObject();
        } else if (ObjUtils.isArray(result)) {
            JSONObject json = new JSONObject();
            json.put("list", result);
            this.result = json;
        } else if (ObjUtils.isString(result)) {
            this.msg = String.valueOf(result);
            this.result = new JSONObject();
        } else if (ObjUtils.isBoolean(result)) {
            JSONObject json = new JSONObject();
            json.put("status", result);
            this.result = json;
        } else {
            if (result.getClass() == JSONObject.class) {
                this.result = (JSONObject) result;
            } else {
                this.result = JSON.parseObject(JSON.toJSONString(result));
            }
        }
    }


    @Override
    public ResultBean clone() {
        try {
            return (ResultBean) super.clone();
        } catch (Exception e) {
            return null;
        }
    }

}
