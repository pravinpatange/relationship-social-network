# Database Design V1

## Project

Relationship-Aware Social Network

Version: 1.0

Database: PostgreSQL 17+

Author: Pravin Patange

Status: Approved for MVP Development

---

# 1. Database Principles

This database design supports:

* User Management
* Authentication
* Relationship Groups
* Friendships
* Followers
* Posts
* Post Visibility
* Likes
* Comments
* Chat

The design is normalized and optimized for future migration to microservices.

---

# 2. Naming Conventions

## Tables

Use plural names.

Examples:

```sql
users
posts
comments
likes
```

---

## Primary Keys

All tables use:

```sql
id BIGSERIAL PRIMARY KEY
```

---

## Audit Columns

All business tables should contain:

```sql
created_at TIMESTAMP NOT NULL

updated_at TIMESTAMP NOT NULL
```

---

# 3. Users

Stores platform users.

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(50) NOT NULL UNIQUE,

    email VARCHAR(100) NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    display_name VARCHAR(100),

    profile_picture_url TEXT,

    bio TEXT,

    location VARCHAR(100),

    website VARCHAR(255),

    account_type VARCHAR(20) NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);
```

---

## Account Types

```text
PUBLIC
PRIVATE
```

---

## Indexes

```sql
CREATE INDEX idx_users_username
ON users(username);

CREATE INDEX idx_users_email
ON users(email);
```

---

# 4. User Groups

User-created relationship groups.

```sql
CREATE TABLE user_groups (

    id BIGSERIAL PRIMARY KEY,

    owner_user_id BIGINT NOT NULL,

    group_name VARCHAR(100) NOT NULL,

    description TEXT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_group_owner
    FOREIGN KEY(owner_user_id)
    REFERENCES users(id)
);
```

---

## Example Groups

```text
Family
Corporate
Travel
Best Friend
College
Gym
```

---

## Indexes

```sql
CREATE INDEX idx_group_owner
ON user_groups(owner_user_id);
```

---

# 5. Friendships

Stores friend relationships.

```sql
CREATE TABLE friendships (

    id BIGSERIAL PRIMARY KEY,

    requester_id BIGINT NOT NULL,

    receiver_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_friend_requester
    FOREIGN KEY(requester_id)
    REFERENCES users(id),

    CONSTRAINT fk_friend_receiver
    FOREIGN KEY(receiver_id)
    REFERENCES users(id)
);
```

---

## Status Values

```text
PENDING
ACCEPTED
REJECTED
BLOCKED
```

---

## Unique Constraint

```sql
CREATE UNIQUE INDEX uk_friendship
ON friendships(requester_id, receiver_id);
```

---

# 6. Friend Group Mapping

Maps friends into relationship groups.

```sql
CREATE TABLE friend_group_mapping (

    id BIGSERIAL PRIMARY KEY,

    owner_user_id BIGINT NOT NULL,

    friend_user_id BIGINT NOT NULL,

    group_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_mapping_owner
    FOREIGN KEY(owner_user_id)
    REFERENCES users(id),

    CONSTRAINT fk_mapping_friend
    FOREIGN KEY(friend_user_id)
    REFERENCES users(id),

    CONSTRAINT fk_mapping_group
    FOREIGN KEY(group_id)
    REFERENCES user_groups(id)
);
```

---

## Example

```text
Amit -> Family

Amit -> Best Friend
```

---

## Indexes

```sql
CREATE INDEX idx_mapping_owner
ON friend_group_mapping(owner_user_id);

CREATE INDEX idx_mapping_friend
ON friend_group_mapping(friend_user_id);

CREATE INDEX idx_mapping_group
ON friend_group_mapping(group_id);
```

---

# 7. Followers

One-way relationships.

```sql
CREATE TABLE followers (

    id BIGSERIAL PRIMARY KEY,

    follower_user_id BIGINT NOT NULL,

    following_user_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_follower
    FOREIGN KEY(follower_user_id)
    REFERENCES users(id),

    CONSTRAINT fk_following
    FOREIGN KEY(following_user_id)
    REFERENCES users(id)
);
```

---

## Unique Constraint

```sql
CREATE UNIQUE INDEX uk_followers
ON followers(
    follower_user_id,
    following_user_id
);
```

---

# 8. Posts

Stores content.

```sql
CREATE TABLE posts (

    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    caption TEXT,

    media_url TEXT,

    visibility_type VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_post_user
    FOREIGN KEY(user_id)
    REFERENCES users(id)
);
```

---

## Visibility Types

```text
PRIVATE

SELECTED_GROUPS

ALL_CONNECTIONS

PUBLIC
```

---

## Indexes

```sql
CREATE INDEX idx_post_user
ON posts(user_id);

CREATE INDEX idx_post_created
ON posts(created_at DESC);

CREATE INDEX idx_post_visibility
ON posts(visibility_type);
```

---

# 9. Post Visibility Groups

Used only when:

```text
SELECTED_GROUPS
```

```sql
CREATE TABLE post_visibility_groups (

    id BIGSERIAL PRIMARY KEY,

    post_id BIGINT NOT NULL,

    group_id BIGINT NOT NULL,

    CONSTRAINT fk_post_visibility_post
    FOREIGN KEY(post_id)
    REFERENCES posts(id),

    CONSTRAINT fk_post_visibility_group
    FOREIGN KEY(group_id)
    REFERENCES user_groups(id)
);
```

---

# 10. Comments

```sql
CREATE TABLE comments (

    id BIGSERIAL PRIMARY KEY,

    post_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,

    comment_text TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_comment_post
    FOREIGN KEY(post_id)
    REFERENCES posts(id),

    CONSTRAINT fk_comment_user
    FOREIGN KEY(user_id)
    REFERENCES users(id)
);
```

---

## Indexes

```sql
CREATE INDEX idx_comment_post
ON comments(post_id);
```

---

# 11. Likes

```sql
CREATE TABLE likes (

    id BIGSERIAL PRIMARY KEY,

    post_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_like_post
    FOREIGN KEY(post_id)
    REFERENCES posts(id),

    CONSTRAINT fk_like_user
    FOREIGN KEY(user_id)
    REFERENCES users(id)
);
```

---

## Prevent Duplicate Likes

```sql
CREATE UNIQUE INDEX uk_post_like
ON likes(post_id, user_id);
```

---

# 12. Chat Rooms

Chat context.

```sql
CREATE TABLE chat_rooms (

    id BIGSERIAL PRIMARY KEY,

    user1_id BIGINT NOT NULL,

    user2_id BIGINT NOT NULL,

    context_group_id BIGINT,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_room_user1
    FOREIGN KEY(user1_id)
    REFERENCES users(id),

    CONSTRAINT fk_room_user2
    FOREIGN KEY(user2_id)
    REFERENCES users(id),

    CONSTRAINT fk_room_group
    FOREIGN KEY(context_group_id)
    REFERENCES user_groups(id)
);
```

---

# 13. Chat Messages

```sql
CREATE TABLE chat_messages (

    id BIGSERIAL PRIMARY KEY,

    room_id BIGINT NOT NULL,

    sender_id BIGINT NOT NULL,

    message TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_message_room
    FOREIGN KEY(room_id)
    REFERENCES chat_rooms(id),

    CONSTRAINT fk_message_sender
    FOREIGN KEY(sender_id)
    REFERENCES users(id)
);
```

---

# 14. Future Tables

Not part of MVP.

Future additions:

```text
notifications

stories

story_views

hashtags

post_tags

user_sessions

audit_logs

reports

ai_recommendations
```

---

# 15. Flyway Migration Strategy

Initial migration:

```text
V1__create_users.sql

V2__create_user_groups.sql

V3__create_friendships.sql

V4__create_friend_group_mapping.sql

V5__create_followers.sql

V6__create_posts.sql

V7__create_post_visibility_groups.sql

V8__create_comments.sql

V9__create_likes.sql

V10__create_chat_rooms.sql

V11__create_chat_messages.sql
```

---

# 16. MVP Database Summary

Tables:

```text
users

user_groups

friendships

friend_group_mapping

followers

posts

post_visibility_groups

comments

likes

chat_rooms

chat_messages
```

Total Tables: 11

Database: PostgreSQL

Architecture: Monolith Ready

Microservice Migration Ready
