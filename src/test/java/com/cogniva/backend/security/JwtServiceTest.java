package com.cogniva.backend.security;

import com.cogniva.backend.config.AppProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static AppProperties props(String secret) {
        return new AppProperties(
                new AppProperties.Cors(List.of("http://localhost:5173")),
                new AppProperties.Jwt(secret, 60),
                new AppProperties.Admin("admin@test.com", "pw", "Admin"));
    }

    @Test
    void issuesAndValidatesToken() {
        JwtService jwt = new JwtService(props("a-very-long-test-secret-that-is-at-least-32-bytes"));
        JwtService.IssuedToken token = jwt.issue("admin@test.com", "ADMIN");

        assertThat(jwt.validateAndGetSubject(token.value())).contains("admin@test.com");
    }

    @Test
    void rejectsTamperedToken() {
        JwtService jwt = new JwtService(props("a-very-long-test-secret-that-is-at-least-32-bytes"));
        String token = jwt.issue("admin@test.com", "ADMIN").value();

        assertThat(jwt.validateAndGetSubject(token + "x")).isEmpty();
        assertThat(jwt.validateAndGetSubject("not-a-jwt")).isEmpty();
    }

    @Test
    void rejectsTokenSignedWithDifferentSecret() {
        JwtService a = new JwtService(props("secret-number-one-which-is-long-enough-123"));
        JwtService b = new JwtService(props("secret-number-two-which-is-long-enough-456"));

        assertThat(b.validateAndGetSubject(a.issue("x@y.com", "ADMIN").value())).isEmpty();
    }

    @Test
    void refusesShortSecret() {
        assertThatThrownBy(() -> new JwtService(props("too-short")))
                .isInstanceOf(IllegalStateException.class);
    }
}
