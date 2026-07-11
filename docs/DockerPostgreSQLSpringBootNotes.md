# Docker + PostgreSQL + Spring Boot Notes

## 1. What is Docker?

Docker is a platform that allows applications to run inside isolated containers.

Without Docker:

Windows
├── PostgreSQL
├── Java
├── Redis

Everything is installed directly on the machine.

With Docker:

Windows
└── Docker Engine
├── PostgreSQL Container
├── Redis Container
└── Spring Boot Container

Each application runs independently.

---

## 2. Docker Terminology

### Image

An image is a blueprint/template.

Example:

docker pull postgres:17

Image = Java Class

### Container

A running instance of an image.

Example:

Image:
postgres:17

Container:
social-postgres

Container = Java Object

---

## 3. Why Use Docker?

Advantages:

* Same environment for every developer
* Easy setup
* Easy cleanup
* No local installation required
* Production-like environment

Example:

docker compose up -d

A new developer can start the database in seconds.

---

## 4. Docker Compose

Docker Compose lets us define infrastructure using YAML.

Instead of:

docker run ...
docker run ...
docker run ...

We create:

docker-compose.yml

and run:

docker compose up -d

---

## 5. Example docker-compose.yml

services:

postgres:
image: postgres:17

```
container_name: social-postgres

environment:
  POSTGRES_DB: relationship_social_network
  POSTGRES_USER: social_user
  POSTGRES_PASSWORD: social_password

ports:
  - "5432:5432"
```

---

## 6. Explanation of docker-compose.yml

### services

Defines containers to create.

services:
postgres:

Creates a PostgreSQL container.

---

### image

image: postgres:17

Downloads PostgreSQL version 17.

Equivalent:

docker pull postgres:17

---

### container_name

container_name: social-postgres

Assigns a fixed name to the container.

Without it Docker creates random names.

---

### environment

POSTGRES_DB: relationship_social_network

Creates database:

relationship_social_network

POSTGRES_USER: social_user

Creates user:

social_user

POSTGRES_PASSWORD: social_password

Creates password:

social_password

---

### ports

ports:

* "5432:5432"

Format:

HOST_PORT : CONTAINER_PORT

Meaning:

localhost:5432
|
V
Container Port 5432

Spring Boot connects to localhost:5432.

---

## 7. Docker Commands

### Download Image

docker pull postgres:17

---

### Start Containers

docker compose up -d

-d = Detached mode (run in background)

---

### Stop Containers

docker compose down

---

### View Running Containers

docker ps

---

### View All Containers

docker ps -a

---

### View Logs

docker logs social-postgres

---

### Open Container Shell

docker exec -it social-postgres bash

---

### Connect PostgreSQL

docker exec -it social-postgres psql -U social_user -d relationship_social_network

---

### Stop Container

docker stop social-postgres

---

### Start Container

docker start social-postgres

---

### Remove Container

docker rm social-postgres

---

## 8. PostgreSQL Commands

### List Databases

\l

---

### Connect Database

\c relationship_social_network

---

### List Tables

\dt

---

### Describe Table

\d users

---

### Exit PostgreSQL

\q

---

## 9. Flyway

Flyway is a database migration tool.

Instead of Hibernate creating tables automatically, we create SQL scripts.

Example:

V1__create_users.sql

---

## 10. Why Flyway?

Hibernate automatic schema generation:

spring.jpa.hibernate.ddl-auto=update

is fine for learning but risky in production.

Potential issues:

* Data loss
* Unexpected schema changes
* Difficult version control

Most companies use:

* Flyway
* Liquibase

---

## 11. Flyway Migration Example

File:

V1__create_users.sql

Example:

CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
username VARCHAR(100)
);

---

## 12. Flyway Schema History

Flyway creates:

flyway_schema_history

This table stores:

* Migration version
* Execution date
* Success status

Example:

Version 1
V1__create_users.sql

Executed only once.

---

## 13. Spring Boot Database Configuration

application.yml

spring:
datasource:
url: jdbc:postgresql://localhost:5432/relationship_social_network
username: social_user
password: social_password

flyway:
enabled: true

jpa:
hibernate:
ddl-auto: none

---

## 14. Why We Disabled ddl-auto

spring.jpa.hibernate.ddl-auto: none

Because Flyway manages schema creation.

Recommended Production Setup:

Flyway -> Database Schema
Hibernate -> ORM Mapping

---

## 15. Problem We Faced

Error:

FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"

Reason:

Old timezone value.

Fix:

-Duser.timezone=Asia/Kolkata

Added in IntelliJ VM Options.

---

## 16. Verify Migration

Connect to PostgreSQL:

docker exec -it social-postgres psql -U social_user -d relationship_social_network

Run:

\dt

Expected Output:

flyway_schema_history
users

---

## 17. Interview Concepts

Must Know:

1. Image
2. Container
3. Docker Compose
4. Port Mapping
5. Volumes
6. Flyway
7. PostgreSQL Basics
8. Spring Boot Database Configuration
9. Hibernate vs Flyway
10. Docker Networking

---

## 18. Future Architecture

Social Network Project

Docker
├── PostgreSQL
├── Spring Boot
├── Angular
├── Redis
└── RabbitMQ

Start Everything:

docker compose up -d

This is close to a real-world microservice environment.
