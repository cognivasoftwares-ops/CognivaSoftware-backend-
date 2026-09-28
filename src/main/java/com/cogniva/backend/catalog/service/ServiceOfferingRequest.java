package com.cogniva.backend.catalog.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ServiceOfferingRequest(
        @NotBlank @Size(max = 120)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be lowercase words separated by hyphens")
        String slug,
        @NotBlank @Size(max = 60) String icon,
        @Size(max = 30) String accent,
        @NotBlank @Size(max = 160) String title,
        @Size(max = 255) String tagline,
        @NotBlank String description,
        List<@Valid Highlight> highlights,
        List<@NotBlank String> techStack,
        List<@NotBlank String> idealFor,
        Integer displayOrder,
        Boolean published
) {
}
