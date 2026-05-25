# API Documentation

## 1. Introduction

This document describes the REST APIs available in the `mcq_platform_api` project.

The API supports:

- JWT authentication
- Random question generation
- Session-based practice
- Timed practice sets
- Answer submission
- Result evaluation
- Admin question management

Base URL:

```txt
http://localhost:8080
```

Response Format:

```txt
JSON
```

---

## 2. Authentication

Protected endpoints require JWT token.

Header Format:

```txt
Authorization: Bearer YOUR_TOKEN
```

---

# 3. Authentication APIs

## Signup User

### Endpoint

```http
POST /auth/signup
```

### Request Body

```json
{
  "username": "john123",
  "password": "password123"
}
```

### Success Response

```json
{
  "message": "Signup successful for user:",
  "data": "john123"
}
```

---

## Login User

### Endpoint

```http
POST /auth/login
```

### Request Body

```json
{
  "username": "john123",
  "password": "password123"
}
```

### Success Response

```json
{
  "token": "jwt_token_here",
  "message": "Login successful",
  "Username": "john123"
}
```

---

# 4. Question APIs

## Get Random Questions

Generate random questions using filters.

### Endpoint

```http
GET /questions
```

### Query Parameters

| Parameter | Required | Description |
|------------|----------|-------------|
| subject | No | Filter by subject |
| topic | No | Filter by topic |
| limit | No | Number of questions |

### Example Request

```http
GET /questions?subject=java&topic=oops&limit=10
```

### Success Response

```json
{
  "sessionId": "practice-session-id",
  "questions": [
    {
      "id": "question-id",
      "questionText": "What is polymorphism?",
      "subject": "java",
      "topic": "oops",
      "options": [
        {
          "id": "option-id",
          "optionText": "Option A"
        }
      ]
    }
  ]
}
```

### Notes

- Questions are randomly generated
- A `sessionId` is returned
- The same session can later become a practice set

---

## Get Question By ID

### Endpoint

```http
GET /question/{id}
```

### Example

```http
GET /question/question-id
```

### Success Response

```json
{
  "id": "question-id",
  "questionText": "What is Java?",
  "subject": "java",
  "topic": "basics"
}
```

---

## Get Answer By Question ID

### Endpoint

```http
GET /question/{id}/answer
```

### Example

```http
GET /question/question-id/answer
```

### Success Response

```json
{
  "questionId": "question-id",
  "correctAnswer": "Option B",
  "explanation": "Explanation text"
}
```

---

## Get Answers By Session ID

Retrieve answers for generated session questions.

### Endpoint

```http
GET /questions/{sessionId}/answer
```

### Example

```http
GET /questions/session-id/answer
```

### Success Response

```json
{
  "answers": [
    {
      "questionId": "question-id",
      "correctAnswer": "Option B"
    }
  ]
}
```

---

# 5. Practice Set APIs

## Start Practice Set

Generate timed practice session.

### Endpoint

```http
POST /practice-set/start
```

### Request Body

```json
{
  "subject": "java",
  "topic": "oops",
  "limit": 10,
  "timeLimit": 30
}
```

### Success Response

```json
{
  "practiceSetId": "practice-set-id",
  "questions": []
}
```

### Notes

- Timer is validated on backend
- Practice session is generated dynamically

---

## Start Saved Practice Set

Start an already saved practice set.

### Endpoint

```http
POST /practice-set/{practiceSetId}/start
```

### Example

```http
POST /practice-set/practice-set-id/start
```

---

## Save Practice Set

Save generated question session.

### Endpoint

```http
POST /practice-set/{practiceSetId}/save
```

### Example

```http
POST /practice-set/practice-set-id/save
```

### Success Response

```json
{
  "message": "Practice set saved successfully"
}
```

---

## Submit Practice Set Answers

Submit answers for evaluation.

### Endpoint

```http
POST /practice-set/{practiceSetId}/submit
```

### Request Body

```json
{
  "answers": [
    {
      "questionId": "question-id",
      "selectedOptionId": "option-id"
    }
  ]
}
```

### Success Response

```json
{
  "message": "10 answers submitted successfully"
}
```

### Notes

- Multiple submissions are allowed within time limit
- Latest submitted answer is used for evaluation

---

## Get Practice Set Result

Retrieve practice result.

### Endpoint

```http
POST /practice-set/{practiceSetId}/result
```

### Example

```http
POST /practice-set/practice-set-id/result
```

### Success Response

```json
{
  "score": 8,
  "totalQuestions": 10,
  "correctAnswers": 8,
  "incorrectAnswers": 2,
  "results": [
    {
      "questionId": "question-id",
      "correct": true,
      "explanation": "Explanation text"
    }
  ]
}
```

### Notes

- Result can be viewed anytime
- Result includes explanations
- Evaluation uses latest submitted answers

---

# 6. Admin APIs

Admin endpoints require:

```txt
ROLE_ADMIN
```

---

## Add Questions

### Endpoint

```http
POST /admin/question
```

### Request Body

```json
[
  {
    "questionText": "What is Java?",
    "subject": "java",
    "topic": "basics",
    "options": []
  }
]
```

### Success Response

```json
{
  "message": "Question Added Successfully!"
}
```

---

## Update Questions

### Endpoint

```http
PUT /admin/question
```

### Request Body

```json
[
  {
    "id": "question-id",
    "questionText": "Updated question"
  }
]
```

### Success Response

```json
{
  "message": "Question Updated Successfully!"
}
```

---

## Delete Question

### Endpoint

```http
DELETE /admin/question/{questionId}
```

### Example

```http
DELETE /admin/question/question-id
```

### Success Response

```json
{
  "message": "Question Deleted Successfully!"
}
```

---

# 7. Common HTTP Status Codes

| Status Code | Meaning |
|-------------|----------|
| 200 | Success |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Resource Not Found |
| 500 | Internal Server Error |

---

# 8. API Workflow Overview

## Practice Question Flow

```txt
Generate Questions
        ↓
Receive Session ID
        ↓
Attempt Questions
        ↓
Fetch Answers
```

---

## Practice Set Flow

```txt
Start Practice Set
        ↓
Backend Timer Starts
        ↓
Submit Answers
        ↓
Evaluation
        ↓
Get Result
```