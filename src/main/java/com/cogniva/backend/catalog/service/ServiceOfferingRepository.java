package com.cogniva.backend.catalog.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

    List<ServiceOffering> findAllByPublishedTrueOrderByDisplayOrderAscIdAsc();

    List<ServiceOffering> findAllByOrderByDisplayOrderAscIdAsc();

    Optional<ServiceOffering> findBySlugAndPublishedTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
