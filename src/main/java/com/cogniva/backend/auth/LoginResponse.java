package com.cogniva.backend.auth;

import java.time.Instant;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        AdminProfile user
) {
}
