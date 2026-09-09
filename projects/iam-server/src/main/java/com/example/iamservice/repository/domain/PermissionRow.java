package com.example.iamservice.repository.domain;

/**
 * Database row representation of a permission.
 */
public record PermissionRow(
        String id,
        String description
) {}
