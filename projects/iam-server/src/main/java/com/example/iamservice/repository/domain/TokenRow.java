package com.example.iamservice.repository.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Database row representation of a token.
 */
public record TokenRow(
        UUID token,
        String email,
        String[] permissions,
        OffsetDateTime expiresAt
) {}
