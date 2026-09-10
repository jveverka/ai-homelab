# REST API
Base path: /api/v1
Content type: application/json (requests and success responses)
Error responses: application/problem+json (RFC 9457 Problem Details)

## Authentication and authorization

Endpoints marked with a required permission expect the opaque access token in
the `Authorization` header, sent as the raw token value (no `Bearer` prefix):

```
Authorization: 8f028ba0-b506-49c8-a0c0-23f1f9f8b0fa
```

- `401 Unauthorized` — the `Authorization` header is missing or malformed, or
  the token is unknown, expired, or already invalidated.
- `403 Forbidden` — the token is valid but its scope does not contain the
  permission required by the endpoint.

## Access Token REST API

### Get Token (login)
POST /api/v1/tokens
Request:
```
{
  "email": "user@gmail.com",
  "password": "*******",
  "duration": 3600,
  "scope": ["iam.users.read", "iam.users.create"]
}
```
* `duration` — token lifetime in seconds. Optional; the server applies
  `DEFAULT_TOKEN_DURATION_SECONDS` when omitted.
* `scope` — optional list of permission ids. Must be a subset of the user's
  current permissions. When omitted, the token receives all of the user's
  current permissions.

Success:
200
Example response:
```
{ "token": "8f028ba0-b506-49c8-a0c0-23f1f9f8b0fa", "expiresAt": "2026-09-07T12:10:00Z", "permissions": ["iam.users.read", "iam.users.create"] }
```

Errors:
* 400 malformed request, blank email or password, or `scope` outside the user's permissions
* 401 unknown email or wrong password

### Validate Token (introspect)
GET /api/v1/tokens
Authorization: {{token}}
Success:
200
Example response:
```
{ "uuid": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "email": "user@gmail.com", "token": "8f028ba0-b506-49c8-a0c0-23f1f9f8b0fa", "expiresAt": "2026-09-07T12:10:00Z", "permissions": ["iam.users.read"] }
```
Errors:
* 401 missing, unknown, expired, or invalidated token

### Invalidate Token (logout)
DELETE /api/v1/tokens
Authorization: {{token}}
Success:
200
This endpoint is idempotent: invalidating an unknown token, or invalidating the
same token more than once, always results in an HTTP 200 response.

## Permissions catalog REST API

### List permissions
GET /api/v1/permissions
Authorization: {{token}}
Required permission: iam.permissions.read
Supports pagination and sorting (see "Pagination and sorting").
Success:
200
Example response:
```
{ "content": [ { "id": "iam.permissions.read", "description": "can read permissions" } ], "page": 0, "size": 20, "totalElements": 9, "totalPages": 1 }
```
Errors:
* 403 caller lacks iam.permissions.read

### Create permission
POST /api/v1/permissions
Authorization: {{token}}
Required permission: iam.permissions.create
Request:
```
{ "id": "app.orders.read", "description": "can read orders" }
```
Success:
201 Created
Example response:
```
{ "id": "app.orders.read", "description": "can read orders" }
```
Errors:
* 400 blank id or description
* 409 a permission with this id already exists
* 403 caller lacks iam.permissions.create

### Delete permission
DELETE /api/v1/permissions/{id}
Authorization: {{token}}
Required permission: iam.permissions.delete
Success:
200
Behaviour:
* Default permissions (AC-01) cannot be deleted.
* Every issued token whose scope contains the deleted permission is invalidated
  (its row is removed from the database).
Errors:
* 404 no permission with this id
* 409 the id is a default permission (AC-01) and cannot be removed
* 403 caller lacks iam.permissions.delete

## User–permission assignment REST API

### Assign permission to user
POST /api/v1/users/{userUUID}/permissions
Authorization: {{token}}
Required permission: iam.permissions.assign
Request:
```
{ "permissionId": "iam.permissions.read" }
```
Success:
201 Created
Assigning a permission the user already holds is idempotent and returns 201.
Errors:
* 404 userUUID does not exist, or permissionId is not in the catalog
* 403 caller lacks iam.permissions.assign

### Unassign permission from user
DELETE /api/v1/users/{userUUID}/permissions/{permissionId}
Authorization: {{token}}
Required permission: iam.permissions.unassign
Success:
200
Behaviour:
* Every issued token for that user whose scope contains the permission is
  invalidated.
Errors:
* 404 userUUID does not exist
* 409 the operation would leave the system with no admin user
* 403 caller lacks iam.permissions.unassign

## Users REST API

### Get Users
GET /api/v1/users
Authorization: {{token}}
Required permission: iam.users.read
Supports pagination and sorting (see "Pagination and sorting").
Success:
200
Example response:
```
{ "content": [ { "uuid": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "email": "user@gmail.com", "active": true, "createdAt": "2026-09-07T12:00:00Z" } ], "page": 0, "size": 20, "totalElements": 1, "totalPages": 1 }
```
Errors:
* 403 caller lacks iam.users.read

### Get User by id
GET /api/v1/users/{uuid}
Authorization: {{token}}
Required permission: iam.users.read
Success:
200
Example response:
```
{ "uuid": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "email": "user@gmail.com", "active": true, "createdAt": "2026-09-07T12:00:00Z" }
```
Errors:
* 404 no user with this uuid
* 403 caller lacks iam.users.read

### Create User
POST /api/v1/users
Authorization: {{token}}
Required permission: iam.users.create
Request:
```
{ "email": "user@gmail.com", "password": "*******" }
```
Success:
201 Created
Example response:
```
{ "uuid": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "email": "user@gmail.com", "active": true, "createdAt": "2026-09-07T12:00:00Z" }
```
New users are created active.
Errors:
* 400 blank or invalid email, or blank password
* 409 a user with this email already exists
* 403 caller lacks iam.users.create

### Remove User
DELETE /api/v1/users
Authorization: {{token}}
Required permission: iam.users.delete
Request:
```
{ "userUUID": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff" }
```
Success:
200
Deleting a user invalidates all of that user's issued tokens.
Errors:
* 404 userUUID does not exist
* 409 userUUID is the last admin user and cannot be removed
* 403 caller lacks iam.users.delete

### Activate and Deactivate User
PUT /api/v1/users
Authorization: {{token}}
Required permission: iam.users.setactive
Request:
```
{ "userUUID": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "active": true }
```
Success:
200
Deactivating a user invalidates all of that user's issued tokens.
Errors:
* 404 userUUID does not exist
* 409 userUUID is the last admin user and cannot be deactivated
* 403 caller lacks iam.users.setactive

## Pagination and sorting

`GET /api/v1/users` and `GET /api/v1/permissions` accept:
* `page` — 0-based page index, default 0
* `size` — page size, default 20, maximum 100
* `sort` — `field,(asc|desc)`, e.g. `email,asc`; may be repeated

Response body:
```
{ "content": [ ... ], "page": 0, "size": 20, "totalElements": 0, "totalPages": 0 }
```
