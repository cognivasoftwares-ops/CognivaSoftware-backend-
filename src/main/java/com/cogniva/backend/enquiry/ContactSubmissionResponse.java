package com.cogniva.backend.enquiry;

/** Minimal response for the public form - never echoes stored data back. */
public record ContactSubmissionResponse(Long referenceId, String message) {
}
