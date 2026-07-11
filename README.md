# Relationship-Aware Social Network

A next-generation social network that understands real-world relationships.

Unlike traditional social media platforms where all connections are treated equally, this platform allows users to organize relationships into meaningful groups such as Family, Best Friends, Corporate Connections, Travel Friends, and more.

Content, chats, feeds, notifications, and interactions are filtered based on relationship context, providing a more private and personalized social experience.

---

# Vision

People maintain multiple social identities in real life.

Examples:

* Family
* Best Friends
* Corporate Connections
* College Friends
* Travel Friends
* Hobby Communities

Current social networks force all of these relationships into a single feed.

This platform introduces:

* Relationship-Based Visibility
* Relationship-Aware Feeds
* Context-Specific Chats
* Mode-Based Experience
* Public Creator Support

The goal is to allow users to share content with the right audience without creating multiple social media accounts.

---

# Core Features

## Relationship Groups

Users can create custom groups.

Examples:

* Family
* Best Friend
* Corporate
* Travel
* Gym
* College

A connection may belong to multiple groups.

Example:

Amit

* Family
* Best Friend

---

## Visibility Control

Every post can have one of the following visibility levels:

### PRIVATE

Visible only to owner.

### SELECTED_GROUPS

Visible only to selected relationship groups.

### ALL_CONNECTIONS

Visible to all accepted friends.

### PUBLIC

Visible to:

* Friends
* Followers
* Visitors
* Search Results

Suitable for:

* Creators
* Influencers
* Businesses
* Public Figures

---

## Active Mode

The application supports mode switching.

Examples:

* All Mode
* Family Mode
* Corporate Mode
* Travel Mode
* Best Friend Mode

When Family Mode is active:

Visible:

* Family Feed
* Family Chats
* Family Notifications

Hidden:

* Corporate Content
* Travel Content
* Other Group Content

---

## Followers & Friends

### Friend

Mutual relationship.

```text
A <-> B
```

### Follower

One-way relationship.

```text
A -> B
```

Supports both private users and public creators.

---

## Feed System

Feed content is generated based on:

* Relationship Groups
* Visibility Rules
* Active Mode
* Public Content

---

# Architecture

## MVP Architecture

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

# Technology Stack

## Backend

* Java 21
* Spring Boot 3.x
* Spring Security
* Spring Data JPA
* Flyway
* Maven

---

## Frontend

* Angular
* Angular Material
* Tailwind CSS

---

## Database

* PostgreSQL

---

## Infrastructure

* Docker
* GitHub Actions (Future)

---

## Future Technologies

* Redis
* Kafka
* Elasticsearch
* Spring AI
* Kubernetes
* Prometheus
* Grafana

---

# Project Structure

```text
relationship-social-network

backend

frontend

docs

README.md
```

---

# Documentation

Project documentation is located in:

```text
docs/
```

Available documents:

```text
PRD.md

SYSTEM_DESIGN_V1.md

DATABASE_DESIGN_V1.md

SPRINT_1_PLAN.md

PROJECT_SETUP_GUIDE.md
```

---

# Local Setup

## Prerequisites

* Java 21
* Maven 3.9+
* PostgreSQL 17+
* Node.js 22+
* Angular CLI
* Docker Desktop

---

## Clone Repository

```bash
git clone https://github.com/<your-github-id>/relationship-social-network.git

cd relationship-social-network
```

---

## Start PostgreSQL

Using Docker:

```bash
docker run -d \
--name social-postgres \
-e POSTGRES_DB=relationship_social_network \
-e POSTGRES_USER=social_user \
-e POSTGRES_PASSWORD=social_password \
-p 5432:5432 \
postgres:17
```

---

## Run Backend

```bash
cd backend

mvn clean install

mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

---

## Run Frontend

```bash
cd frontend

npm install

ng serve
```

Frontend URL:

```text
http://localhost:4200
```

---

# MVP Scope

Version 1 Includes:

* User Registration
* User Login
* JWT Authentication
* User Profiles
* Group Management
* Friend Requests
* Followers
* Post Visibility Rules
* Feed Generation
* Active Mode Switching
* Comments
* Likes

---

# Future Roadmap

## Version 2

* WebSocket Chat
* Notifications
* Stories

---

## Version 3

* Redis
* Elasticsearch
* Kafka

---

## Version 4

* Spring AI
* Relationship Insights
* Smart Search
* Recommendation Engine

---

## Version 5

* Microservices
* Kubernetes
* Observability
* Horizontal Scaling

---

# Screenshots

Screenshots will be added as development progresses.

## Login Screen

Coming Soon

---

## Feed Screen

Coming Soon

---

## Group Management

Coming Soon

---

## Profile Page

Coming Soon

---

# License

This project is being developed for learning, experimentation, and portfolio purposes.

---

# Author

Pravin Patange

Senior Java / Spring Boot Developer

Building a relationship-aware social platform using modern backend architecture and scalable system design principles.
