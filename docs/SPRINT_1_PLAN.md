# Sprint 1 Plan

## Project

Relationship-Aware Social Network

Sprint: 1

Duration: 1-2 Weeks

Goal:

Establish project foundation, authentication system, database migrations, user management, and relationship group management.

---

# Sprint Objective

At the end of Sprint 1, the application should support:

✅ User Registration

✅ User Login

✅ JWT Authentication

✅ User Profile APIs

✅ Relationship Group APIs

✅ PostgreSQL Integration

✅ Flyway Migrations

✅ Spring Security

---

# Sprint Deliverables

Modules included:

1. Infrastructure Setup
2. PostgreSQL Setup
3. Flyway Setup
4. Security Setup
5. JWT Authentication
6. User Module
7. Group Module

---

# 1. Infrastructure Setup

## Goal

Create base project structure.

---

## Tasks

### Create Git Repository

Repository:

```text
relationship-social-network
```

---

### Backend Project

Generate Spring Boot Project

Dependencies:

```text
Spring Web

Spring Security

Spring Data JPA

PostgreSQL Driver

Validation

Lombok

Flyway

Actuator
```

---

### Frontend Project

Generate Angular Project

Dependencies:

```text
Angular Material

Tailwind CSS
```

Frontend development not required in Sprint 1.

Only project creation.

---

# 2. PostgreSQL Setup

## Goal

Configure PostgreSQL locally.

---

## Tasks

Install PostgreSQL.

Create database:

```sql
CREATE DATABASE relationship_social_network;
```

Create user:

```sql
CREATE USER social_user
WITH PASSWORD 'social_password';
```

Grant permissions:

```sql
GRANT ALL PRIVILEGES
ON DATABASE relationship_social_network
TO social_user;
```

---

## Spring Configuration

application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/relationship_social_network
    username: social_user
    password: social_password
```

---

# 3. Flyway Setup

## Goal

Manage database schema using migrations.

---

## Folder Structure

```text
src/main/resources/db/migration
```

---

## Migration Files

Create:

```text
V1__create_users.sql

V2__create_user_groups.sql

V3__create_friendships.sql

V4__create_friend_group_mapping.sql

V5__create_followers.sql
```

Only first two migrations must be implemented during Sprint 1.

Remaining migrations can contain placeholders.

---

## Validation

Verify:

```text
flyway_schema_history
```

table created automatically.

---

# 4. Spring Security Setup

## Goal

Secure APIs using JWT.

---

## Security Components

Create package:

```text
security
```

Classes:

```text
SecurityConfig

JwtAuthenticationFilter

JwtTokenProvider

CustomUserDetailsService

UnauthorizedHandler
```

---

## Public APIs

```text
POST /api/auth/register

POST /api/auth/login
```

---

## Protected APIs

Everything else.

---

# 5. JWT Authentication

## Goal

Implement stateless authentication.

---

## Registration Flow

```text
User Registers
        |
Password Encoded
        |
User Saved
        |
Success Response
```

---

## Login Flow

```text
User Login
        |
Validate Credentials
        |
Generate JWT
        |
Return JWT
```

---

## JWT Payload

```json
{
  "userId": 1,
  "username": "pravin"
}
```

---

## Response Example

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer"
}
```

---

# 6. User Module

## Goal

Manage user information.

---

## Package

```text
user
```

---

## Components

```text
UserController

UserService

UserRepository

UserEntity

UserMapper

UserDto
```

---

## APIs

### Register User

```http
POST /api/auth/register
```

---

### Login

```http
POST /api/auth/login
```

---

### Get Current User

```http
GET /api/users/me
```

---

### Update Profile

```http
PUT /api/users/me
```

---

### Get User By Id

```http
GET /api/users/{id}
```

---

## Validations

Username:

```text
Required
Unique
3-50 Characters
```

Email:

```text
Required
Unique
Valid Format
```

Password:

```text
Minimum 8 Characters
```

---

# 7. Group Module

## Goal

Manage relationship groups.

---

## Package

```text
group
```

---

## Components

```text
GroupController

GroupService

GroupRepository

UserGroupEntity

GroupDto
```

---

## APIs

### Create Group

```http
POST /api/groups
```

Request:

```json
{
  "groupName": "Family",
  "description": "Family Members"
}
```

---

### Get Groups

```http
GET /api/groups
```

---

### Get Group

```http
GET /api/groups/{id}
```

---

### Update Group

```http
PUT /api/groups/{id}
```

---

### Delete Group

```http
DELETE /api/groups/{id}
```

---

## Rules

Only owner can:

* Update Group
* Delete Group

---

# Folder Structure

```text
com.pravin.social

auth

user

group

security

config

common

exception
```

---

# Definition Of Done

Sprint 1 is complete when:

✅ PostgreSQL Connected

✅ Flyway Working

✅ User Table Created

✅ User Group Table Created

✅ Registration Working

✅ Login Working

✅ JWT Working

✅ Protected Endpoints Working

✅ Group CRUD Working

✅ Postman Collection Created

---

# Out Of Scope

Not included in Sprint 1:

❌ Friend Requests

❌ Followers

❌ Posts

❌ Comments

❌ Likes

❌ Feed

❌ Chat

❌ Notifications

❌ AI Features

❌ Microservices

❌ Redis

❌ Kafka

❌ Elasticsearch

---

# Sprint 2 Preview

Modules:

* Friend Requests
* Friend Group Mapping
* Followers
* User Search

Target Outcome:

Users can connect with each other and organize relationships into groups.
