# Private Chat Server

This microservice is very simple private chat server. 
When deployed, it can be used for simple text communications between connected users.

## 1. Database model
Use postresql relational database. Database name is chat-server.
Use UUIDs for every as unique identifier for every entity.

## 2. REST API
POST   /v1/tokens/login
GET    /v1/tokens/check
POST   /v1/tokens/logout
POST   /v1/users/{user-id}
PUT    /v1/users/{user-id}
GET    /v1/users
GET    /v1/users/myuser
POST   /v1/users/{user-id}/message
GET    /v1/groups
POST   /v1/groups/{group-id}
DELETE /v1/groups/{group-id}
POST   /v1/groups/{group-id}/message

## 3. Backend

## 4. Single page UI

## 5. Features

