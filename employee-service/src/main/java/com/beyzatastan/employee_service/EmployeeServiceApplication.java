package com.beyzatastan.employee_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //servis Eureka Server’a register olsun diye -Eureka kullanıyorsan şart
@EnableFeignClients //servis başka bir servisi Feign Client ile çağıracaksa - ileride auth-service çağıracağım için
public class EmployeeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}