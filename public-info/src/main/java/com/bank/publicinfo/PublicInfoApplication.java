package com.bank.publicinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.bank.publicinfo", "com.bank.common"})
public class PublicInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PublicInfoApplication.class, args);
    }
}
