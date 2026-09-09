-- V1__init_schema.sql
-- Initial IAM service schema

CREATE TABLE users (
    id          UUID          NOT NULL,
    email       VARCHAR(255)  NOT NULL,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now(),
    active      BOOLEAN       NOT NULL DEFAULT true,
    pwdhash     VARCHAR(1024) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE permissions (
    id          VARCHAR(256)  NOT NULL,
    description VARCHAR(1024) NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE user_permissions (
    user_id       UUID         NOT NULL,
    permission_id VARCHAR(256) NOT NULL,
    CONSTRAINT pk_user_permissions PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user_permissions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_permissions_perm FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);

CREATE TABLE tokens (
    token       UUID         NOT NULL,
    email       VARCHAR(255) NOT NULL,
    permissions TEXT[]       NOT NULL,
    expires_at  TIMESTAMPTZ  NOT NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (token)
);

CREATE INDEX idx_tokens_email ON tokens (email);
CREATE INDEX idx_tokens_expires_at ON tokens (expires_at);
