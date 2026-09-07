# Data model

## User

| field       | type         | constraints |
|-------------|--------------|-------------|
| id          | UUID         | PK  |
| email       | varchar(255) | NOT NULL, UNIQUE |
| createdAt   | timestamptz  | NOT NULL |
| active      | boolean      | NOT NULL |
| pwdhash     | varchar(1024) | NOT NULL |


## Permission
| field          | type          | constraints |
|----------------|---------------|-------------|
| id             | varchar(256)  | PK  |
| description    | varchar(1024) | NOT NULL |

User 1:N Permission.
