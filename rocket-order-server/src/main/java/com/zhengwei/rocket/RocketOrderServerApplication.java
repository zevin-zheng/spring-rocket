package com.zhengwei.rocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RocketOrderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketOrderServerApplication.class, args);
    }

}
