package com.example.iamservice.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TokenResponse(
        UUID token,
        OffsetDateTime expiresAt,
        List<String> permissions
) {}
