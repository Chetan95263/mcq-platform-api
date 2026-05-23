# Project Architecture

## 1. Introduction

The `mcq_platform_api` follows a layered Spring Boot architecture designed for maintainability, scalability, and separation of concerns.

The application is structured around REST APIs, DTO-based communication, session-based practice generation, JWT authentication, and role-based authorization.

The architecture separates responsibilities into dedicated layers to keep business logic clean and easy to maintain.

---

## 2. High-Level Architecture

The project follows a layered request flow:

```txt
Client Request
      ↓
Controller Layer
      ↓
Service Layer
      ↓
Repository Layer
      ↓
Database
```

Each layer has a specific responsibility and avoids tightly coupled code.

---

## 3. Project Structure

```txt
src/main/java/com/example/mcq_platform_api
│── auth/
│── cache/
│── config/
│── controller/
│── dto/
│   ├── request/
│   └── response/
│── entities/
│── exception/
│── repository/
│── security/
│── service/
```

---

## 4. Layered Architecture

### Controller Layer

The controller layer handles incoming HTTP requests and returns API responses.

Responsibilities:

- Request handling
- Request parameter validation
- Calling service layer
- Returning HTTP responses

Main controllers:

```txt
QuestionController
AuthController
PracticeSetController
AdminController
```

Example Flow:

```txt
GET /questions
      ↓
QuestionController
      ↓
QuestionService
      ↓
Database
```

---

### Service Layer

The service layer contains business logic.

Responsibilities:

- Question generation
- Practice set management
- Answer submission
- Result evaluation
- Authentication logic

Examples:

```txt
QuestionService
PracticeSetService
UserService
```

This layer acts as the core engine of the application.

---

### Repository Layer

The repository layer handles database interaction using Spring Data JPA.

Responsibilities:

- CRUD operations
- Query execution
- Data retrieval

Repositories communicate directly with MySQL through Hibernate/JPA.

Example:

```txt
QuestionRepository
UserRepository
PracticeSetRepository
```

---

### DTO Layer

The project uses DTO architecture for cleaner API communication.

Structure:

```txt
dto/
├── request/
└── response/
```

Benefits:

- Clean request bodies
- Secure API responses
- Better separation from entities
- Prevents exposing internal database structure

Example:

```txt
LoginRequest
SignupRequest
PracticeStartRequest

QuestionResponse
PracticeSetResponse
AuthResponse
```

---

## 5. Authentication Architecture

The API uses JWT-based authentication with Spring Security.

Authentication Flow:

```txt
User Signup/Login
        ↓
JWT Token Generated
        ↓
Client Stores Token
        ↓
Authorization Header
        ↓
Protected API Access
```

Header format:

```txt
Authorization: Bearer JWT_TOKEN
```

### Role-Based Access

Two roles are implemented:

#### ROLE_USER

Permissions:

- Generate questions
- Start practice set
- Submit answers
- View results

#### ROLE_ADMIN

Additional permissions:

- Add questions
- Update questions
- Delete questions

Admin APIs are protected separately.

---

## 6. Session-Based Practice Architecture

The project uses a session-based question generation system.

Question generation flow:

```txt
Subject + Topic + Limit
            ↓
Random Questions Generated
            ↓
Session ID Created
            ↓
Questions Returned
```

The generated question list is temporarily associated with a `sessionId`.

This allows:

- Fast answer retrieval
- Session persistence
- Practice set creation

In the system:

```txt
sessionId ≈ practiceSetId
```

The same generated session can later be saved as a practice set.

---

## 7. Practice Set Architecture

Practice sets are an extension of random question generation.

Practice Set Flow:

```txt
Generate Questions
        ↓
Create Practice Set
        ↓
Backend Timer Starts
        ↓
User Submits Answers
        ↓
Evaluation Performed
        ↓
Result Generated
```

Key Features:

- Backend timer validation
- Multiple submissions allowed
- Latest submitted answer is used
- Result accessible anytime
- Explanation support for incorrect answers

The evaluation system checks correctness even after timer completion.

---

## 8. Database Entity Architecture

Core entities:

### User

Handles:

- Authentication
- Role management
- User registration

---

### Question

Stores:

- Question text
- Subject
- Topic
- Explanation

Relationship:

```txt
One Question → Many Options
```

---

### Option

Stores MCQ options for questions.

Each option belongs to a single question.

---

### PracticeSet

Represents a saved user practice session.

Stores:

- User ID
- Topic
- Subject
- Timestamp

---

### PracticeSetItem

Stores generated question collections inside a practice set.

Relationship:

```txt
PracticeSet
      ↓
PracticeSetItem
      ↓
Question List
```

---

## 9. Caching Architecture

The project currently uses manual in-memory caching.

Caching implementation:

```txt
ConcurrentHashMap
```

Purpose:

- Temporary answer storage
- Fast answer retrieval
- Session management

Current cache example:

```txt
AnswerListCache
```

This improves performance by avoiding unnecessary database calls.

Future plan:

```txt
Redis caching
```

for distributed and scalable cache management.

---

## 10. Request Lifecycle Example

Example request flow:

```txt
GET /questions?subject=java&topic=oops&limit=10
                ↓
QuestionController
                ↓
QuestionService
                ↓
QuestionRepository
                ↓
MySQL Database
                ↓
QuestionResponse DTO
                ↓
JSON Response
```

This architecture keeps business logic separated and improves maintainability.

---

## 11. Architecture Goals

The system architecture was designed to achieve:

- Clean code structure
- Separation of concerns
- Easy scalability
- Secure authentication
- Maintainable business logic
- Flexible MCQ practice system
- Better API response structure