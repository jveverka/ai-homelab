# Testing Strategy

## Objective

Tests must provide confidence that the service satisfies the functional requirements and acceptance criteria.

The complete test suite must execute through:

```bash
mvn clean verify
```

## Unit tests

Use:

- JUnit 5
- AssertJ
- Mockito only when useful

Unit tests should:

- execute quickly
- not start the Spring context unnecessarily
- test business logic independently
- cover important edge cases

Do not mock simple value objects.

Do not create mocks merely to increase test coverage.

## Integration tests

Integration tests must use:

```text
Testcontainers
```

with:

```text
PostgreSQL
```

Do not use:

- H2
- HSQLDB
- Derby
- mocked repositories

for persistence integration testing.

## Database integration

Integration tests must verify:

- Flyway migrations execute successfully
- repositories work against PostgreSQL
- database constraints behave correctly
- transactions behave correctly

The test database must start from an empty PostgreSQL instance.

Flyway must create the schema.

## REST integration tests

REST integration tests shall start the Spring Boot application.

Tests should verify the application through its HTTP API.

Important flows must include:

```text
HTTP request
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
PostgreSQL Testcontainer
```

Do not mock the repository in acceptance-level REST tests.

## Required scenarios

At minimum test:

### Users

- create users
- retrieve users (list and by id)
- retrieve nonexistent user by id (404)
- reject blank email, password
- reject duplicate email
- list users
- pagination
- sorting
- reject deletion of last admin user
- reject deactivation of last admin user
- activate user
- deactivate user
- deactivating / deleting a user invalidates that user's tokens

### Permissions

- create permission
- retrieve permissions
- remove permission
- assign / unassign permission to a user
- pagination
- minimal permission set cannot be removed
- removing or unassigning a permission invalidates tokens whose scope contains it

### Tokens
- issue token (login)
- issue token with an explicit scope
- reject a requested scope wider than the user's permissions
- validate token (introspect) returns uuid, email, expiresAt, permissions
- invalidate token (logout)
- reject expired token

## Error responses

Integration tests must verify that error responses:

- use `application/problem+json`
- contain the expected HTTP status
- do not expose stack traces
- do not expose SQL
- do not expose Java exception names

## Test isolation

Tests must not depend on execution order.

Each test must establish its own required data.

Tests must not rely on data created by another test.

## Testcontainers lifecycle

A PostgreSQL container may be shared between tests within the same test suite for performance.

Database state must nevertheless be isolated between tests.

## Maven lifecycle

Unit tests should execute during:

```text
test
```

Integration/acceptance tests may execute using Maven Failsafe during:

```text
integration-test
verify
```

Suggested naming:

```text
*Test.java
```

for unit tests and:

```text
*IT.java
```

for integration tests.

## Completion criterion

The project is considered test-complete only when:

```bash
mvn clean verify
```

returns exit code 0.