package com.cogniva.backend.enquiry;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContactRateLimiterTest {

    @Test
    void allowsFiveThenBlocksPerKey() {
        ContactRateLimiter limiter = new ContactRateLimiter();
        for (int i = 0; i < 5; i++) {
            assertThat(limiter.tryAcquire("1.2.3.4")).isTrue();
        }
        assertThat(limiter.tryAcquire("1.2.3.4")).isFalse();
        assertThat(limiter.tryAcquire("5.6.7.8")).isTrue();
    }
}
