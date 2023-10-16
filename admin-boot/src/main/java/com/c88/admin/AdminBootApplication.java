package com.c88.admin;

import com.c88.feign.AuthFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//使用common-web底下的component
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.c88")
@EnableFeignClients(basePackageClasses = {AuthFeignClient.class})
public class AdminBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminBootApplication.class, args);
    }

}
