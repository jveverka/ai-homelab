package com.example.iamservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record SetActiveRequest(
        UUID userUUID,
        boolean active
) {}
