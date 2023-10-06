package com.skillup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderApp2_2 {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp2_2.class, args);
        System.out.println("OrderApp2_2");
    }
}