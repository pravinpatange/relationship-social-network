# System Design V1

## Project

Relationship-Aware Social Network

Version: 1.0

Status: Initial Architecture

Author: Pravin Patange

---

# 1. Overview

This document describes the initial technical architecture for the Relationship-Aware Social Network.

The primary goal is to build a working MVP using a Spring Boot monolith and Angular frontend while keeping the design extensible for future migration to microservices.

---

# 2. Architecture Principles

## Current Phase

Monolithic Architecture

Advantages:

* Faster development
* Easier debugging
* Simpler deployment
* Reduced operational complexity

---

## Future Phase

Architecture will evolve toward:

* Microservices
* Kafka
* Redis
* Elasticsearch
* Kubernetes
* Spring AI

without major redesign.

---

# 3. High Level Architecture

```text
Angular Frontend
        |
        |
REST APIs
        |
        v

----------------------------------------
Spring Boot Monolith
----------------------------------------

Auth Module

User Module

Group Module

Friend Module

Follower Module

Post Module

Feed Module

Comment Module

Like Module

Chat Module

Notification Module

Mode Module

----------------------------------------

PostgreSQL Database
```

---

# 4. Core Business Concepts

The system is built around three concepts.

## Relationship Groups

User-defined categories.

Examples:

* Family
* Corporate
* Travel
* Best Friend
* Gym
* College

A friend may belong to multiple groups.

---

## Visibility Scope

Defines who can see content.

Types:

* PRIVATE
* SELECTED_GROUPS
* ALL_CONNECTIONS
* PUBLIC

---

## Active Mode

Defines current application context.

Examples:

* All Mode
* Family Mode
* Corporate Mode
* Travel Mode

Active mode filters:

* Feed
* Chats
* Notifications
* Friends
* Profile Views

---

# 5. Database Design

## users

Stores user information.

Columns:

* id
* username
* email
* password
* display_name
* profile_picture_url
* bio
* location
* website
* account_type
* created_at
* updated_at

---

## user_groups

Stores user-created groups.

Columns:

* id
* owner_user_id
* group_name
* description
* created_at

---

## friendships

Stores friend relationships.

Columns:

* id
* requester_id
* receiver_id
* status
* created_at

Status Values:

* PENDING
* ACCEPTED
* REJECTED
* BLOCKED

---

## friend_group_mapping

Maps friends to groups.

Columns:

* id
* owner_user_id
* friend_user_id
* group_id

Example:

Amit

* Family
* Best Friend

---

## followers

Stores follower relationships.

Columns:

* id
* follower_user_id
* following_user_id
* created_at

---

## posts

Stores post content.

Columns:

* id
* user_id
* caption
* media_url
* visibility_type
* created_at

Visibility Types:

* PRIVATE
* SELECTED_GROUPS
* ALL_CONNECTIONS
* PUBLIC

---

## post_visibility_groups

Stores groups allowed to view a post.

Columns:

* id
* post_id
* group_id

Only used for SELECTED_GROUPS visibility.

---

## comments

Stores post comments.

Columns:

* id
* post_id
* user_id
* comment_text
* created_at

---

## likes

Stores post likes.

Columns:

* id
* post_id
* user_id
* created_at

---

## user_modes

Stores currently active user mode.

Columns:

* user_id
* active_group_id
* active_mode

---

## chat_rooms

Stores chat context.

Columns:

* id
* user1_id
* user2_id
* context_group_id
* created_at

---

## chat_messages

Stores messages.

Columns:

* id
* room_id
* sender_id
* message
* created_at

---

# 6. Entity Relationship Overview

```text
USER
 |
 |------< USER_GROUP
 |
 |------< FRIENDSHIP
 |
 |------< FOLLOWER
 |
 |------< POST
 |
 |------< COMMENT
 |
 |------< LIKE
 |
 |------< CHAT_MESSAGE

USER_GROUP
 |
 |------< FRIEND_GROUP_MAPPING

POST
 |
 |------< POST_VISIBILITY_GROUP
 |
 |------< COMMENT
 |
 |------< LIKE

CHAT_ROOM
 |
 |------< CHAT_MESSAGE
```

---

# 7. Backend Package Structure

```text
com.pravin.social
|
+-- auth
|
+-- user
|
+-- group
|
+-- friend
|
+-- follower
|
+-- post
|
+-- feed
|
+-- comment
|
+-- like
|
+-- chat
|
+-- notification
|
+-- mode
|
+-- security
|
+-- config
|
+-- common
```

---

# 8. Layer Structure

Each module follows:

```text
controller

service

repository

entity

dto

mapper

validator
```

Example:

```text
post

PostController

PostService

PostRepository

PostEntity

PostMapper

CreatePostRequest

CreatePostResponse
```

---

# 9. Authentication Flow

## Registration

1. User submits registration request.
2. Password encrypted using BCrypt.
3. User record stored.
4. Success response returned.

---

## Login

1. User submits email and password.
2. Credentials validated.
3. JWT token generated.
4. Token returned.

---

## JWT Payload

```json
{
  "userId": 101,
  "username": "pravin",
  "roles": ["USER"]
}
```

---

# 10. API Contracts

## Authentication

```http
POST /api/auth/register

POST /api/auth/login

POST /api/auth/refresh

POST /api/auth/logout
```

---

## User APIs

```http
GET /api/users/me

PUT /api/users/me

GET /api/users/{id}
```

---

## Group APIs

```http
POST /api/groups

GET /api/groups

GET /api/groups/{id}

PUT /api/groups/{id}

DELETE /api/groups/{id}
```

---

## Friend APIs

```http
POST /api/friends/request

POST /api/friends/accept

POST /api/friends/reject

GET /api/friends
```

---

## Friend Group Assignment

```http
POST /api/friends/{friendId}/groups

GET /api/friends/{friendId}/groups
```

---

## Follower APIs

```http
POST /api/follow/{userId}

DELETE /api/follow/{userId}

GET /api/followers

GET /api/following
```

---

## Post APIs

```http
POST /api/posts

GET /api/posts/{id}

DELETE /api/posts/{id}
```

---

## Feed APIs

```http
GET /api/feed

GET /api/feed?family

GET /api/feed?groupId=10
```

---

## Comment APIs

```http
POST /api/comments

DELETE /api/comments/{id}
```

---

## Like APIs

```http
POST /api/likes

DELETE /api/likes
```

---

## Mode APIs

```http
POST /api/mode/change

GET /api/mode/current
```

---

# 11. Feed Generation Algorithm

Feed generation is the most important business function.

## All Mode

Steps:

1. Fetch all groups assigned to current user.
2. Fetch PUBLIC posts.
3. Fetch ALL_CONNECTIONS posts.
4. Fetch group-visible posts.
5. Merge results.
6. Sort by created_at descending.
7. Return paginated response.

---

## Family Mode

Steps:

1. Determine active group.
2. Fetch PUBLIC posts.
3. Fetch Family posts.
4. Exclude other groups.
5. Sort by created_at descending.

---

## Corporate Mode

Steps:

1. Determine active group.
2. Fetch PUBLIC posts.
3. Fetch Corporate posts.
4. Exclude all non-corporate groups.

---

# 12. Mode Switching Design

User selects a mode.

Example:

Family Mode

Frontend calls:

```http
POST /api/mode/change
```

Request:

```json
{
  "groupId": 5
}
```

Backend:

1. Validate group ownership.
2. Update user_modes table.
3. Store active mode.
4. Return success response.

All subsequent requests use active mode filtering.

---

# 13. Security Design

Security Components:

* Spring Security
* JWT Authentication
* BCrypt Password Encryption
* Stateless Sessions

Future:

* OAuth2 Login
* Two Factor Authentication
* Biometric Verification

---

# 14. Deployment Strategy

Development:

* Local Machine
* PostgreSQL

Initial Deployment:

* GitHub
* Docker

Future Deployment:

* AWS
* Kubernetes
* Load Balancers

---

# 15. Future Enhancements

Version 2:

* WebSocket Chat
* Notifications
* Stories

Version 3:

* Redis Cache
* Elasticsearch
* Kafka

Version 4:

* Spring AI
* Recommendation Engine
* Relationship Insights

Version 5:

* Microservices
* Kubernetes
* Observability Stack
