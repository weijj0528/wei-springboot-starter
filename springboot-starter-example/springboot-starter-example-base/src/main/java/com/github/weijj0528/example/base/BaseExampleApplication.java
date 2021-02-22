package com.github.weijj0528.example.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * The type Cache example application.
 *
 * @author William.Wei
 */
@MapperScan("com.github.weijj0528.example.base.mapper")
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
