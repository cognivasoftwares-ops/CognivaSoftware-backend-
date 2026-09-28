package com.cogniva.backend.enquiry;

import com.cogniva.backend.common.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Public endpoint used by the website's Contact page. */
@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactEnquiryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactSubmissionResponse submit(@Valid @RequestBody ContactEnquiryRequest request,
                                            HttpServletRequest httpRequest) {
        return service.submit(request,
                ClientIpResolver.resolve(httpRequest),
                httpRequest.getHeader(HttpHeaders.USER_AGENT));
    }
}
