# Relationship-Aware Social Network (Working Name)

## Version

1.0

## Author

Pravin Patange

## Status

Planning Phase

---

# 1. Product Vision

Most social media platforms treat all relationships equally.

In reality, people maintain multiple identities and relationship contexts:

* Family
* Best Friends
* Corporate Connections
* College Friends
* Travel Friends
* Hobby Communities

Users frequently want to share content with one group while hiding it from another.

The purpose of this platform is to create a relationship-aware social network where content, chats, feeds, notifications, and interactions are filtered based on relationship groups selected by the user.

---

# 2. Product Goal

Allow users to:

1. Categorize connections into groups.
2. Share content with specific groups.
3. Switch application modes based on relationship context.
4. Filter feeds, chats, and profiles by active group.
5. Maintain privacy without creating multiple social media accounts.

---

# 3. Target Users

### Individual Users

People who want separation between:

* Family life
* Personal life
* Work life

### Professionals

People who want professional networking separated from personal content.

### Students

People who want separate visibility for:

* School Friends
* College Friends
* Family

---

# 4. Core Concept

Every connection belongs to one or more groups.

Example:

Amit belongs to:

* Family
* Best Friend

Rahul belongs to:

* Corporate

Neha belongs to:

* Family
* Travel Group

Posts, chats, notifications and feeds are controlled using these groups.

---

# 5. User Roles

## Regular User

Can:

* Register
* Login
* Create groups
* Manage friends
* Create posts
* Send messages
* Change active mode

## Administrator

Can:

* Manage reported content
* Manage users
* View platform analytics
* Moderate abuse

---

# 6. Authentication Module

## Registration

Fields:

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
* Google Login (Future)
* GitHub Login (Future)

---

## Session Management

* JWT Access Token
* Refresh Token
* Logout
* Token Expiry Handling

---

# 7. User Profile Module

## Profile Information

Fields:

* Profile Picture
* Username
* Name
* Bio
* Location
* Website
* Date Joined

---

## Profile Visibility

Options:

* Public
* Friends Only
* Private

---

# 8. Group Management Module

Users can create unlimited groups.

Examples:

* Family
* Corporate
* Best Friend
* Travel
* College
* Gym

---

## Create Group

Fields:

* Group Name
* Description (Optional)

---

## Edit Group

User can:

* Rename group
* Update description

---

## Delete Group

Rules:

* Group can be deleted only by owner.
* Associated mappings removed automatically.

---

# 9. Friendship Module

## Send Friend Request

User sends request.

Status:

* Pending
* Accepted
* Rejected
* Blocked

---

## Accept Friend Request

After accepting:

Friend must be assigned to at least one group.

Example:

Amit

Groups:

* Family
* Best Friend

---

## Group Assignment

One friend may belong to multiple groups.

Example:

Friend A

* Family
* Travel

Friend B

* Corporate

---

# 10. Mode Switching System

Unique platform feature.

User may switch active mode.

Options:

* All Mode
* Family Mode
* Corporate Mode
* Travel Mode
* Best Friend Mode

---

## Family Mode

Visible:

* Family posts
* Family chats
* Family notifications

Hidden:

* Corporate content
* Best friend content

---

## Corporate Mode

Visible:

* Corporate feed
* Corporate chats

Hidden:

* Family content

---

# 11. Post Module

## Create Post

Content Types:

* Text
* Image
* Video (Future)

Fields:

* Caption
* Media
* Visibility Selection

---

## Visibility Options

### Public

Anyone can see.

### Friends

All friends can see.

### Group-Based

Selected groups can see.

Example:

Visible To:

* Family
* Travel

---

## All Groups

All assigned groups can see.

---

# 12. Feed Module

Feed generated using:

* Public posts
* Visible friend posts
* Group visibility rules

---

## Feed Filters

Options:

* All
* Family
* Corporate
* Travel
* Public

---

# 13. Comment Module

Users can:

* Comment
* Edit Comment
* Delete Comment

Visibility follows post visibility.

Example:

If post visible only to Family.

Comments also visible only to Family.

---

# 14. Like Module

Users can:

* Like post
* Unlike post

Visibility follows post visibility.

---

# 15. Chat Module

Private Messaging.

---

## One-to-One Chat

Features:

* Send Message
* Edit Message
* Delete Message
* Read Receipts

---

## Group Visibility

Chat visibility follows group assignment.

Example:

Corporate Chat visible only in Corporate Mode.

---

## Future Features

* Voice Message
* Video Call
* File Sharing

---

# 16. Notification Module

Notifications:

* Friend Request
* Post Like
* Comment
* Mention
* Message

Filtered according to active mode.

---

# 17. Search Module

Search:

* Users
* Posts
* Groups
* Hashtags

---

# 18. Media Management

Storage:

* Images
* Videos

Future:

* AWS S3
* MinIO

---

# 19. Privacy Module

User Controls:

* Block User
* Unfriend User
* Private Account
* Hide Activity Status

---

# 20. Reporting Module

Users can report:

* Spam
* Abuse
* Fake Accounts

---

# 21. Future AI Features

## Relationship Classification

Suggest:

"This user appears to be a Family connection."

---

## Smart Search

Example:

"Show my family trip photos."

---

## Relationship Insights

Display:

* Most interacted group
* Most active friend
* Relationship score

---

# 22. Non-Functional Requirements

## Security

* Spring Security
* JWT
* Password Encryption
* Rate Limiting

---

## Scalability

Future support:

* Microservices
* Redis Cache
* Kafka
* Kubernetes

---

## Availability

Target:

99.9%

---

## Performance

Feed Load Time:

< 2 seconds

Profile Load Time:

< 1 second

---

# 23. Technology Stack

Backend:

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL

Frontend:

* Angular
* Angular Material
* Tailwind CSS

Infrastructure:

* Docker
* GitHub Actions

Future:

* Redis
* Kafka
* Elasticsearch
* Kubernetes
* Spring AI

---

# 24. MVP Scope (Version 1)

Must Have:

* Registration
* Login
* JWT
* User Profile
* Groups
* Friend Requests
* Group Assignment
* Create Post
* Visibility Rules
* Feed
* Mode Switching

Not Included:

* AI
* Kafka
* Redis
* Elasticsearch
* Microservices
* Video Upload
* Video Calls

These will be added in future releases.
