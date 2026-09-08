# Acceptance criteria

## AC-01 Default permissions
Given the database is empty
These default permissions will be created:
* iam.users.create
* iam.users.read
* iam.users.delete
* iam.users.setactive
* iam.permissions.create
* iam.permissions.read
* iam.permissions.delete
* iam.permissions.assign
* iam.permissions.unassign

## AC-02 Admin users
Given the database is empty
Default admin user is created, password is taken from environment variable DEFAULT_ADMIN_PWD and all permissions from AC-01 will be assigned to that user.
Last admin user cannot be removed from the system.

## AC-03 Default permissions
All permission listed in AC-01 are considered default minimum and can't be removed.

## AC-04 Get Access Token (login)
User can get access token in exchange for credentials: email and password.
User can specify token duration and scope. Token scope is set of permissions which is set of user's permissions or subset of user's permissions.

## AC-04 Validate Access Token (introspect)
User can validate token, in case token is valid response from server contains user UUID, email, expiration Date, permissions for the token.

## AC-04 Invalidate Access Token (logout)
User can invalidate token, token will be removed from database and cannot be used for any action.

## AC-05 Create users
Only admin user can create new users.

## AC-06 Delete users
Only admin user can delete users.

## AC-07 List users
Only admin user can list users.

## AC-08 Activate and Deactivate users
Only admin user can activate or deactivate users.

## AC-09 Create new permissions
Only admin user can add new permissions on the top of permissions listed in AC-01.

## AC-10 Remove new permissions
Only admin user can remove permissions. Permissions listed in AC-01 cannot be removed.
Issued tokens with removed permissions will be deleted from database (invalidated).