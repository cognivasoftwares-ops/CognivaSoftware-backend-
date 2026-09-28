package com.cogniva.backend.catalog.project;

import java.util.List;

/** JSON shape intentionally mirrors the frontend's src/data/projects.js objects. */
public record ProjectResponse(
        Long id,
        String slug,
        String title,
        String industry,
        String scope,
        String description,
        List<String> tags,
        String accent,
        String liveUrl,
        ProjectStat stat1,
        ProjectStat stat2,
        String summary,
        String challenge,
        String approach,
        List<String> solution,
        int displayOrder,
        boolean published
) {
    public static ProjectResponse from(Project p) {
        return new ProjectResponse(p.getId(), p.getSlug(), p.getTitle(), p.getIndustry(), p.getScope(),
                p.getDescription(), p.getTags(), p.getAccent(), p.getLiveUrl(), p.getStat1(), p.getStat2(),
                p.getSummary(), p.getChallenge(), p.getApproach(), p.getSolution(),
                p.getDisplayOrder(), p.isPublished());
    }
}
