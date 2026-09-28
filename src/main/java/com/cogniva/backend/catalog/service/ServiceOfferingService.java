package com.cogniva.backend.catalog.service;

import com.cogniva.backend.common.ConflictException;
import com.cogniva.backend.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService {

    private final ServiceOfferingRepository repository;

    @Transactional(readOnly = true)
    public List<ServiceOfferingResponse> listPublished() {
        return repository.findAllByPublishedTrueOrderByDisplayOrderAscIdAsc().stream()
                .map(ServiceOfferingResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ServiceOfferingResponse getPublishedBySlug(String slug) {
        return repository.findBySlugAndPublishedTrue(slug)
                .map(ServiceOfferingResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Service", slug));
    }

    @Transactional(readOnly = true)
    public List<ServiceOfferingResponse> listAll() {
        return repository.findAllByOrderByDisplayOrderAscIdAsc().stream()
                .map(ServiceOfferingResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ServiceOfferingResponse get(Long id) {
        return ServiceOfferingResponse.from(find(id));
    }

    @Transactional
    public ServiceOfferingResponse create(ServiceOfferingRequest request) {
        if (repository.existsBySlug(request.slug())) {
            throw new ConflictException("A service with slug '" + request.slug() + "' already exists");
        }
        ServiceOffering entity = new ServiceOffering();
        apply(entity, request);
        return ServiceOfferingResponse.from(repository.save(entity));
    }

    @Transactional
    public ServiceOfferingResponse update(Long id, ServiceOfferingRequest request) {
        ServiceOffering entity = find(id);
        if (repository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new ConflictException("A service with slug '" + request.slug() + "' already exists");
        }
        apply(entity, request);
        return ServiceOfferingResponse.from(repository.saveAndFlush(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private ServiceOffering find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", id));
    }

    private static void apply(ServiceOffering e, ServiceOfferingRequest r) {
        e.setSlug(r.slug().trim());
        e.setIcon(r.icon().trim());
        e.setAccent(r.accent() == null || r.accent().isBlank() ? "sky" : r.accent().trim());
        e.setTitle(r.title().trim());
        e.setTagline(r.tagline());
        e.setDescription(r.description().trim());
        e.setHighlights(r.highlights() == null ? new ArrayList<>() : new ArrayList<>(r.highlights()));
        e.setTechStack(r.techStack() == null ? new ArrayList<>() : new ArrayList<>(r.techStack()));
        e.setIdealFor(r.idealFor() == null ? new ArrayList<>() : new ArrayList<>(r.idealFor()));
        if (r.displayOrder() != null) {
            e.setDisplayOrder(r.displayOrder());
        }
        if (r.published() != null) {
            e.setPublished(r.published());
        }
    }
}
