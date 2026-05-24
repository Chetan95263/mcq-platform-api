# Database Design

## 1. Introduction

The `mcq_platform_api` uses a relational database design to manage questions, options, users, practice sessions, and authentication data.

The project uses **MySQL** as the primary database and **Spring Data JPA + Hibernate** for ORM (Object Relational Mapping).

The database structure is designed to support:

- Random question generation
- Subject and topic filtering
- Session-based practice
- Timed practice sets
- User authentication
- Result evaluation

---

## 2. Database Overview

The system currently contains the following core entities:

```txt
User
Question
Option
PracticeSet
PracticeSetItem
```

High-Level Relationship:

```txt
User
  ↓
PracticeSet
  ↓
PracticeSetItem
  ↓
Question
  ↓
Option
```

---

## 3. Entity Relationship Diagram (ER Diagram)

Add ER Diagram image here:

```md
![ER Diagram](diagrams/er-diagram.png)
```

Suggested Diagram:

```txt
User
 └── PracticeSet
        └── PracticeSetItem
                └── Question
                        └── Option
```

---

## 4. UUID-Based ID Strategy

The project uses **UUID String IDs** instead of auto-increment numeric IDs.

Example:

```java
UUID.randomUUID().toString()
```

Why UUID?

Benefits:

- Globally unique IDs
- Better security
- Prevents predictable sequential IDs
- Suitable for distributed systems

Example ID:

```txt
550e8400-e29b-41d4-a716-446655440000
```

---

## 5. Entity Details

### User Entity

The `User` entity manages authentication and authorization.

Stores:

- User ID
- Username
- Password (encoded)
- Role

Purpose:

- User registration
- Login authentication
- JWT authorization
- Role management

Roles:

```txt
ROLE_USER
ROLE_ADMIN
```

---

### Question Entity

The `Question` entity stores MCQ questions.

Fields:

| Field | Description |
|--------|-------------|
| id | Unique question ID |
| questionText | MCQ question text |
| subject | Question subject |
| topic | Question topic |
| explanation | Answer explanation |

Responsibilities:

- Random question generation
- Subject filtering
- Topic filtering
- Question retrieval

Relationship:

```txt
One Question → Many Options
```

Example:

```txt
Question
   ↓
Option A
Option B
Option C
Option D
```

---

### Option Entity

The `Option` entity stores answer choices for questions.

Responsibilities:

- Store MCQ options
- Maintain correct answer data
- Link answers with questions

Relationship:

```txt
Many Options → One Question
```

Each option belongs to exactly one question.

---

### PracticeSet Entity

The `PracticeSet` entity represents a user practice session.

Stores:

| Field | Description |
|--------|-------------|
| id | Practice set ID |
| userId | Owner of practice set |
| topic | Practice topic |
| subject | Practice subject |
| dateAndTime | Creation timestamp |

Purpose:

- Save generated question sessions
- Track practice attempts
- Enable result evaluation

Important Note:

```txt
practiceSetId ≈ sessionId
```

The generated question session and practice set share the same logical flow.

---

### PracticeSetItem Entity

The `PracticeSetItem` entity stores generated questions inside a practice session.

Relationship:

```txt
PracticeSet
      ↓
PracticeSetItem
      ↓
Question List
```

Purpose:

- Store generated question collections
- Connect questions with practice sessions
- Enable answer evaluation

This design allows users to save and reuse generated question sets.

---

## 6. Relationship Mapping

### Question ↔ Option

Relationship Type:

```txt
One-to-Many
```

JPA Mapping:

```txt
Question
@OneToMany
        ↓
Option
@ManyToOne
```

Meaning:

A single question can contain multiple options.

---

### PracticeSet ↔ PracticeSetItem

Relationship Type:

```txt
One-to-One
```

JPA Mapping:

```txt
PracticeSet
@OneToOne
        ↓
PracticeSetItem
```

Meaning:

Each practice set contains one question collection.

---

### PracticeSetItem ↔ Question

Relationship Type:

```txt
Many-to-Many
```

JPA Mapping:

```txt
PracticeSetItem
@ManyToMany
        ↓
Question
```

Meaning:

A practice set contains multiple questions.

Questions can also appear in multiple practice sets.

---

## 7. Database Workflow

### Question Generation Flow

```txt
Subject + Topic + Limit
            ↓
Random Questions Retrieved
            ↓
Session Created
            ↓
Questions Returned
```

---

### Practice Set Flow

```txt
Generate Questions
        ↓
Save Practice Set
        ↓
User Attempts Questions
        ↓
Submit Answers
        ↓
Evaluation
        ↓
Result Retrieval
```

---

## 8. Performance Considerations

The project includes:

### Session-Based Caching

Temporary answer sessions are stored using:

```txt
ConcurrentHashMap
```

Benefits:

- Faster answer retrieval
- Reduced database queries
- Improved response time

Future Improvement:

```txt
Redis Cache
```

for scalable distributed caching.

---

## 9. Future Database Improvements

Planned improvements:

- Redis caching
- Better indexing optimization
- Query optimization
- Audit tracking
- Practice analytics
- Performance monitoring