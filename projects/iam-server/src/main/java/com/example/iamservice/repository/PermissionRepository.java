package com.example.iamservice.repository;

import com.example.iamservice.repository.domain.PermissionRow;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.iamservice.repository.Tables.*;

@Repository
public class PermissionRepository {

    private final DSLContext dsl;

    public PermissionRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public PermissionRow findById(String id) {
        return dsl.selectFrom(PERMISSIONS)
                .where(PERM_ID.eq(id))
                .fetchOne(this::toRow);
    }

    public Optional<PermissionRow> findByName(String name) {
        return Optional.ofNullable(dsl.selectFrom(PERMISSIONS)
                .where(PERM_ID.eq(name))
                .fetchOne(this::toRow));
    }

    public PermissionRow insert(PermissionRow permission) {
        dsl.insertInto(PERMISSIONS)
                .set(PERM_ID, permission.id())
                .set(PERM_DESCRIPTION, permission.description())
                .execute();
        return permission;
    }

    public void deleteById(String id) {
        dsl.deleteFrom(PERMISSIONS)
                .where(PERM_ID.eq(id))
                .execute();
    }

    public List<PermissionRow> findAll() {
        return dsl.selectFrom(PERMISSIONS)
                .orderBy(PERM_ID.asc())
                .fetch(this::toRow);
    }

    // --- user-permission association ---

    public void assignPermission(UUID userId, String permissionId) {
        dsl.insertInto(USER_PERMISSIONS)
                .set(UP_USER_ID, userId)
                .set(UP_PERMISSION_ID, permissionId)
                .onConflictDoNothing()
                .execute();
    }

    public void unassignPermission(UUID userId, String permissionId) {
        dsl.deleteFrom(USER_PERMISSIONS)
                .where(UP_USER_ID.eq(userId).and(UP_PERMISSION_ID.eq(permissionId)))
                .execute();
    }

    public List<String> getPermissionsForUser(UUID userId) {
        return dsl.select(UP_PERMISSION_ID)
                .from(USER_PERMISSIONS)
                .where(UP_USER_ID.eq(userId))
                .orderBy(UP_PERMISSION_ID.asc())
                .fetchInto(String.class);
    }

    private PermissionRow toRow(org.jooq.Record record) {
        if (record == null) return null;
        return new PermissionRow(
                record.get(PERM_ID),
                record.get(PERM_DESCRIPTION)
        );
    }
}
