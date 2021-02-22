package com.github.weijj0528.example.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Redis example application.
 *
 * @author William.Wei
 */
@SpringBootApplication
public class LockExampleApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(LockExampleApplication.class, args);
    }

}
