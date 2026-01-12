package com.gymcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Alish
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableFeignClients
public class GymApp {

    public static void main(String[] args) {
        SpringApplication.run(GymApp.class, args);
    }
}
