# Technology Stack

## 1. Introduction

This document describes the technologies, frameworks, libraries, and architectural patterns used in the `mcq_platform_api` project.

The API is built using a modern Spring Boot backend architecture focused on secure authentication, session-based MCQ practice, timed practice sets, performance evaluation, and scalable API development.

---

## 2. Technology Overview

| Technology | Version | Purpose |
|------------|----------|----------|
| Java | 17 | Core programming language |
| Spring Boot | 3.2.5 | Backend framework |
| Spring Web | Included | REST API development |
| Spring Security | Included | Authentication & authorization |
| JWT | 0.11.5 | Token-based authentication |
| Spring Data JPA | Included | Database ORM |
| Hibernate | Included | Entity mapping |
| MySQL | Runtime | Primary database |
| H2 Database | Test Scope | Testing database |
| Maven | Build Tool | Dependency management |
| Lombok | Included | Boilerplate code reduction |
| JUnit | Test Scope | Unit testing |
| Mockito | Test Scope | Mocking in tests |

---

## 3. Backend Framework

### Spring Boot

The project uses Spring Boot 3.2.5 for backend development.

Spring Boot simplifies REST API development by providing:

- Auto configuration
- Embedded server support
- Dependency management
- Production-ready architecture
- Easy integration with Spring ecosystem

The framework is used to build scalable and maintainable backend services for MCQ practice and evaluation.

### Spring Web

Spring Web is used to create RESTful APIs.

Responsibilities include:

- HTTP request handling
- API endpoint creation
- JSON request/response processing
- Request mapping

Examples:

- Generate practice questions
- Create practice sets
- Submit answers
- Retrieve results

---

## 4. Security & Authentication

### Spring Security

Spring Security is used to secure API endpoints and protect user resources.

The project implements:

- Authentication
- Authorization
- Protected endpoints
- Role-based access control

### JWT Authentication

The API uses JWT (JSON Web Token) authentication.

Authentication flow:

```txt
User Login/Register
        ↓
JWT Token Generated
        ↓
Client Sends Token
        ↓
Protected API Access
```

### Role-Based Authorization

Two user roles are implemented:

#### ROLE_USER
Regular users can:

- Generate practice questions
- Attempt practice sets
- Submit answers
- View results

#### ROLE_ADMIN
Admin users have additional permissions:

- Add questions
- Update questions
- Delete questions
- Manage question database

Admin operations are handled through separate controllers.

---

## 5. Database Layer

### MySQL

MySQL is used as the primary relational database.

The database stores:

- Questions
- Options
- Practice Sets
- Practice Set Items
- Users

### JPA & Hibernate

Spring Data JPA and Hibernate are used for database interaction.

Benefits:

- Reduced SQL boilerplate
- Entity relationship mapping
- Repository abstraction
- Cleaner persistence layer

The project uses UUID-based string IDs:

```java
UUID.randomUUID().toString()
```

This improves uniqueness and avoids sequential ID exposure.

---

## 6. Entity Architecture

The core entities include:

### Question
Stores MCQ question information.

Contains:

- Question text
- Subject
- Topic
- Explanation
- Multiple options

Relationship:

```txt
One Question → Many Options
```

### Option
Stores answer options for questions.

Each option belongs to a single question.

### PracticeSet
Represents a timed user practice session.

Stores:

- User ID
- Topic
- Subject
- Date and time

### PracticeSetItem
Acts as a container for generated questions inside a practice set.

Relationship:

```txt
PracticeSet
      ↓
PracticeSetItem
      ↓
Question List
```

### User
Handles authentication and authorization.

Supports:

- Registration
- Login
- JWT-based authentication
- Role management

---

## 7. Session-Based Practice Architecture

The API follows a session-based question generation approach.

When a user requests random questions:

```txt
Topic + Subject + Size
          ↓
Question List Generated
          ↓
Session ID Created
```

The generated question list is temporarily stored and associated with a `sessionId`.

This same session can later be saved as a `practiceSet`.

In the system:

```txt
sessionId ≈ practiceSetId
```

This design allows:

- Reusable question sessions
- Practice persistence
- Result tracking
- Better scalability

---

## 8. Practice Set & Evaluation System

Practice sets are timed and validated on the backend.

Features include:

- Backend timer validation
- Multiple submissions within time limit
- Latest answer replacement
- Automatic evaluation
- Result retrieval anytime

Submission workflow:

```txt
Practice Set Started
        ↓
Timer Begins
        ↓
User Submits Answers
        ↓
Latest Answer Saved
        ↓
Result Generated
```

Result responses include:

- Correct answers
- Incorrect answers
- Explanations
- Performance summary

---

## 9. DTO Architecture

The project follows DTO (Data Transfer Object) architecture.

DTOs are separated into:

```txt
dto/
├── request/
└── response/
```

Benefits:

- Cleaner API responses
- Better request validation
- Hidden internal entity structure
- Improved security

This prevents exposing unnecessary database fields directly.

---

## 10. Caching Strategy

The project currently uses custom in-memory caching.

A manual cache system is implemented using:

```txt
ConcurrentHashMap
```

Current cache usage:

- Session-based answer storage
- Fast retrieval using sessionId
- Temporary answer management

Future improvement:

```txt
Redis-based distributed caching
```

is planned to replace manual caching for better scalability.

---

## 11. Testing

The project includes testing support using:

### JUnit
Used for:

- Unit testing
- Service testing
- Business logic verification

### Mockito
Used for:

- Mocking dependencies
- Isolated testing
- Repository mocking

Testing is partially implemented and will be expanded further for optimization and reliability.

---

## 12. Build & Dependency Management

### Maven

The project uses Maven for:

- Dependency management
- Project builds
- Plugin management
- Test execution

Common commands:

```bash
mvn clean install
mvn spring-boot:run
mvn test
```

---

## 13. Development Tools

Development tools used:

- VS Code
- Postman
- Git
- GitHub

These tools support development, testing, version control, and project collaboration.

---

## 14. Future Improvements

Planned improvements include:

- Redis caching
- Docker containerization
- Improved unit testing coverage
- Performance optimization
- Better caching strategy
- API scalability improvements