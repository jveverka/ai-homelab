# Data model

## Users
| field       | type         | constraints |
|-------------|--------------|-------------|
| id          | UUID         | PK  |
| email       | varchar(255) | NOT NULL, UNIQUE |
| createdAt   | timestamptz  | NOT NULL |
| active      | boolean      | NOT NULL |
| pwdhash     | varchar(1024) | NOT NULL |

## Permissions
| field          | type          | constraints |
|----------------|---------------|-------------|
| id             | varchar(256)  | PK  |
| description    | varchar(1024) | NOT NULL |

## Tokens
| field          | type          | constraints |
|----------------|---------------|-------------|
| token          | UUID  | PK  |
| email          | varchar(255) | NOT NULL |
| permissions    | text[] | NOT NULL |

Users 1:N Permissions
Users 1:N Tokens
