# Acceptance criteria

## AC-01 Default permissions
Given the database is empty
These default permissions will be created:
  iam.users.create
  iam.users.delete
  iam.users.activate
  iam.users.deactivate
  iam.permissions.create
  iam.permissions.remove
  iam.permissions.assign
  iam.permissions.unassign

## AC-02 Default admin user
Given the database is empty
Default admin user is created, password is taken from environment variable DEFAULT_ADMIN_PWD and all permissions from AC-01 will be assigned to that user.

## AC-03 Default permissions
All permission listed in AC-01 are considered default minimum and can't be removed.

## AC-04 Get Access Token (login)

## AC-04 Validate Access Token (introspect)

## AC-04 Invalidate Access Token (logout)

