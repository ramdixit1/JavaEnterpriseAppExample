package com.bank.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for account-service microservice.
 * Spring Boot starts an embedded Tomcat server (chosen for simplicity and wide enterprise adoption).
 */
@SpringBootApplication
public class AccountServiceApplication {

    /**
     * Bootstraps account-service runtime.
     *
     * @param args startup arguments from command line.
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
