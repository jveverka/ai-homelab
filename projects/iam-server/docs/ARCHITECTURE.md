# Architecture

## Overview

The application is a conventional Spring Boot service.

Use a simple layered architecture within feature packages.

Conceptually:

```text
HTTP
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
PostgreSQL
```

## Package organization

Base package:

```text
com.example.iamservice
```

The exact class organization may differ if there is a clear reason.

## Controller responsibilities

Controllers are responsible for:

- HTTP mapping
- request validation
- DTO conversion where appropriate
- HTTP response codes

Controllers should not contain business logic.

## Service responsibilities

Services implement:

- business rules
- transaction boundaries
- coordination between repositories
- domain-level validation not expressible through Bean Validation

## Repository responsibilities

Repositories implement persistence operations.

Use JOOQ.

Do not expose repository objects directly through REST endpoints.

## DTOs

REST request and response models must be separate from persistence models.

Prefer Java records.

Example:

```java
public record CreatePermissionRequest(
        String id,
        String description
) {}
```

## Error handling

Use centralized exception handling.

Prefer:

```text
@RestControllerAdvice
```

Map domain/application errors to RFC 9457 Problem Details.

Do not scatter error response construction across controllers.

## Transactions

Use Spring transaction management.

Business operations involving multiple persistence operations must be atomic.

## Database migrations

Flyway owns the schema.

Application startup must execute pending migrations automatically.

## Dependency policy

Do not introduce a new dependency when equivalent functionality already exists in:

- Java standard library
- Spring Framework
- Spring Boot

Every additional dependency should have a concrete purpose.

## Deployment
Resulting microservice will be deployed as docker container. Dockerfile is required.
All configuration parameters must be set as environment variables.
Docker compose file is created for local testing.

## Monitoring and Documentation 
- Spring Boot actuator
- OpenAPI, Swagger UI
- Git commit version available via Spring Boot actuator

## Future compatibility

Do not prematurely implement:

- Kafka
- caching
- distributed locking
- event sourcing
- CQRS
- Redis
- Kubernetes configuration
- authentication

unless explicitly required by another specification.