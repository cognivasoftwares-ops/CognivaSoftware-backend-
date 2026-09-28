package com.cogniva.backend.auth;

public record AdminProfile(Long id, String email, String fullName, String role) {

    public static AdminProfile from(AdminUser user) {
        return new AdminProfile(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }
}
