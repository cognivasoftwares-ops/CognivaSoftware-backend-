package com.cogniva.backend.catalog.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Headline stat shown on a case study card, e.g. { value: "500+", label: "Projects Showcased" }. */
public record ProjectStat(
        @NotBlank @Size(max = 60) String value,
        @NotBlank @Size(max = 80) String label
) {
}
