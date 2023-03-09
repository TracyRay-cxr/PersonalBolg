package com.ray;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.ray.mapper")
@EnableScheduling
@EnableSwagger2
@EnableTransactionManagement
public class frontBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(frontBlogApplication.class,args);
    }
}
