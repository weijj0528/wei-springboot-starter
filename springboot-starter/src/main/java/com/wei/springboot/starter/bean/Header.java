package com.wei.springboot.starter.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务参数封装
 * @author Administrator
 */
@Data
public class Header implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求版本号
     */
    private String version;
    /**
     * 时间戳字符串
     */
    private String time;
    /**
     * 设备ID
     */
    private String devices;
    /**
     * 签名
     */
    private String sign;

    /**
     * 请求客户端类型
     */
    private String model;

    /**
     * 用户 请求 ip地址
     */
    private String ip;

    /**
     * Token
     */
    private String token;

}
