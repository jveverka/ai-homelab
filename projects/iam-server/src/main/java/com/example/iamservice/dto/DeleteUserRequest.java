package com.example.iamservice.dto;

import java.util.UUID;

public record DeleteUserRequest(
        UUID userUUID
) {}
