package com.hegetomi.userservice;

import com.hegetomi.tokenlib.JwtAuthFilter;
import com.hegetomi.userservice.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {JwtAuthFilter.class, UserServiceApplication.class})
public class UserServiceApplication implements CommandLineRunner {

    @Autowired
    InitService initService;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //initService.deleteAll();
        initService.insertInit();
    }
}
