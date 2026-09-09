package com.example.iamservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String email,
        OffsetDateTime createdAt,
        boolean active
) {}
