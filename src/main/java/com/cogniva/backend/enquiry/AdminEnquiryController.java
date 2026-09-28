package com.cogniva.backend.enquiry;

import com.cogniva.backend.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Admin-only management of contact enquiries (requires a Bearer token). */
@RestController
@RequestMapping("/api/admin/enquiries")
@RequiredArgsConstructor
public class AdminEnquiryController {

    private final ContactEnquiryService service;

    @GetMapping
    public PageResponse<ContactEnquiryResponse> list(@RequestParam(required = false) EnquiryStatus status,
                                                     @RequestParam(required = false) String q,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        return service.list(status, q, page, size);
    }

    @GetMapping("/stats")
    public Map<EnquiryStatus, Long> stats() {
        return service.stats();
    }

    @GetMapping("/{id}")
    public ContactEnquiryResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    public ContactEnquiryResponse update(@PathVariable Long id, @Valid @RequestBody UpdateEnquiryRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
