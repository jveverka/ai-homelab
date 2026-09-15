# Simple IAM Service
Simple Identity Access Management Service

## Requirements

### Development requires:

Java 25
Docker
Maven, or the included Maven Wrapper

Docker is required for integration tests using Testcontainers.

## Build
Run: ``mvn clean verify``

This is the canonical command for verifying the complete project.

## REST API

The REST API is documented in:

docs/REST_API.md

## Specifications
Project requirements are maintained under:
docs/

## Important documents:

REQUIREMENTS.md — functional and non-functional requirements
DATA_MODEL.md — persistent data model
REST_API.md — HTTP interface
ARCHITECTURE.md — implementation constraints
TESTING.md — testing strategy
ACCEPTANCE_CRITERIA.md — definition of correct behavior

Coding agents must read:
AGENTS.md
before modifying the project.

The implementation is not considered complete until:
``mvn clean verify``
passes and all acceptance criteria have been verified.