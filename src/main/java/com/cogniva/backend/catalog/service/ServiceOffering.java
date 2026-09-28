package com.cogniva.backend.catalog.service;

import com.cogniva.backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * A service Cogniva offers (Web Development, Mobile Apps, ...).
 * Named ServiceOffering to avoid clashing with Spring's @Service annotation.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "service_offerings")
public class ServiceOffering extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false, length = 60)
    private String icon;

    @Column(nullable = false, length = 30)
    private String accent = "sky";

    @Column(nullable = false, length = 160)
    private String title;

    @Column(length = 255)
    private String tagline;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<Highlight> highlights = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tech_stack", nullable = false, columnDefinition = "jsonb")
    private List<String> techStack = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ideal_for", nullable = false, columnDefinition = "jsonb")
    private List<String> idealFor = new ArrayList<>();

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean published = true;
}
