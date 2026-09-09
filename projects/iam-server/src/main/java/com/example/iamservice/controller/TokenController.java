package com.example.iamservice.controller;

import com.example.iamservice.dto.LoginRequest;
import com.example.iamservice.dto.TokenResponse;
import com.example.iamservice.service.AuthorizationService;
import com.example.iamservice.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController {

    private final TokenService tokenService;
    private final AuthorizationService authorizationService;

    public TokenController(TokenService tokenService, AuthorizationService authorizationService) {
        this.tokenService = tokenService;
        this.authorizationService = authorizationService;
    }

    /**
     * POST /api/v1/tokens - Login: exchange credentials for a token
     */
    @PostMapping
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = tokenService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/tokens - Introspect/validate a token
     */
    @GetMapping
    public ResponseEntity<TokenResponse> introspect(@RequestHeader("Authorization") String token) {
        TokenResponse response = tokenService.introspect(extractToken(token));
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/tokens - Invalidate/logout a token (idempotent)
     */
    @DeleteMapping
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        tokenService.invalidate(extractToken(token));
        return ResponseEntity.ok().build();
    }

    private UUID extractToken(String authHeader) {
        // Authorization header value is the raw token UUID
        String raw = authHeader.trim();
        if (raw.toLowerCase().startsWith("bearer ")) {
            raw = raw.substring(7).trim();
        }
        return UUID.fromString(raw);
    }
}
