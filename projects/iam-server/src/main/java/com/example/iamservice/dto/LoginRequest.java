package com.example.iamservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        @Positive Integer duration
) {
    public LoginRequest {
        if (duration == null) duration = 3600;
    }
}
