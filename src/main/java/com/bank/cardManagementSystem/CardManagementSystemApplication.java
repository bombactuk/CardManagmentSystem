package com.bank.cardManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CardManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardManagementSystemApplication.class, args);
    }

}
