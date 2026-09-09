package com.example.iamservice.repository;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * jOOQ table and field constants for the IAM schema.
 */
public final class Tables {

    private Tables() {
        throw new UnsupportedOperationException("no instances");
    }

    // --- users ---
    public static final Table<?> USERS = DSL.table(DSL.name("users"));
    public static final Field<UUID> USR_ID = DSL.field(DSL.name("users", "id"), UUID.class);
    public static final Field<String> USR_EMAIL = DSL.field(DSL.name("users", "email"), String.class);
    public static final Field<OffsetDateTime> USR_CREATED_AT = DSL.field(DSL.name("users", "created_at"), OffsetDateTime.class);
    public static final Field<Boolean> USR_ACTIVE = DSL.field(DSL.name("users", "active"), Boolean.class);
    public static final Field<String> USR_PWDHASH = DSL.field(DSL.name("users", "pwdhash"), String.class);

    // --- permissions ---
    public static final Table<?> PERMISSIONS = DSL.table(DSL.name("permissions"));
    public static final Field<String> PERM_ID = DSL.field(DSL.name("permissions", "id"), String.class);
    public static final Field<String> PERM_DESCRIPTION = DSL.field(DSL.name("permissions", "description"), String.class);

    // --- user_permissions ---
    public static final Table<?> USER_PERMISSIONS = DSL.table(DSL.name("user_permissions"));
    public static final Field<UUID> UP_USER_ID = DSL.field(DSL.name("user_permissions", "user_id"), UUID.class);
    public static final Field<String> UP_PERMISSION_ID = DSL.field(DSL.name("user_permissions", "permission_id"), String.class);

    // --- tokens ---
    public static final Table<?> TOKENS = DSL.table(DSL.name("tokens"));
    public static final Field<UUID> TK_TOKEN = DSL.field(DSL.name("tokens", "token"), UUID.class);
    public static final Field<String> TK_EMAIL = DSL.field(DSL.name("tokens", "email"), String.class);
    public static final Field<String[]> TK_PERMISSIONS = DSL.field(DSL.name("tokens", "permissions"), String[].class);
    public static final Field<OffsetDateTime> TK_EXPIRES_AT = DSL.field(DSL.name("tokens", "expires_at"), OffsetDateTime.class);
}
