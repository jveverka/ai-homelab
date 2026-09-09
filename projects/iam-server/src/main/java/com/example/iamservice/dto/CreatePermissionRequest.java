package com.example.iamservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequest(
        @NotBlank String id,
        @NotBlank String description
) {}
