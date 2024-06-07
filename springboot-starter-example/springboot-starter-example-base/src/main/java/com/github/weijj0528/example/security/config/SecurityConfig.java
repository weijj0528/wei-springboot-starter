package com.github.weijj0528.example.security.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Security config.
 *
 * @author William.Wei
 */
@Configuration
public class SecurityConfig implements ApplicationContextAware {

    private ApplicationContext context;

    /**
     * 用户权限分配
     */
    private final Map<String, Set<String>> userPermission = new HashMap<>();

    /**
     * Gets user permission.
     *
     * @return the user permission
     */
    public Map<String, Set<String>> getUserPermission() {
        return userPermission;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        this.context = context;
    }

    /**
     * keytool是Java提供的命令行工具，用于管理密钥和证书，包括生成JKS文件。以下是生成JKS文件并用于JWT签名的基本步骤：
     * <p>
     * 生成RSA密钥对: 使用keytool生成一个RSA密钥对，并将其存储在JKS文件中。
     * 例如，创建一个名为jwt.jks的密钥库，并设置别名为jwt-key，密码为yourpassword：
     * keytool -genkeypair -alias jwt-key -keyalg RSA -keystore jwt.jks -keypass yourpassword -storepass yourpassword
     * <p>
     * 导出公钥: 通常，公钥会被公开给客户端，用于验证JWT签名。可以使用keytool导出公钥到PEM格式
     * keytool -list -rfc -v -keystore jwt.jks -alias jwt-key -storepass yourpassword | openssl x509 -pubkey -out public_key.pem
     *
     * @param keyAlias    key alias
     * @param keyStore    key store
     * @param keyPassword key password
     * @return key pair
     * @throws FileNotFoundException the file not found exception
     */
    @Bean
    public KeyPair keyPair(@Value("${wei-jwt.key-alias}") String keyAlias,
                           @Value("${wei-jwt.key-store}") String keyStore,
                           @Value("${wei-jwt.key-password}") String keyPassword) throws FileNotFoundException {

        Resource keyStoreResource;
        if (ResourceUtils.isUrl(keyStore)) {
            keyStoreResource = this.context.getResource(keyStore);
        } else {
            keyStoreResource = new FileSystemResource(ResourceUtils.getFile(keyStore));
        }
        char[] keyStorePwd = keyPassword.toCharArray();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStoreResource, keyStorePwd);
        return keyStoreKeyFactory.getKeyPair(keyAlias, keyStorePwd);
    }
}
