package com.example.iamservice.initialization;

import com.example.iamservice.repository.PermissionRepository;
import com.example.iamservice.repository.UserRepository;
import com.example.iamservice.repository.domain.PermissionRow;
import com.example.iamservice.repository.domain.UserRow;
import com.example.iamservice.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Initializes the database with default permissions and admin user
 * on first boot (when the database is empty).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("${iam.admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${iam.admin.password:changeme123}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already initialized, skipping data initialization");
            return;
        }

        log.info("Initializing database with default permissions and admin user...");

        createDefaults();

        UserRow admin = new UserRow(
                UUID.randomUUID(),
                adminEmail,
                OffsetDateTime.now(),
                true,
                encoder.encode(adminPassword)
        );
        userRepository.insert(admin);

        for (String permId : PermissionService.DEFAULT_PERMISSIONS) {
            permissionRepository.assignPermission(admin.id(), permId);
        }

        log.info("Data initialization complete. Admin user: {}", adminEmail);
    }

    @Transactional
    private void createDefaults() {
        String[][] defaults = {
                {"iam.users.create", "Can create users"},
                {"iam.users.read", "Can read/list users"},
                {"iam.users.delete", "Can delete users"},
                {"iam.users.setactive", "Can activate/deactivate users"},
                {"iam.permissions.create", "Can create permissions"},
                {"iam.permissions.read", "Can read/list permissions"},
                {"iam.permissions.delete", "Can delete permissions"},
                {"iam.permissions.assign", "Can assign permissions to users"},
                {"iam.permissions.unassign", "Can unassign permissions from users"},
        };
        for (String[] d : defaults) {
            if (permissionRepository.findByName(d[0]).isEmpty()) {
                permissionRepository.insert(new PermissionRow(d[0], d[1]));
            }
        }
    }
}
