package com.example.iamservice.controller;

import com.example.iamservice.dto.*;
import com.example.iamservice.repository.domain.UserRow;
import com.example.iamservice.service.AuthorizationService;
import com.example.iamservice.service.AuthorizationService.TokenData;
import com.example.iamservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthorizationService auth;

    public UserController(UserService userService, AuthorizationService auth) {
        this.userService = userService;
        this.auth = auth;
    }

    /**
     * GET /api/v1/users - List all users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers(@RequestHeader("Authorization") String token) {
        TokenData data = auth.validateTokenWithPermission(token, "iam.users.read");
        List<UserResponse> users = userService.getAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * POST /api/v1/users - Create a new user
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateUserRequest request) {
        TokenData data = auth.validateTokenWithPermission(token, "iam.users.create");
        UserRow user = userService.create(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
    }

    /**
     * DELETE /api/v1/users - Remove a user
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody DeleteUserRequest request) {
        TokenData data = auth.validateTokenWithPermission(token, "iam.users.delete");
        UUID requesterId = auth.getUserUuid(data);
        userService.delete(request.userUUID(), requesterId);
        return ResponseEntity.ok().build();
    }

    /**
     * PUT /api/v1/users - Activate/Deactivate a user
     */
    @PutMapping
    public ResponseEntity<Void> setActive(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody SetActiveRequest request) {
        TokenData data = auth.validateTokenWithPermission(token, "iam.users.setactive");
        UUID requesterId = auth.getUserUuid(data);
        userService.setActive(request.userUUID(), request.active(), requesterId);
        return ResponseEntity.ok().build();
    }

    private UserResponse toResponse(UserRow row) {
        return new UserResponse(row.id(), row.email(), row.createdAt(), row.active());
    }
}
