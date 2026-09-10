# Data model

Column names use `lower_snake_case`. Timestamps are `timestamptz`.
REST field names may differ from column names (e.g. `id` is exposed as `uuid`);
persistence entities are never exposed directly (see `ARCHITECTURE.md`).

## users
| field      | type          | constraints |
|------------|---------------|-------------|
| id         | UUID          | PK |
| email      | varchar(255)  | NOT NULL, UNIQUE |
| created_at | timestamptz   | NOT NULL |
| active     | boolean       | NOT NULL, DEFAULT true |
| pwd_hash   | varchar(1024) | NOT NULL |

## permissions
| field       | type          | constraints |
|-------------|---------------|-------------|
| id          | varchar(256)  | PK |
| description | varchar(1024) | NOT NULL |

The nine default permissions (AC-01) are seeded by Flyway and are treated as
immutable by the application (AC-03); they cannot be deleted.

## user_permissions
Join table for the many-to-many relationship between users and permissions.

| field         | type         | constraints |
|---------------|--------------|-------------|
| user_id       | UUID         | NOT NULL, FK -> users(id) ON DELETE CASCADE |
| permission_id | varchar(256) | NOT NULL, FK -> permissions(id) ON DELETE CASCADE |

PK (user_id, permission_id)

## tokens
| field       | type         | constraints |
|-------------|--------------|-------------|
| token       | UUID         | PK |
| user_id     | UUID         | NOT NULL, FK -> users(id) ON DELETE CASCADE |
| email       | varchar(255) | NOT NULL |
| permissions | text[]       | NOT NULL |
| created_at  | timestamptz  | NOT NULL |
| expires_at  | timestamptz  | NOT NULL |

`email` and `permissions` are denormalised snapshots taken when the token is
issued: `permissions` is the token scope (a subset of the user's permissions at
issue time). A token is invalid once `expires_at` is in the past or the row has
been deleted.

## Relationships
- users N:M permissions (via `user_permissions`)
- users 1:N tokens
