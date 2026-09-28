package com.cogniva.backend.catalog.service;

import java.util.List;

/** JSON shape intentionally mirrors the frontend's src/data/services.js objects. */
public record ServiceOfferingResponse(
        Long id,
        String slug,
        String icon,
        String accent,
        String title,
        String tagline,
        String description,
        List<Highlight> highlights,
        List<String> techStack,
        List<String> idealFor,
        int displayOrder,
        boolean published
) {
    public static ServiceOfferingResponse from(ServiceOffering s) {
        return new ServiceOfferingResponse(s.getId(), s.getSlug(), s.getIcon(), s.getAccent(), s.getTitle(),
                s.getTagline(), s.getDescription(), s.getHighlights(), s.getTechStack(), s.getIdealFor(),
                s.getDisplayOrder(), s.isPublished());
    }
}
