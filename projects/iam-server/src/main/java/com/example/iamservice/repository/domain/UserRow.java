package com.example.iamservice.repository.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Database row representation of a user (persistence model).
 */
public record UserRow(
        UUID id,
        String email,
        OffsetDateTime createdAt,
        boolean active,
        String pwdhash
) {}
