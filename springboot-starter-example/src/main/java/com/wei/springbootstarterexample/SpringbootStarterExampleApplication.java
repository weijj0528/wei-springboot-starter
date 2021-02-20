package com.wei.springbootstarterexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@MapperScan("com.wei.springbootstarterexample.mapper")
@SpringBootApplication
public class SpringbootStarterExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootStarterExampleApplication.class, args);
    }

}
