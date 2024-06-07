package com.github.weijj0528.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * The type Cache example application.
 *
 * @author William.Wei
 */
@EnableAspectJAutoProxy
@SpringBootApplication
public class BaseExampleApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BaseExampleApplication.class, args);
    }

}
