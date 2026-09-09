package com.example.iamservice.service;

import com.example.iamservice.exception.ConflictException;
import com.example.iamservice.exception.NotFoundException;
import com.example.iamservice.repository.PermissionRepository;
import com.example.iamservice.repository.TokenRepository;
import com.example.iamservice.repository.UserRepository;
import com.example.iamservice.repository.domain.PermissionRow;
import com.example.iamservice.repository.domain.TokenRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PermissionService {

    public static final Set<String> DEFAULT_PERMISSIONS = Set.of(
            "iam.users.create",
            "iam.users.read",
            "iam.users.delete",
            "iam.users.setactive",
            "iam.permissions.create",
            "iam.permissions.read",
            "iam.permissions.delete",
            "iam.permissions.assign",
            "iam.permissions.unassign"
    );

    private final PermissionRepository permissionRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public PermissionService(PermissionRepository permissionRepository,
                             TokenRepository tokenRepository,
                             UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public List<PermissionRow> getAll() {
        return permissionRepository.findAll();
    }

    @Transactional
    public PermissionRow create(String id, String description) {
        if (permissionRepository.findByName(id).isPresent()) {
            throw new ConflictException("Permission already exists: " + id);
        }
        return permissionRepository.insert(new PermissionRow(id, description));
    }

    @Transactional
    public void delete(String permissionId) {
        if (DEFAULT_PERMISSIONS.contains(permissionId)) {
            throw new ConflictException("Default permission cannot be removed: " + permissionId);
        }
        PermissionRow perm = permissionRepository.findById(permissionId);
        if (perm == null) {
            throw new NotFoundException("Permission not found: " + permissionId);
        }
        permissionRepository.deleteById(permissionId);
        // Invalidate all tokens that carry the removed permission
        List<TokenRow> affected = tokenRepository.findByPermission(permissionId);
        for (TokenRow t : affected) {
            tokenRepository.deleteById(t.token());
        }
    }

    @Transactional
    public void assign(UUID userId, String permissionId) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        if (permissionRepository.findById(permissionId) == null) {
            throw new NotFoundException("Permission not found: " + permissionId);
        }
        permissionRepository.assignPermission(userId, permissionId);
    }

    @Transactional
    public void unassign(UUID userId, String permissionId) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        permissionRepository.unassignPermission(userId, permissionId);
    }

    public List<String> getPermissionsForUser(UUID userId) {
        return permissionRepository.getPermissionsForUser(userId);
    }

    public boolean isDefaultPermission(String permissionId) {
        return DEFAULT_PERMISSIONS.contains(permissionId);
    }
}
