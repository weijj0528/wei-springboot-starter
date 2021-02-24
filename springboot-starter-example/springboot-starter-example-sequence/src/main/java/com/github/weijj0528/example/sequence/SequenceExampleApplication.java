package com.github.weijj0528.example.sequence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * The type Redis example application.
 *
 * @author William.Wei
 */
@MapperScan("com.github.weijj0528.example.sequence.mapper")
@SpringBootApplication
public class SequenceExampleApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SequenceExampleApplication.class, args);
    }

}
