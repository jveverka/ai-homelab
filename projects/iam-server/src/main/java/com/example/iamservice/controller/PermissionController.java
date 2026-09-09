package com.example.iamservice.controller;

import com.example.iamservice.dto.*;
import com.example.iamservice.exception.ConflictException;
import com.example.iamservice.repository.domain.PermissionRow;
import com.example.iamservice.service.AuthorizationService;
import com.example.iamservice.service.AuthorizationService.TokenData;
import com.example.iamservice.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final AuthorizationService auth;

    public PermissionController(PermissionService permissionService, AuthorizationService auth) {
        this.permissionService = permissionService;
        this.auth = auth;
    }

    /**
     * GET /api/v1/permissions - List all permissions
     */
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> listPermissions(
            @RequestHeader("Authorization") String token) {
        auth.validateTokenWithPermission(token, "iam.permissions.read");
        List<PermissionResponse> perms = permissionService.getAll().stream()
                .map(p -> new PermissionResponse(p.id(), p.description()))
                .toList();
        return ResponseEntity.ok(perms);
    }

    /**
     * POST /api/v1/permissions - Assign permission to user
     */
    @PostMapping
    public ResponseEntity<Void> assignPermission(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AssignPermissionRequest request) {
        auth.validateTokenWithPermission(token, "iam.permissions.assign");
        permissionService.assign(request.userUUID(), request.permissionId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * DELETE /api/v1/permissions - Unassign permission from user
     */
    @DeleteMapping
    public ResponseEntity<Void> unassignPermission(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AssignPermissionRequest request) {
        auth.validateTokenWithPermission(token, "iam.permissions.unassign");
        if (permissionService.isDefaultPermission(request.permissionId())) {
            throw new ConflictException("Default permission cannot be removed: " + request.permissionId());
        }
        permissionService.unassign(request.userUUID(), request.permissionId());
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/v1/permissions/create - Create a new permission entity
     */
    @PostMapping("/create")
    public ResponseEntity<PermissionResponse> createPermission(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreatePermissionRequest request) {
        auth.validateTokenWithPermission(token, "iam.permissions.create");
        PermissionRow perm = permissionService.create(request.id(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PermissionResponse(perm.id(), perm.description()));
    }

    /**
     * DELETE /api/v1/permissions/{id} - Delete a permission entity from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        auth.validateTokenWithPermission(token, "iam.permissions.delete");
        permissionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
