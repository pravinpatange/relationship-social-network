# PRODUCT REQUIREMENTS DOCUMENT (PRD)

# Project Name

Relationship-Aware Social Network

Version: 1.0

Status: Planning

Author: Pravin Patange

---

# 1. Product Vision

Current social media platforms treat all relationships similarly.

In reality, people maintain multiple social identities and relationship contexts.

Examples:

* Family
* Best Friends
* Corporate Connections
* College Friends
* Travel Friends
* Hobby Communities
* School Friends
* Gym Friends
* Custom User Groups

People often want to:

* Share family photos only with family.
* Share office achievements only with colleagues.
* Share travel experiences only with travel friends.
* Keep personal and professional interactions separate.

The goal of this platform is to provide a relationship-aware social network where users control visibility, communication, and content based on relationship groups.

---

# 2. Product Goals

The platform should allow users to:

1. Organize connections into relationship groups.
2. Share content with selected groups.
3. Maintain separate social contexts.
4. Filter feed and chats by active group.
5. Support public content creators and influencers.
6. Improve privacy without creating multiple accounts.

---

# 3. Key Differentiator

Unlike Instagram, Facebook, or other social platforms:

The application operates using relationship-aware visibility.

The same person can belong to multiple groups.

Example:

Amit:

* Family
* Best Friend

Rahul:

* Corporate

Neha:

* Family
* Travel Friend

Content visibility is determined using these relationships.

---

# 4. Core Concepts

## 4.1 Relationship Groups

Groups are user-created categories.

Examples:

* Family
* Best Friend
* Corporate
* College
* Travel
* Gym
* Photography
* School Friends

Users can create unlimited custom groups.

---

## 4.2 Visibility Scopes

System-defined visibility options.

### PRIVATE

Visible only to owner.

### SELECTED_GROUPS

Visible only to chosen groups.

Example:

* Family
* Travel

### ALL_CONNECTIONS

Visible to all accepted friends regardless of group.

### PUBLIC

Visible to everyone.

Including:

* Friends
* Followers
* Profile Visitors
* Search Results
* Non-followers

Intended for:

* Influencers
* Creators
* Businesses
* Public Figures
* Regular Users who want public visibility

---

## 4.3 Active Mode

Unique feature of the platform.

Users may switch the application context.

Examples:

* All Mode
* Family Mode
* Corporate Mode
* Travel Mode
* Best Friend Mode
* Custom Group Mode

The active mode filters the visible content throughout the application.

---

# 5. User Types

## Regular User

Can:

* Create groups
* Add friends
* Create posts
* Chat
* Follow users
* Switch modes

---

## Creator / Influencer

Can:

* Maintain followers
* Create public content
* Build public audience

---

## Platform Administrator

Can:

* Moderate content
* Handle reports
* Manage users
* Review analytics

---

# 6. Authentication & Security

## Registration

Required Fields:

* Username
* Email
* Password
* Mobile Number

Validation:

* Unique username
* Unique email
* Strong password

---

## Login

Methods:

* Email + Password

Future:

* Google OAuth2
* GitHub OAuth2

---

## Security

* Spring Security
* JWT Access Token
* Refresh Token
* BCrypt Password Encryption

---

# 7. User Profile

Fields:

* Username
* Display Name
* Profile Picture
* Bio
* Location
* Website
* Joined Date

---

## Profile Visibility

Options:

* Public
* Friends Only
* Private

---

# 8. Group Management

Users can create unlimited groups.

Examples:

* Family
* Corporate
* Travel
* College
* Gym

Operations:

* Create Group
* Update Group
* Delete Group
* View Group Members

---

# 9. Friend Management

## Friend Request Flow

States:

* Pending
* Accepted
* Rejected
* Cancelled
* Blocked

---

## Group Assignment

Every accepted friend must belong to at least one group.

A friend may belong to multiple groups.

Example:

Amit:

* Family
* Best Friend

---

# 10. Followers

Followers are different from friends.

Friend:

Mutual relationship.

Follower:

One-way relationship.

Example:

User A follows User B.

User B does not need to follow back.

---

# 11. Posts

Users can create:

* Text Posts
* Image Posts

Future:

* Video Posts

---

## Post Fields

* Caption
* Media
* Location
* Tags
* Visibility

---

## Visibility Options

PRIVATE

SELECTED_GROUPS

ALL_CONNECTIONS

PUBLIC

---

# 12. Feed

Feed contains:

* Public Posts
* Friend Posts
* Group-Based Posts

Feed should only show content user is authorized to see.

---

## Feed Filters

* All
* Family
* Corporate
* Travel
* Best Friend
* Public

---

# 13. Mode-Based Experience

When user selects Family Mode:

Visible:

* Family Posts
* Family Chats
* Family Notifications
* Family Friends

Hidden:

* Corporate Content
* Travel Content
* Other Group Content

---

When user selects Corporate Mode:

Visible:

* Corporate Feed
* Corporate Chats
* Corporate Notifications

Hidden:

* Family Content

---

# 14. Comments

Users can:

* Create Comment
* Edit Comment
* Delete Comment

Comments inherit visibility from the parent post.

---

# 15. Likes

Users can:

* Like Post
* Remove Like

Likes inherit visibility from the parent post.

---

# 16. Chat System

## One-to-One Chat

Features:

* Send Message
* Edit Message
* Delete Message
* Read Receipt

---

## Group-Aware Chat Context

Users can chat within relationship contexts.

Example:

Amit belongs to:

* Family
* Best Friend

Messages sent in Family Context should only appear when Family Mode is active.

Messages sent in Best Friend Context should only appear when Best Friend Mode is active.

---

# 17. Notifications

Notifications include:

* Friend Requests
* Likes
* Comments
* Mentions
* Messages
* Follows

Notifications should respect active mode filters.

---

# 18. Search

Search Users

Search Posts

Search Groups

Search Hashtags

Future:

Natural Language Search using AI.

---

# 19. Privacy Features

Users can:

* Block User
* Unfriend User
* Remove Follower
* Hide Online Status
* Restrict User

---

# 20. Reporting & Moderation

Users can report:

* Spam
* Abuse
* Fake Accounts
* Harassment
* Inappropriate Content

---

# 21. Future AI Features

Relationship Classification

Suggested Group Assignment

Smart Search

Relationship Insights

Relationship Score

Content Recommendations

---

# 22. Non-Functional Requirements

Security:

* JWT
* Encryption
* Secure APIs

Performance:

* Feed Response < 2 Seconds

Availability:

* 99.9%

Scalability:

Future support for:

* Redis
* Kafka
* Elasticsearch
* Microservices
* Kubernetes
* Spring AI

---

# 23. MVP Scope

Included:

* Registration
* Login
* JWT Authentication
* User Profile
* Group Management
* Friend Requests
* Friend Group Assignment
* Followers
* Create Post
* Visibility Rules
* Feed
* Active Mode Switching
* Likes
* Comments

Excluded:

* AI Features
* Kafka
* Redis
* Elasticsearch
* Microservices
* Video Upload
* Video Calls

These will be implemented in later versions.
