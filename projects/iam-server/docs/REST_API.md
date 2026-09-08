# REST API 
Base path: /api/v1
Content type: application/json

## Access Token REST API

### Get Token (login)
POST /api/v1/tokens
Request:
{
"email": "user@gmail.com",
"password": "*******",
"duration": 3600
}
Success:
200
Example response:
{ "token": "8f028ba0-b506-49c8-a0c0-23f1f9f8b0fa", "expiresAt": "2026-09-07T12:10:00Z", "permissions": [  ] }

Errors:
* 401 invalid input

### Validate Token (introspect)
GET /api/v1/tokens
Authorization: {{token}}
Success:
200
Example response:
{ "token": "8f028ba0-b506-49c8-a0c0-23f1f9f8b0fa", "expiresAt": "2026-09-07T12:10:00Z", "permissions": [  ] }
Errors:
* 401 invalid token

### Invalidate Token (logout)
DELETE /api/v1/tokens
Authorization: {{token}}
Success:
200
This REST endpoint is idempotent, invalidating unknown token or invalidating same token more than once always results in http 200 response.

## Management REST API

### GET Permissions
GET /api/v1/permissions
Authorization: {{token}}
Required permissions: iam.permissions.read
Success:
200
Example response:
[ { "id": "iam.permissions.read", "description": "can read permissions" }, ... ]
Errors:
* 401 Unauthorized - in case user does not have required permissions.

### Add permission
POST /api/v1/permissions
Authorization: {{token}}
Request:
{ "userUUID": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "permissionId": "iam.permissions.read" }
Required permissions: iam.permissions.assign
Success:
201 Created
Errors:
* 404 Not Found in case userUUID does not exist.
* 401 Unauthorized - in case user does not have required permissions.

### Remove Permission
DELETE /api/v1/permissions
Authorization: {{token}}
Request:
{ "userUUID": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "permissionId": "iam.permissions.read" }
Required permissions: iam.permissions.unassign
Success:
200
Errors:
* 404 Not Found in case userUUID does not exist.
* 401 Unauthorized - in case user does not have required permissions.

### Get Users
GET /api/v1/users
Authorization: {{token}}
Required permissions: iam.users.read
Success:
200
Example response:
[ { "uuid": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", ... }, ... ]
Errors:
* 401 Unauthorized - in case user does not have required permissions.

### Create User
POST /api/v1/users
Authorization: {{token}}
Required permissions: iam.users.create

### Remove User
DELETE /api/v1/users
Authorization: {{token}}
Required permissions: iam.users.delete
Request:
{ "userUUID": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff" }
Success:
200
Errors:
* 404 Not Found in case userUUID does not exist.
* 401 Unauthorized - in case user does not have required permissions.
Last admin user in database can't be removed.

### Activate and Deactivate User
PUT /api/v1/users
Authorization: {{token}}
Required permissions: iam.users.setactive
Request:
{ "userUUID": "6c4f8c38-56cb-4854-89f2-cdc6c7ca7eff", "active": true }
Success:
200
Errors:
* 404 Not Found in case userUUID does not exist.
* 401 Unauthorized - in case user does not have required permissions.
  Last admin user in database can't be deactivated.
