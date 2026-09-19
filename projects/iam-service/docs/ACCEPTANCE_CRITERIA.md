# Acceptance criteria

## Terminology
- **Required permission** — every management endpoint in `REST_API.md` lists the
  permission that a caller's token scope must contain. "Only an admin user can
  X" in the criteria below is shorthand for "the caller's token must contain the
  permission required by that endpoint".
- **Admin user** — a user who holds every default permission listed in AC-01.
- **Last admin user** — the only remaining admin user. The system must reject any
  operation (delete, deactivate, unassign a permission) that would leave the
  system with no admin user.

## AC-01 Default permissions
Given the database is empty, these default permissions are created:
* iam.users.create
* iam.users.read
* iam.users.delete
* iam.users.setactive
* iam.permissions.create
* iam.permissions.read
* iam.permissions.delete
* iam.permissions.assign
* iam.permissions.unassign

## AC-02 Default admin user
Given the database is empty, a default admin user is created. Its email is taken
from environment variable `DEFAULT_ADMIN_EMAIL` and its password from
`DEFAULT_ADMIN_PWD`. All permissions from AC-01 are assigned to it.
The last admin user cannot be removed from the system.

## AC-03 Default permissions are immutable
All permissions listed in AC-01 are the minimum permission set and cannot be
removed.

## AC-04 Get access token (login)
A user can obtain an access token in exchange for credentials: email and
password. The user may specify the token duration and scope. The scope is a set
of permissions equal to, or a subset of, the user's permissions. When no scope
is given, the token receives all of the user's current permissions.

## AC-05 Validate access token (introspect)
A user can validate a token. If the token is valid, the response contains the
user UUID, email, expiration timestamp, and the permissions carried by the
token.

## AC-06 Invalidate access token (logout)
A user can invalidate a token. The token is removed from the database and can no
longer be used for any action. The operation is idempotent.

## AC-07 Create users
Only an admin user can create new users. Email must be unique; new users are
created active.

## AC-08 Delete users
Only an admin user can delete users. The last admin user cannot be deleted.
Deleting a user invalidates that user's tokens.

## AC-09 List users
Only an admin user can list users and retrieve individual users. Listing
supports pagination and sorting.

## AC-10 Activate and deactivate users
Only an admin user can activate or deactivate users. The last admin user cannot
be deactivated. Deactivating a user invalidates that user's tokens.

## AC-11 Create permissions
Only an admin user can add new permissions on top of those listed in AC-01.

## AC-12 Remove permissions
Only an admin user can remove permissions. Permissions listed in AC-01 cannot be
removed. Removing a permission — or unassigning it from a user — invalidates
every issued token whose scope contains that permission.
