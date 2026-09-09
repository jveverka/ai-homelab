package com.example.iamservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record AssignPermissionRequest(
        UUID userUUID,
        @NotBlank String permissionId
) {}
