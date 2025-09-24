package org.example.sampleservice.bootstrap;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * Sets Authorization: Bearer <token> header for Spring Cloud Config client calls very early in bootstrap.
 * We leverage property: spring.cloud.config.headers.Authorization
 *
 * Token resolution order:
 * - ENV: CONFIG_SERVER_TOKEN
 * - System property: config.client.token
 * If no token is present, we don't set the header (server may permitAll in dev).
 */
public class ConfigClientAuthBootstrapInitializer implements BootstrapRegistryInitializer {

    @Override
    public void initialize(BootstrapRegistry registry) {
        System.out.println("ConfigClientAuthBootstrapInitializer");
        // Resolve token from environment or system properties very early
        String token = System.getenv("CONFIG_SERVER_TOKEN");
        if (token == null || token.isEmpty()) {
            token = System.getProperty("config.client.token", "");
        }
        if (token != null && !token.isEmpty()) {
            // Configure Authorization header for Spring Cloud Config client
            System.setProperty("spring.cloud.config.headers.Authorization", "Bearer " + token);
        }
    }
}
