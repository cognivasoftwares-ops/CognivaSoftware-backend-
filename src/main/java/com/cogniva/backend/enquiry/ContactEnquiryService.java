package com.cogniva.backend.enquiry;

import com.cogniva.backend.common.PageResponse;
import com.cogniva.backend.common.ResourceNotFoundException;
import com.cogniva.backend.common.TooManyRequestsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactEnquiryService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ContactEnquiryRepository repository;
    private final ContactRateLimiter rateLimiter;

    @Transactional
    public ContactSubmissionResponse submit(ContactEnquiryRequest request, String ip, String userAgent) {
        if (!rateLimiter.tryAcquire(ip == null ? "unknown" : ip)) {
            throw new TooManyRequestsException("Too many messages from this network. Please try again in a few minutes.");
        }

        ContactEnquiry enquiry = new ContactEnquiry();
        enquiry.setFullName(request.fullName().trim());
        enquiry.setEmail(request.email().trim().toLowerCase());
        enquiry.setPhone(blankToNull(request.phone()));
        enquiry.setCompany(blankToNull(request.company()));
        enquiry.setProjectType(request.projectType().trim());
        enquiry.setMessage(request.message().trim());
        enquiry.setSourceIp(ip);
        enquiry.setUserAgent(truncate(userAgent, 512));

        // Honeypot filled in -> almost certainly a bot. Store it as SPAM but respond normally.
        boolean isSpam = request.website() != null && !request.website().isBlank();
        enquiry.setStatus(isSpam ? EnquiryStatus.SPAM : EnquiryStatus.NEW);

        ContactEnquiry saved = repository.save(enquiry);
        log.info("New contact enquiry #{} from {} ({}){}", saved.getId(), saved.getEmail(),
                saved.getProjectType(), isSpam ? " [flagged as spam]" : "");

        return new ContactSubmissionResponse(saved.getId(),
                "Thanks for reaching out - we'll get back to you within one business day.");
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactEnquiryResponse> list(EnquiryStatus status, String query, int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        PageRequest pageable = PageRequest.of(Math.max(page, 0), safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<ContactEnquiry> spec = ContactEnquiryRepository.hasStatus(status)
                .and(ContactEnquiryRepository.matches(query));
        return PageResponse.from(repository.findAll(spec, pageable).map(ContactEnquiryResponse::from));
    }

    @Transactional(readOnly = true)
    public ContactEnquiryResponse get(Long id) {
        return ContactEnquiryResponse.from(find(id));
    }

    @Transactional
    public ContactEnquiryResponse update(Long id, UpdateEnquiryRequest request) {
        ContactEnquiry enquiry = find(id);
        if (request.status() != null) {
            enquiry.setStatus(request.status());
        }
        if (request.adminNotes() != null) {
            enquiry.setAdminNotes(blankToNull(request.adminNotes()));
        }
        return ContactEnquiryResponse.from(repository.saveAndFlush(enquiry));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(find(id));
    }

    @Transactional(readOnly = true)
    public Map<EnquiryStatus, Long> stats() {
        Map<EnquiryStatus, Long> result = new EnumMap<>(EnquiryStatus.class);
        for (EnquiryStatus s : EnquiryStatus.values()) {
            result.put(s, 0L);
        }
        repository.countByStatus().forEach(c -> result.put(c.getStatus(), c.getTotal() == null ? 0L : c.getTotal()));
        return result;
    }

    private ContactEnquiry find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Enquiry", id));
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String truncate(String value, int max) {
        return value == null ? null : (value.length() <= max ? value : value.substring(0, max));
    }
}
