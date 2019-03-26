package com.wei.springboot.starter.swagger2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Swagger properties.
 *
 * @author William
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * 是否开启
     */
    private boolean enabled = false;
    /**
     * 标题
     */
    private String title = "Api Title";
    /**
     * 说明
     */
    private String description = "Api description";
    /**
     * 版本
     */
    private String version = "Version";
    /**
     * 联系人名
     */
    private String contactName = "William";
    /**
     * 联系人地址
     */
    private String contactUrl = "";
    /**
     * 联系人邮箱
     */
    private String contactEmail = "weiun0528@gmail.com";
    /**
     * 扫描的包
     */
    private String basePackage = "";

}
