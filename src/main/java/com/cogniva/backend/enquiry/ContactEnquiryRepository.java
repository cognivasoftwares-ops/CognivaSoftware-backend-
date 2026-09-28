package com.cogniva.backend.enquiry;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactEnquiryRepository extends JpaRepository<ContactEnquiry, Long>,
        JpaSpecificationExecutor<ContactEnquiry> {

    @Query("SELECT e.status AS status, COUNT(e) AS total FROM ContactEnquiry e GROUP BY e.status")
    List<StatusCount> countByStatus();

    interface StatusCount {
        EnquiryStatus getStatus();

        Long getTotal();
    }

    static Specification<ContactEnquiry> hasStatus(EnquiryStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    static Specification<ContactEnquiry> matches(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) {
                return null;
            }
            String like = "%" + text.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), like),
                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(cb.lower(cb.coalesce(root.<String>get("company"), "")), like)
            );
        };
    }
}
