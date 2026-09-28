package com.cogniva.backend.enquiry;

import java.time.Instant;

public record ContactEnquiryResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String company,
        String projectType,
        String message,
        EnquiryStatus status,
        String adminNotes,
        String sourceIp,
        Instant createdAt,
        Instant updatedAt
) {
    public static ContactEnquiryResponse from(ContactEnquiry e) {
        return new ContactEnquiryResponse(
                e.getId(), e.getFullName(), e.getEmail(), e.getPhone(), e.getCompany(),
                e.getProjectType(), e.getMessage(), e.getStatus(), e.getAdminNotes(),
                e.getSourceIp(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
