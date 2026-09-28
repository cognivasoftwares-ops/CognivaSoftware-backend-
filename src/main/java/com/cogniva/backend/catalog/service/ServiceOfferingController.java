package com.cogniva.backend.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Public, read-only service catalogue used by the Services pages and Footer. */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService service;

    @GetMapping
    public List<ServiceOfferingResponse> list() {
        return service.listPublished();
    }

    @GetMapping("/{slug}")
    public ServiceOfferingResponse getBySlug(@PathVariable String slug) {
        return service.getPublishedBySlug(slug);
    }
}
