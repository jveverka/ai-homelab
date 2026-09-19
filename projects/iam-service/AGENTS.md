# AGENTS.md

## Goal

Implement the microservice described in:

- docs/REQUIREMENTS.md
- docs/ACCEPTANCE_CRITERIA.md
- docs/DATA_MODEL.md
- docs/REST_API.md
- docs/ARCHITECTURE.md
- docs/TESTING.md

All acceptance criteria must be satisfied.

## Technology

Use:

- Java 25
- Spring Boot 4.x.x (do not use Spring Boot 3.x.x)
- Spring Security
- Maven (do not use Maven Wrapper: `mvnw`)
- PostgreSQL
- Flyway
- jOOQ
- JUnit 5
- Testcontainers
- AssertJ

## Development rules

- Do not mock PostgreSQL.
- Integration tests must use Testcontainers.
- All database schema changes must use Flyway.
- REST API must match docs/REST_API.md exactly.
- Do not expose persistence entities through REST APIs.
- Use immutable DTOs / Java records where appropriate.
- Follow constructor injection.
- Do not use field injection.
- No Lombok.
- Enable strict compiler warnings where practical.
- Do not use H2

## Workflow

1. Read all files under docs/ before implementing.
2. Create an implementation plan.
3. Implement incrementally.
4. Run tests after each major change.
5. Run the complete build before declaring the task complete.
6. Fix all failing tests.
7. Verify every acceptance criterion explicitly.

## Definition of Done

The task is complete only when:

`mvn clean verify`

passes successfully.

Also verify each item in docs/ACCEPTANCE_CRITERIA.md.
Do not stop merely because the project compiles.
