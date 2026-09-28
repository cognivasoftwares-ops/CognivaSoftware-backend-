package com.cogniva.backend.auth;

import com.cogniva.backend.common.ResourceNotFoundException;
import com.cogniva.backend.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AdminUserRepository repository;
    private final JwtService jwtService;

    /** Exchange admin email + password for a JWT bearer token. */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));

        AdminUser user = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user", email));
        JwtService.IssuedToken token = jwtService.issue(user.getEmail(), user.getRole());
        return new LoginResponse(token.value(), "Bearer", token.expiresAt(), AdminProfile.from(user));
    }

    /** Returns the currently logged-in admin (requires a valid token). */
    @GetMapping("/me")
    public AdminProfile me(Authentication authentication) {
        return repository.findByEmailIgnoreCase(authentication.getName())
                .map(AdminProfile::from)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user", authentication.getName()));
    }
}
