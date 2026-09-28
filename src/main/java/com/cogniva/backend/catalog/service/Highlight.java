package com.cogniva.backend.catalog.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Highlight(
        @NotBlank(message = "Highlight title is required") @Size(max = 160) String title,
        @NotBlank(message = "Highlight description is required") @Size(max = 500) String description
) {
}
