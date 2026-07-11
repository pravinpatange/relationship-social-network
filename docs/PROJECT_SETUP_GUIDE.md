# Project Setup Guide

## Project

Relationship-Aware Social Network

Version: 1.0

Author: Pravin Patange

---

# 1. Overview

This document describes how to set up the local development environment for the Relationship-Aware Social Network project.

This guide should allow any developer to clone the repository and run the application locally.

---

# 2. Technology Stack

## Backend

* Java 21
* Spring Boot 3.x
* Spring Security
* Spring Data JPA
* Flyway
* PostgreSQL

---

## Frontend

* Angular 20
* Angular Material
* Tailwind CSS

---

## Database

* PostgreSQL 17+

---

## Build Tool

* Maven 3.9+

---

## IDE

* IntelliJ IDEA Ultimate (Recommended)
* IntelliJ IDEA Community Edition

---

## Version Control

* Git
* GitHub

---

# 3. Required Software

Install the following software before starting development.

| Software       | Version |
| -------------- | ------- |
| Java           | 21      |
| Maven          | 3.9+    |
| PostgreSQL     | 17+     |
| Git            | Latest  |
| Docker Desktop | Latest  |
| IntelliJ IDEA  | Latest  |
| Node.js        | 22 LTS  |
| Angular CLI    | Latest  |

---

# 4. Verify Installations

## Java

```bash
java -version
```

Expected:

```text
openjdk version "21"
```

---

## Maven

```bash
mvn -version
```

Expected:

```text
Apache Maven 3.9+
```

---

## PostgreSQL

```bash
psql --version
```

Expected:

```text
psql (PostgreSQL) 17+
```

---

## Docker

```bash
docker --version
```

Expected:

```text
Docker version xx.x.x
```

---

## Node

```bash
node -v
```

Expected:

```text
v22.x.x
```

---

## Angular

```bash
ng version
```

Expected:

```text
Angular CLI
```

---

# 5. Clone Repository

```bash
git clone https://github.com/<your-github-id>/relationship-social-network.git
```

```bash
cd relationship-social-network
```

---

# 6. Project Structure

```text
relationship-social-network

backend

frontend

docs
```

---

# 7. PostgreSQL Setup

## Create Database

Connect to PostgreSQL:

```bash
psql -U postgres
```

---

Create database:

```sql
CREATE DATABASE relationship_social_network;
```

---

Create application user:

```sql
CREATE USER social_user
WITH PASSWORD 'social_password';
```

---

Grant permissions:

```sql
GRANT ALL PRIVILEGES
ON DATABASE relationship_social_network
TO social_user;
```

---

Verify:

```sql
\l
```

Database should appear in list.

---

# 8. Docker PostgreSQL Setup (Alternative)

Instead of local installation:

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

Verify:

```bash
docker ps
```

Expected:

```text
social-postgres
```

running.

---

# 9. Backend Setup

Navigate:

```bash
cd backend
```

---

## Application Configuration

Create:

```text
application-local.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/relationship_social_network
    username: social_user
    password: social_password

  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true

server:
  port: 8080
```

---

## Maven Build

```bash
mvn clean install
```

Expected:

```text
BUILD SUCCESS
```

---

## Run Application

```bash
mvn spring-boot:run
```

Application:

```text
http://localhost:8080
```

---

# 10. Flyway Validation

On startup verify:

```text
flyway_schema_history
```

table created.

Verify in PostgreSQL:

```sql
SELECT *
FROM flyway_schema_history;
```

---

# 11. IntelliJ Setup

## Import Project

Open IntelliJ.

Select:

```text
Open Project
```

Choose:

```text
backend
```

folder.

---

## Configure SDK

Navigate:

```text
Project Structure
```

Set:

```text
SDK = Java 21
```

---

## Enable Annotation Processing

Navigate:

```text
Settings

Build

Compiler

Annotation Processors
```

Enable:

```text
Enable Annotation Processing
```

Required for:

```text
Lombok
MapStruct
```

---

## Install Plugins

Recommended:

```text
Lombok

SonarLint

GitToolBox

Database Tools

Spring Boot Assistant
```

---

# 12. Frontend Setup

Navigate:

```bash
cd frontend
```

---

Install dependencies:

```bash
npm install
```

---

Start Angular:

```bash
ng serve
```

Application:

```text
http://localhost:4200
```

---

# 13. Local URLs

Backend:

```text
http://localhost:8080
```

Frontend:

```text
http://localhost:4200
```

PostgreSQL:

```text
localhost:5432
```

---

# 14. Environment Profiles

Current Profiles:

```text
local
```

Future Profiles:

```text
dev

qa

uat

prod
```

---

# 15. Git Workflow

Create feature branch:

```bash
git checkout -b feature/user-registration
```

---

Commit:

```bash
git add .

git commit -m "Add user registration module"
```

---

Push:

```bash
git push origin feature/user-registration
```

---

# 16. Common Commands

## Build

```bash
mvn clean install
```

---

## Run Tests

```bash
mvn test
```

---

## Run Spring Boot

```bash
mvn spring-boot:run
```

---

## Start Angular

```bash
ng serve
```

---

## Start PostgreSQL Container

```bash
docker start social-postgres
```

---

## Stop PostgreSQL Container

```bash
docker stop social-postgres
```

---

# 17. Troubleshooting

## Port 8080 Already In Use

Find process:

```bash
netstat -ano | findstr :8080
```

Kill process or change application port.

---

## PostgreSQL Connection Failed

Verify:

```bash
docker ps
```

or

```bash
services.msc
```

Ensure PostgreSQL is running.

---

## Flyway Errors

Verify:

```text
Migration Naming Convention

V1__description.sql
```

Example:

```text
V1__create_users.sql
```

---

# 18. Sprint 1 Startup Checklist

Before coding ensure:

* Java Installed
* Maven Installed
* PostgreSQL Running
* Docker Running
* IntelliJ Configured
* Backend Builds Successfully
* Angular Starts Successfully
* Database Connection Works
* Flyway Executes Successfully

Once all checklist items are complete, development can begin with:

1. Flyway Migration Scripts
2. User Entity
3. User Registration
4. JWT Authentication
5. Group Management
