# Requirements

## 1. Purpose
This is a simple Identity and Access Management (IAM) service.

## 2. Functional requirements

### FR-001 Tokens
An access token is an opaque, randomly generated UUID issued to a user in
exchange for valid credentials (email and password). A token carries a scope
(a subset of the user's permissions) and an expiry. Tokens are the only
credential accepted by the management endpoints. See AC-04, AC-05, AC-06.

### FR-002 User creation
A caller holding `iam.users.create` can create users with a unique email and a
password. New users are active on creation. On an empty database the service
seeds one default admin user from `DEFAULT_ADMIN_EMAIL` / `DEFAULT_ADMIN_PWD`.
See AC-02, AC-07.

### FR-003 User management
A caller holding the relevant permission can list users (`iam.users.read`),
retrieve a single user (`iam.users.read`), delete a user (`iam.users.delete`),
and activate or deactivate a user (`iam.users.setactive`). The last remaining
admin user cannot be deleted or deactivated. Deleting or deactivating a user
invalidates that user's tokens. See AC-08, AC-09, AC-10.

### FR-004 Permission management
The service seeds nine default permissions (AC-01) that cannot be removed
(AC-03). A caller holding `iam.permissions.create` can add further permissions;
a caller holding `iam.permissions.delete` can remove non-default permissions.
A caller holding `iam.permissions.assign` / `iam.permissions.unassign` can
grant or revoke a permission for a user. Removing a permission, or unassigning
it from a user, invalidates every affected token. See AC-11, AC-12.

### FR-005 Token life cycle
A token can be issued (login), validated (introspect), and invalidated
(logout). Introspection returns the user UUID, email, expiry, and the token's
permissions. A token stops being usable when it expires, when it is
invalidated, or when a permission in its scope is removed or unassigned.
Maximum token duration is 90 days.
See AC-04, AC-05, AC-06, AC-12.

## 3. Non-functional requirements
- Java 25, Spring Boot 4.x, Maven, PostgreSQL, Flyway, jOOQ.
- Schema changes only through Flyway; migrations run automatically on startup.
- All configuration supplied through environment variables.
- Errors returned as RFC 9457 Problem Details (`application/problem+json`).
- Packaged as a Docker image; a docker-compose file is provided for local runs.
- Actuator, OpenAPI / Swagger UI, and git commit info exposed.
- Passwords are stored in the database hashed, not in plaintext.
