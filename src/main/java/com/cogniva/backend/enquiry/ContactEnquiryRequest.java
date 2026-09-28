package com.cogniva.backend.enquiry;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload sent by the website's "Start a Conversation" form.
 * {@code website} is a honeypot field: real users never fill it, bots usually do.
 */
public record ContactEnquiryRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 120, message = "Full name must be at most 120 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 160, message = "Email must be at most 160 characters")
        String email,

        @Size(max = 30, message = "Phone number must be at most 30 characters")
        @Pattern(regexp = "^$|^[+0-9 ()\\-]{6,30}$", message = "Enter a valid phone number")
        String phone,

        @Size(max = 160, message = "Company must be at most 160 characters")
        String company,

        @NotBlank(message = "Project type is required")
        @Size(max = 80, message = "Project type must be at most 80 characters")
        String projectType,

        @NotBlank(message = "Please tell us about your project")
        @Size(min = 10, max = 5000, message = "Message must be between 10 and 5000 characters")
        String message,

        String website
) {
}
