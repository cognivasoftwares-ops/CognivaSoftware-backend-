package com.cogniva.backend.auth;

import com.cogniva.backend.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Creates the first admin account (from app.admin.* / ADMIN_* env vars) when the admin table is empty.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements ApplicationRunner {

    private final AdminUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties properties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            return;
        }
        AppProperties.Admin admin = properties.admin();
        AdminUser user = new AdminUser();
        user.setEmail(admin.email().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(admin.password()));
        user.setFullName(admin.name());
        user.setRole("ADMIN");
        user.setActive(true);
        repository.save(user);
        log.warn("Created initial admin user '{}'. Change the default password via ADMIN_PASSWORD before going live.",
                user.getEmail());
    }
}
