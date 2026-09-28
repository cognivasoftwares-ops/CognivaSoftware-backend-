package com.cogniva.backend.catalog.project;

import com.cogniva.backend.common.ConflictException;
import com.cogniva.backend.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> listPublished() {
        return repository.findAllByPublishedTrueOrderByDisplayOrderAscIdAsc().stream()
                .map(ProjectResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getPublishedBySlug(String slug) {
        return repository.findBySlugAndPublishedTrue(slug)
                .map(ProjectResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Project", slug));
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listAll() {
        return repository.findAllByOrderByDisplayOrderAscIdAsc().stream()
                .map(ProjectResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse get(Long id) {
        return ProjectResponse.from(find(id));
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        if (repository.existsBySlug(request.slug())) {
            throw new ConflictException("A project with slug '" + request.slug() + "' already exists");
        }
        Project entity = new Project();
        apply(entity, request);
        return ProjectResponse.from(repository.save(entity));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project entity = find(id);
        if (repository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new ConflictException("A project with slug '" + request.slug() + "' already exists");
        }
        apply(entity, request);
        return ProjectResponse.from(repository.saveAndFlush(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Project find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    private static void apply(Project e, ProjectRequest r) {
        e.setSlug(r.slug().trim());
        e.setTitle(r.title().trim());
        e.setIndustry(r.industry());
        e.setScope(r.scope());
        e.setDescription(r.description().trim());
        e.setTags(r.tags() == null ? new ArrayList<>() : new ArrayList<>(r.tags()));
        e.setAccent(r.accent() == null || r.accent().isBlank() ? "sky" : r.accent().trim());
        e.setLiveUrl(r.liveUrl() == null || r.liveUrl().isBlank() ? null : r.liveUrl().trim());
        e.setStat1(r.stat1());
        e.setStat2(r.stat2());
        e.setSummary(r.summary());
        e.setChallenge(r.challenge());
        e.setApproach(r.approach());
        e.setSolution(r.solution() == null ? new ArrayList<>() : new ArrayList<>(r.solution()));
        if (r.displayOrder() != null) {
            e.setDisplayOrder(r.displayOrder());
        }
        if (r.published() != null) {
            e.setPublished(r.published());
        }
    }
}
