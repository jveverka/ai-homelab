package com.example.iamservice.service;

import com.example.iamservice.dto.LoginRequest;
import com.example.iamservice.dto.TokenResponse;
import com.example.iamservice.exception.DomainException;
import com.example.iamservice.exception.NotFoundException;
import com.example.iamservice.exception.UnauthorizedException;
import com.example.iamservice.repository.TokenRepository;
import com.example.iamservice.repository.UserRepository;
import com.example.iamservice.repository.domain.TokenRow;
import com.example.iamservice.repository.domain.UserRow;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public TokenService(TokenRepository tokenRepository,
                        UserRepository userRepository,
                        PermissionService permissionService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        UserRow user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.active()) {
            throw new UnauthorizedException("User is deactivated");
        }

        if (!encoder.matches(request.password(), user.pwdhash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        List<String> userPermissions = permissionService.getPermissionsForUser(user.id());
        UUID token = UUID.randomUUID();
        OffsetDateTime expiresAt = OffsetDateTime.now().plusSeconds(request.duration());

        TokenRow tokenRow = new TokenRow(token, user.email(), userPermissions.toArray(new String[0]), expiresAt);
        tokenRepository.insert(tokenRow);

        return new TokenResponse(token, expiresAt, userPermissions);
    }

    @Transactional(readOnly = true)
    public TokenResponse introspect(UUID token) {
        TokenRow row = tokenRepository.findById(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired token"));

        if (row.expiresAt().isBefore(OffsetDateTime.now())) {
            tokenRepository.deleteById(token);
            throw new UnauthorizedException("Invalid or expired token");
        }

        List<String> permissions = List.of(row.permissions());
        return new TokenResponse(row.token(), row.expiresAt(), permissions);
    }

    @Transactional
    public void invalidate(UUID token) {
        // Idempotent: silently succeed even if token not found
        tokenRepository.deleteById(token);
    }
}
