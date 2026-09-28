package com.cogniva.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Strongly-typed binding for the {@code app.*} section of application.yml.
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Cors cors, Jwt jwt, Admin admin) {

    public record Cors(List<String> allowedOrigins) {
    }

    public record Jwt(String secret, long expirationMinutes) {
    }

    public record Admin(String email, String password, String name) {
    }
}
