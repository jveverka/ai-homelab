# REST API 
Base path: /api/v1
Content type: application/json

## Get Token (login)
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

## Validate Token (introspect)
GET /api/v1/tokens
Authorization: {{token}}
Success:
200
Example response:
{ "token": "8f028ba0-b506-49c8-a0c0-23f1f9f8b0fa", "expiresAt": "2026-09-07T12:10:00Z", "permissions": [  ] }
Errors:
* 401 invalid token

## Invalidate Token (logout)
DELETE /api/v1/tokens
Authorization: {{token}}
Success:
200