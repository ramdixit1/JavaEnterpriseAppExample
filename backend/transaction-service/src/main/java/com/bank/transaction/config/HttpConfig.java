package com.bank.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Infrastructure bean configuration for HTTP integrations.
 */
@Configuration
public class HttpConfig {

    /**
     * Provides RestTemplate bean injected into service clients.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
