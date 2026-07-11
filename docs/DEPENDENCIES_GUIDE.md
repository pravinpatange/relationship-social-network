# Dependencies Guide

## Project

Relationship-Aware Social Network

Version: 1.0

Purpose:

This document explains every dependency used in the project, why it was selected, and where it is used.

---

# Sprint 1 Dependencies

## Spring Web

Dependency:

```xml
spring-boot-starter-web
```

Purpose:

Provides REST API capabilities.

Used For:

* Controllers
* Request Mapping
* Response Handling
* JSON Serialization

Examples:

```java
@RestController

@GetMapping

@PostMapping
```

Without This:

The application cannot expose REST APIs.

---

## Spring Security

Dependency:

```xml
spring-boot-starter-security
```

Purpose:

Provides authentication and authorization.

Used For:

* Login Security
* JWT Authentication
* Endpoint Protection
* Role-Based Access Control

Examples:

```java
SecurityFilterChain

AuthenticationManager

PasswordEncoder
```

Future Usage:

* OAuth2
* Google Login
* GitHub Login

---

## Spring Data JPA

Dependency:

```xml
spring-boot-starter-data-jpa
```

Purpose:

Provides ORM support using Hibernate.

Used For:

* Entity Mapping
* Repository Layer
* Database Operations

Examples:

```java
@Entity

JpaRepository

@ManyToOne

@OneToMany
```

Benefits:

* Reduces SQL boilerplate
* Automatic Query Generation
* Transaction Management

---

## PostgreSQL Driver

Dependency:

```xml
postgresql
```

Purpose:

Allows Java application to connect to PostgreSQL database.

Used For:

* Database Connectivity
* JDBC Communication

Without This:

Spring Boot cannot communicate with PostgreSQL.

---

## Validation

Dependency:

```xml
spring-boot-starter-validation
```

Purpose:

Validates incoming requests.

Used For:

* DTO Validation
* Request Validation

Examples:

```java
@NotNull

@NotBlank

@Email

@Size
```

Example:

```java
public class RegisterUserRequest {

    @Email
    private String email;

}
```

---

## Flyway Migration

Dependency:

```xml
flyway-core
```

Purpose:

Database version control.

Used For:

* Schema Creation
* Database Upgrades
* Production Deployments

Examples:

```text
V1__create_users.sql

V2__create_user_groups.sql
```

Benefits:

* No manual schema creation
* Consistent environments
* Version-controlled database changes

---

## Lombok

Dependency:

```xml
lombok
```

Purpose:

Reduces boilerplate code.

Used For:

```java
@Getter

@Setter

@Builder

@NoArgsConstructor

@AllArgsConstructor
```

Example:

Instead of:

```java
public String getName() {
   return name;
}
```

Lombok generates it automatically.

Benefits:

* Cleaner code
* Faster development

---

## Spring Boot Actuator

Dependency:

```xml
spring-boot-starter-actuator
```

Purpose:

Provides monitoring and operational endpoints.

Used For:

* Health Checks
* Metrics
* Application Monitoring

Examples:

```text
/actuator/health

/actuator/info

/actuator/metrics
```

Future Usage:

* Kubernetes Health Checks
* Prometheus Monitoring

---

# Future Dependencies

Not included in Sprint 1.

---

## Spring AI

Purpose:

* AI Assistant
* Relationship Suggestions
* Smart Search

---

## Redis

Purpose:

* Feed Caching
* Session Storage

---

## Kafka

Purpose:

* Event-Driven Architecture
* Notifications

---

## Elasticsearch

Purpose:

* User Search
* Post Search
* Hashtag Search

---

## WebSocket

Purpose:

* Real-Time Chat

---

## OAuth2 Client

Purpose:

* Google Login
* GitHub Login

---

# Dependency Philosophy

Only add dependencies when they are needed.

Avoid adding future dependencies before implementation.

Benefits:

* Faster startup
* Smaller build size
* Better maintainability
* Reduced security risks

Current Sprint Dependency Count:

8

Future dependencies will be added incrementally as the project evolves.
