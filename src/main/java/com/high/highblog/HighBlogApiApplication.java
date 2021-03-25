package com.high.highblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HighBlogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighBlogApiApplication.class, args);
    }

}
