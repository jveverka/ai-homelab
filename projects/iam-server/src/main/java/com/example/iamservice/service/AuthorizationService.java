package com.example.iamservice.service;

import com.example.iamservice.exception.UnauthorizedException;
import com.example.iamservice.repository.TokenRepository;
import com.example.iamservice.repository.UserRepository;
import com.example.iamservice.repository.domain.TokenRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Validates tokens and checks permission access.
 * Used by controllers to enforce authorization.
 */
@Service
public class AuthorizationService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public AuthorizationService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Validates the token and returns the token data (permissions, user email).
     * Throws UnauthorizedException if the token is invalid or expired.
     */
    @Transactional(readOnly = true)
    public TokenData validateToken(String rawToken) {
        UUID token;
        try {
            token = UUID.fromString(rawToken);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid token format");
        }

        TokenRow row = tokenRepository.findById(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired token"));

        if (row.expiresAt().isBefore(OffsetDateTime.now())) {
            throw new UnauthorizedException("Token expired");
        }

        return new TokenData(token, row.email(), List.of(row.permissions()));
    }

    /**
     * Validates the token and checks that it carries the required permission.
     */
    @Transactional(readOnly = true)
    public TokenData validateTokenWithPermission(String rawToken, String requiredPermission) {
        TokenData data = validateToken(rawToken);
        if (!data.permissions().contains(requiredPermission)) {
            throw new UnauthorizedException("Missing required permission: " + requiredPermission);
        }
        return data;
    }

    /**
     * Returns the user UUID associated with the token (for admin check).
     */
    public UUID getUserUuid(TokenData tokenData) {
        return userRepository.findByEmail(tokenData.email())
                .map(u -> u.id())
                .orElse(null);
    }

    public record TokenData(UUID token, String email, List<String> permissions) {}
}
