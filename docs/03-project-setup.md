# Project Setup Guide

## 1. Introduction

This guide explains how to set up and run the `mcq_platform_api` project locally.

The API provides:

- Random MCQ practice questions
- Timed practice sets
- JWT authentication
- Admin question management
- Result evaluation system

---

## 2. Prerequisites

Make sure the following are installed:

| Software | Version |
|----------|----------|
| Java | 17+ |
| Maven | 3.9+ |
| MySQL | 8+ |
| Git | Latest |

Verify installation:

```bash
java -version
mvn -version
git --version
```

---

## 3. Clone the Repository

Clone the project from GitHub:

```bash
git clone <your-repository-url>
cd mcq-platform-api
```

---

## 4. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE mcq_platform_db;
```

Open:

```txt
src/main/resources/application.properties
```

Configure database connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mcq_platform_db
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secret=your_jwt_secret

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Update credentials according to your local MySQL setup.

---

## 5. Install Dependencies

Install Maven dependencies:

```bash
mvn clean install
```

This will:

- Download dependencies
- Compile project
- Run tests
- Build the application

---

## 6. Run the Project

Start the Spring Boot server:

```bash
mvn spring-boot:run
```

Or run directly from IDE:

```txt
Run McqPlatformApiApplication.java
```

If successful:

```txt
Tomcat started on port 8080
```

API Base URL:

```txt
http://localhost:8080
```

---

## 7. Authentication Setup

Most protected endpoints require JWT authentication.

### Register User

**Endpoint**

```http
POST /auth/signup
```

Example Request:

```json
{
  "username": "john123",
  "password": "password123"
}
```

---

### Login User

**Endpoint**

```http
POST /auth/login
```

Example Request:

```json
{
  "username": "john123",
  "password": "password123"
}
```

Example Response:

```json
{
  "token": "jwt_token_here",
  "message": "Login successful",
  "Username": "john123"
}
```

Use this token in headers:

```txt
Token: Bearer YOUR_TOKEN
```

---

## 8. Quick API Usage

### Get Random Questions

Generate random questions using filters.

```http
GET /questions?subject=math&topic=arithmetic&limit=2
```


Example Response:

```json
{
  "sessionId": "88cb7f28-1904-4d44-8f05-79632135f9eb",
  "total": 2,
  "subject": "math",
  "topic": "arithmetic",
  "questions": [
    {
      "number": 1,
      "questionId": "1",
      "questionText": "What is 2 + 2?",
      "options": [
        {
          "label": "a",
          "optionText": "3"
        },
        {
          "label": "b",
          "optionText": "4"
        },
        {
          "label": "c",
          "optionText": "5"
        },
        {
          "label": "d",
          "optionText": "6"
        }
      ]
    },
    {
      "number": 2,
      "questionId": "2",
      "questionText": "What is 5 * 6?",
      "options": [
        {
          "label": "a",
          "optionText": "30"
        },
        {
          "label": "b",
          "optionText": "35"
        },
        {
          "label": "c",
          "optionText": "25"
        },
        {
          "label": "d",
          "optionText": "20"
        }
      ]
    }
  ]
}
```
---

### Get Question by ID

```http
GET /question/{id}
```

Example:

```http
GET /question/1
```
Response
```json
{
  "number": 1,
  "questionId": "1",
  "questionText": "What is 2 + 2?",
  "options": [
    {
      "label": "a",
      "optionText": "3"
    },
    {
      "label": "b",
      "optionText": "4"
    },
    {
      "label": "c",
      "optionText": "5"
    },
    {
      "label": "d",
      "optionText": "6"
    }
  ]
}
```

---

### Get Answer by Question ID

```http
GET /question/{id}/answer
```
Example
```http
GET /question/1/answer
```
Response
```json
{
  "questionId": "1",
  "correctOption": "b",
  "correctOptionText": "4"
}
```

---

### Get Session Answers

Retrieve answers using session ID.

```http
GET /questions/{sessionId}/answer
```
Example
```http
GET /questions/88cb7f28-1904-4d44-8f05-79632135f9eb/answer
```
Response
```json
{
  "answers": [
    {
      "questionId": "1",
      "correctOption": "b",
      "correctOptionText": "4"
    },
    {
      "questionId": "2",
      "correctOption": "a",
      "correctOptionText": "30"
    }
  ]
}
```
---

## 9. Practice Set APIs

### Start Practice Set

Generate timed practice session.

```http
POST /practice-set/start
```
Example
```http
http://localhost:8080/practice-set/start
```
Extra Perimeter
```json
{
    "time":"5"   
} 
```
Response
```json
{
  "time": "5",
  "questionListResponse": {
    "sessionId": "0dc07d35-f10e-49a3-8dc5-250ec78d07f5",
    "total": 3,
    "subject": "Mixed",
    "topic": "Mixed",
    "questions": [
      {
        "number": 1,
        "questionId": "1",
        "questionText": "What is 2 + 2?",
        "options": [
          {
            "label": "a",
            "optionText": "3"
          },
          {
            "label": "b",
            "optionText": "4"
          },
          {
            "label": "c",
            "optionText": "5"
          },
          {
            "label": "d",
            "optionText": "6"
          }
        ]
      },
      {
        "number": 2,
        "questionId": "2",
        "questionText": "What is 5 * 6?",
        "options": [
          {
            "label": "a",
            "optionText": "30"
          },
          {
            "label": "b",
            "optionText": "35"
          },
          {
            "label": "c",
            "optionText": "25"
          },
          {
            "label": "d",
            "optionText": "20"
          }
        ]
      },
      {
        "number": 3,
        "questionId": "3",
        "questionText": "Capital of France?",
        "options": [
          {
            "label": "a",
            "optionText": "Madrid"
          },
          {
            "label": "b",
            "optionText": "Paris"
          },
          {
            "label": "c",
            "optionText": "Rome"
          },
          {
            "label": "d",
            "optionText": "Berlin"
          }
        ]
      }
    ]
  }
}
```
Note: You can also add subject or topic or limit of question in JSON
---

### Save Practice Set

```http
POST /practice-set/{practiceSetId}/save
```
Example
```http
Post http://localhost:8080/practice-set/0dc07d35-f10e-49a3-8dc5-250ec78d07f5/save 
```
Response
```json
{
  "message":"Practice set saved successfully"
}
```

---

### Submit Answers

Users can submit answers multiple times within time limit.

Latest submitted answer is evaluated.

```http
POST /practice-set/{practiceSetId}/submit
```
Example
```http
Post http://localhost:8080/practice-set/0dc07d35-f10e-49a3-8dc5-250ec78d07f5/submit 
```
Request
```json
{
    "answers" : [
        {
            "questionId": "1",
            "selectedOption": "b"
        } ,
        {
            "questionId": "2",
            "selectedOption": "b"
        } ,
        {
            "questionId": "3",
            "selectedOption": "b"
        }
    ]
}
```
Response
```json
{
  "message":"3 answers submitted successfully"
}
```

---

### Get Result

Retrieve result anytime.

Result includes:

- Correct answers
- Incorrect answers
- Explanations
- Evaluation summary

```http
POST /practice-set/{practiceSetId}/result
```
Example
```http
Post http://localhost:8080/practice-set/0dc07d35-f10e-49a3-8dc5-250ec78d07f5/result
```

Response
```json
{
  "score": "2/3",
  "practiceSetResultOverview": [
    {
      "status": "Correct Answer",
      "questionText": "What is 2 + 2?",
      "correctOption": "b",
      "explanation": "+ operation will add numbers",
      "correctOptionText": "4"
    },
    {
      "status": "Wrong Answer",
      "questionText": "What is 5 * 6?",
      "correctOption": "a",
      "explanation": "+ operation will add number",
      "correctOptionText": "30"
    },
    {
      "status": "Correct Answer",
      "questionText": "Capital of France?",
      "correctOption": "b",
      "explanation": "it is a fact",
      "correctOptionText": "Paris"
    }
  ]
}
```

---

## 10. Admin APIs

Admin-only endpoints for question management.

### Add Questions


```http
POST /admin/question
```
Example 
```http
Post http://localhost:8080/admin/question
```
Json Request
```json
[
  {
    "subject": "Mathematics",
    "topic": "Algebra",
    "questionText": "What is the value of x in 2x + 3 = 7?",
    "options": [
      {
        "optionText": "1",
        "isCorrect": false
      },
      {
        "optionText": "2",
        "isCorrect": true
      },
      {
        "optionText": "3",
        "isCorrect": false
      },
      {
        "optionText": "4",
        "isCorrect": false
      }
    ],
    "explanation": "2x + 3 = 7 → 2x = 4 → x = 2"
  }
]
```
Response
```json
{
  "message":"1 Question Added Successfully!"
}
```

### Update Questions

```http
PUT /admin/question
```
Example
```http
PUT http://localhost:8080/admin/question
```
Request
```json
[
  {
    "questionId": "b981494e-3d84-426d-baa1-6f8f43dd92e4",
    "subject": "Mathematics",
    "topic": "Algebra",
    "questionText": "What is the value of x in 2x + 3 = 7?",
    "options": [
      {
        "optionText": "1",
        "isCorrect": false
      },
      {
        "optionText": "2",
        "isCorrect": true
      },
      {
        "optionText": "3",
        "isCorrect": false
      },
      {
        "optionText": "4",
        "isCorrect": false
      }
    ],
    "explanation": "If we put x = 2 , then we will get 7 on both side after evaluation"
  }
]
```
Response
```json
{"message":"Question Updated Successfully!"}
```

### Delete Question

```http
DELETE /admin/question/{questionId}
```
Example
```http
DELETE http://localhost:8080/admin/question/b981494e-3d84-426d-baa1-6f8f43dd92e4
```
Response
```json
{"message":"Question Deleted Successfully!"}
```

---

## 11. Testing

Run tests:

```bash
mvn test
```

The project uses:

- JUnit
- Mockito
- H2 Database (test environment)

---

## 12. Common Commands

Run project:

```bash
mvn spring-boot:run
```

Build project:

```bash
mvn clean install
```

Run tests:

```bash
mvn test
```

Clean build:

```bash
mvn clean
```