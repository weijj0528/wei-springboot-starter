package com.github.weijj0528.example.mybatis;

import com.github.weijj0528.example.mybatis.dto.AddArgs;
import com.wei.starter.mybatis.plugin.ArgsInterceptor;
import com.wei.starter.mybatis.plugin.ArgsProvider;
import com.wei.starter.mybatis.plugin.SqlCostInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The type Redis example application.
 *
 * @author William.Wei
 */
@SpringBootApplication
public class MybatisExampleApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MybatisExampleApplication.class, args);
    }


    @Bean
    public SqlCostInterceptor sqlCostInterceptor() {
        return new SqlCostInterceptor();
    }

    @Bean
    public ArgsInterceptor argsInterceptor() {
        return new ArgsInterceptor(new ArgsProvider() {

            private final AddArgs addArgs = new AddArgs();

            @Override
            public Object insertArgs() {
                return addArgs;
            }

            @Override
            public Object updateArgs() {
                return addArgs;
            }
        });
    }

}
