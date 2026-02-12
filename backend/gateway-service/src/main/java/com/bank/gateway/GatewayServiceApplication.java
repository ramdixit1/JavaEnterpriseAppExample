package com.bank.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for gateway-service.
 * Spring Cloud Gateway provides a single ingress endpoint used by React frontend.
 */
@SpringBootApplication
public class GatewayServiceApplication {

    /**
     * Boots gateway-service runtime.
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
