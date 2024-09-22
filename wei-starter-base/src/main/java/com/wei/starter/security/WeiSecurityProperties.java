package com.wei.starter.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * The type Wei security properties.
 *
 * @author Weijj0528
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.custom")
public class WeiSecurityProperties {

    /**
     * 安全配置启用开关
     */
    private boolean enable = false;
    /**
     * 开放接口配置
     * - 默认为空，表示所有接口都需要认证
     * - 配置接口不进行权限解析与认证，也无法获取用户信息
     * - 配置示例
     * - [method:]/example/hello
     */
    private List<String> openApis = Collections.emptyList();

    private Cors cors = new Cors();

    private String rolePrefix = "ROLE_";

    /**
     * 跨越配置
     */
    @Data
    public static class Cors {

        private String pathPattern = "/**";

        private String[] origins = new String[]{"*"};

        private String[] headers = new String[]{"*"};

        private String[] methods = new String[]{"*"};

    }

}
