package com.senai.baseapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JavaAwsS3BaseAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaAwsS3BaseAppApplication.class, args);
    }

}
