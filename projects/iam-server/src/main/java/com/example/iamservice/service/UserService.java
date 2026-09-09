package com.example.iamservice.service;

import com.example.iamservice.exception.ConflictException;
import com.example.iamservice.exception.NotFoundException;
import com.example.iamservice.exception.ForbiddenException;
import com.example.iamservice.repository.PermissionRepository;
import com.example.iamservice.repository.TokenRepository;
import com.example.iamservice.repository.UserRepository;
import com.example.iamservice.repository.domain.UserRow;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository,
                       PermissionRepository permissionRepository,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public UserRow create(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("User with email already exists: " + email);
        }
        UserRow user = new UserRow(
                UUID.randomUUID(),
                email,
                OffsetDateTime.now(),
                true,
                encoder.encode(password)
        );
        userRepository.insert(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<UserRow> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void delete(UUID userId, UUID requesterId) {
        UserRow user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        // Prevent deletion of last admin
        if (hasAdminPermission(user.id())) {
            long adminCount = userRepository.countAdmins();
            if (adminCount <= 1) {
                throw new ForbiddenException("Last admin user cannot be removed");
            }
        }

        // Delete user's tokens
        var tokens = tokenRepository.findByEmail(user.email());
        for (var t : tokens) {
            tokenRepository.deleteById(t.token());
        }

        userRepository.deleteById(userId);
    }

    @Transactional
    public void setActive(UUID userId, boolean active, UUID requesterId) {
        UserRow user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!active && hasAdminPermission(user.id())) {
            long adminCount = userRepository.countAdmins();
            if (adminCount <= 1) {
                throw new ForbiddenException("Last admin user cannot be deactivated");
            }
        }

        userRepository.updateActive(userId, active);
    }

    public UserRow getById(UUID userId) {
        UserRow user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public boolean hasAdminPermission(UUID userId) {
        List<String> perms = permissionRepository.getPermissionsForUser(userId);
        return perms.stream().anyMatch(p -> p.startsWith("iam.users."));
    }
}
