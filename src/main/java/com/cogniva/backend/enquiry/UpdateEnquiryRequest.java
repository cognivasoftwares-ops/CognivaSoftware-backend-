package com.cogniva.backend.enquiry;

import jakarta.validation.constraints.Size;

public record UpdateEnquiryRequest(
        EnquiryStatus status,
        @Size(max = 2000, message = "Notes must be at most 2000 characters") String adminNotes
) {
}
