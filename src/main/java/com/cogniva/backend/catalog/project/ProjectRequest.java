package com.cogniva.backend.catalog.project;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProjectRequest(
        @NotBlank @Size(max = 160)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must be lowercase words separated by hyphens")
        String slug,
        @NotBlank @Size(max = 200) String title,
        @Size(max = 160) String industry,
        @Size(max = 500) String scope,
        @NotBlank String description,
        List<@NotBlank String> tags,
        @Size(max = 30) String accent,
        @Size(max = 255)
        @Pattern(regexp = "^$|^https?://.+", message = "Live URL must start with http:// or https://")
        String liveUrl,
        @Valid ProjectStat stat1,
        @Valid ProjectStat stat2,
        String summary,
        String challenge,
        String approach,
        List<@NotBlank String> solution,
        Integer displayOrder,
        Boolean published
) {
}
