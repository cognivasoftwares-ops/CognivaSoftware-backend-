package com.cogniva.backend.catalog.project;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Public, read-only portfolio used by the Portfolio and Case Study pages. */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @GetMapping
    public List<ProjectResponse> list() {
        return service.listPublished();
    }

    @GetMapping("/{slug}")
    public ProjectResponse getBySlug(@PathVariable String slug) {
        return service.getPublishedBySlug(slug);
    }
}
