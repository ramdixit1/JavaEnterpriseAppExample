package com.bank.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for transaction-service.
 */
@SpringBootApplication
public class TransactionServiceApplication {

    /**
     * Starts transaction-service with embedded Tomcat.
     */
    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
}
