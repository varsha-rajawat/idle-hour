# Idle Hour

A modern full-stack productivity platform for managing tasks, priorities, and workflows with AI-assisted insights.

Idle Hour combines secure authentication, collaborative task management, intelligent AI suggestions, and a scalable enterprise-ready architecture into a clean developer-focused application.

---

## Preview

### Core Highlights

* Secure JWT authentication with rotating refresh tokens
* Role-based access control (`USER` and `ADMIN`)
* AI-powered task summaries and priority suggestions using GPT-4o
* Dynamic filtering, searching, sorting, and pagination
* Fully containerized development setup with Docker Compose
* CI pipeline using GitHub Actions
* Production-style backend architecture with DTOs, Flyway migrations, Specifications, and layered services

---

# Tech Stack

## Backend

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* Flyway
* JWT Authentication
* OpenAPI / Swagger
* JUnit 5 + Mockito

## Frontend

* React 18
* TypeScript
* Redux Toolkit
* React Router
* Axios
* Tailwind CSS
* Jest + React Testing Library

## AI Integration

* OpenAI GPT-4o

## DevOps

* Docker
* Docker Compose
* GitHub Actions CI

---

# Features

## Authentication & Security

* JWT access token authentication
* Rotating refresh token implementation
* Secure logout flow
* Role-based authorization
* Method-level security
* Password encryption using BCrypt
* Protected frontend routes

---

## Task Management

* Create tasks
* Edit tasks
* Delete tasks
* Update task status
* Filter by:

  * status
  * priority
  * assignee
  * keyword search
* Pagination and sorting support
* Dashboard statistics endpoint

---

## AI Features

### AI Task Summary

Generate concise summaries for lengthy task descriptions.

### AI Priority Suggestion

Analyze task content and automatically suggest:

* LOW
* MEDIUM
* HIGH

priority levels using GPT-4o.

---

# Architecture Overview

The project follows a production-style layered architecture.

```text
Client (React + Redux)
        ↓
REST API Layer (Controllers)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (JPA)
        ↓
PostgreSQL Database
```

Backend responsibilities are cleanly separated using:

* DTOs
* Services
* Specifications
* Exception handlers
* Security filters
* Repository interfaces

---

# Project Structure

```text
idle-hour/
├── backend/
│   ├── src/main/java/com/varsha/taskmanager/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── specification/
│   │
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/
│   │
│   └── src/test/
│
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── routes/
│   │   ├── store/
│   │   ├── types/
│   │   └── __tests__/
│
├── docker-compose.yml
├── .github/workflows/ci.yml
└── README.md
```

---

# Getting Started

## Prerequisites

Install:

* Docker Desktop
* Java 17
* Node.js 20+
* Maven
* PostgreSQL (optional if using Docker)

You also need an OpenAI API key if you want AI features enabled.

---

# Run With Docker (Recommended)

```bash
# Clone repository
git clone https://github.com/varsharajawat/idle-hour.git

# Enter project
cd idle-hour

# Optional: add OpenAI key
export OPENAI_API_KEY=sk-...

# Start application
docker compose up --build
```

---

# Local URLs

| Service    | URL                                                                            |
| ---------- | ------------------------------------------------------------------------------ |
| Frontend   | [http://localhost:3000](http://localhost:3000)                                 |
| Backend    | [http://localhost:8080](http://localhost:8080)                                 |
| Swagger UI | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| PostgreSQL | localhost:5432                                                                 |

---

# Run Without Docker

## Backend

```bash
cd backend

mvn spring-boot:run
```

Default database configuration:

```text
host=localhost
port=5432
database=taskmanager
username=admin
password=password
```

Flyway migrations run automatically during startup.

---

## Frontend

```bash
cd frontend

npm install
npm run dev
```

The Vite dev server proxies `/api/*` requests to the backend.

---

# API Overview

All protected endpoints require:

```http
Authorization: Bearer <access_token>
```

---

## Authentication Endpoints

| Method | Endpoint             | Description          |
| ------ | -------------------- | -------------------- |
| POST   | `/api/auth/register` | Register user        |
| POST   | `/api/auth/login`    | Login user           |
| POST   | `/api/auth/refresh`  | Refresh access token |
| POST   | `/api/auth/logout`   | Logout user          |

---

## Task Endpoints

| Method | Endpoint                           | Description            |
| ------ | ---------------------------------- | ---------------------- |
| GET    | `/api/tasks`                       | Get tasks with filters |
| POST   | `/api/tasks`                       | Create task            |
| GET    | `/api/tasks/{id}`                  | Get task by ID         |
| PUT    | `/api/tasks/{id}`                  | Update task            |
| PATCH  | `/api/tasks/{id}/status`           | Update task status     |
| DELETE | `/api/tasks/{id}`                  | Delete task            |
| GET    | `/api/tasks/dashboard-stats`       | Dashboard analytics    |
| POST   | `/api/tasks/{id}/summarize`        | AI task summary        |
| POST   | `/api/tasks/{id}/suggest-priority` | AI priority suggestion |

---

## User Endpoints

| Method | Endpoint        | Access              |
| ------ | --------------- | ------------------- |
| GET    | `/api/users/me` | Authenticated users |
| GET    | `/api/users`    | ADMIN only          |

---

# Query Parameters

Supported query params for:

```http
GET /api/tasks
```

| Parameter  | Description        |
| ---------- | ------------------ |
| status     | Filter by status   |
| priority   | Filter by priority |
| assigneeId | Filter by assignee |
| search     | Keyword search     |
| page       | Pagination page    |
| size       | Pagination size    |
| sortBy     | Sort field         |
| sortDir    | asc / desc         |

---

# Environment Variables

| Variable                   | Description         |
| -------------------------- | ------------------- |
| SPRING_DATASOURCE_URL      | PostgreSQL JDBC URL |
| SPRING_DATASOURCE_USERNAME | Database username   |
| SPRING_DATASOURCE_PASSWORD | Database password   |
| JWT_SECRET                 | JWT signing secret  |
| OPENAI_API_KEY             | OpenAI API key      |

Example:

```env
JWT_SECRET=your-secret-key
OPENAI_API_KEY=sk-...
```

Never commit real secrets.

---

# Testing

## Backend Tests

```bash
cd backend
mvn test
```

## Frontend Tests

```bash
cd frontend
npm test
```

## Frontend Coverage Report

```bash
npm run test:coverage
```

---

# CI/CD Pipeline

GitHub Actions automatically runs:

1. Backend tests
2. Frontend tests
3. Type checking
4. Docker build verification

Workflow file:

```text
.github/workflows/ci.yml
```

---

# Technical Decisions

## Why UUIDs?

UUIDs prevent predictable sequential IDs and improve scalability across distributed systems.

---

## Why Refresh Token Rotation?

Every refresh invalidates the previous refresh token, reducing the impact of token theft.

---

## Why Flyway?

Flyway provides deterministic, version-controlled database migrations.

---

## Why JPA Specifications?

Specifications allow dynamic query building without creating dozens of repository methods.

---

## Why Store Tokens In Memory?

Keeping tokens in Redux memory reduces exposure to XSS attacks compared to localStorage.

---

# Future Improvements

* Real-time collaboration with WebSockets
* Kanban drag-and-drop board
* Notifications and reminders
* File attachments
* Team workspaces
* Activity history
* Dark mode
* AI-generated subtasks
* Calendar integration
* Deployment to AWS or Azure

---

# Developer Notes

This project was designed to reflect real-world backend and frontend engineering practices:

* scalable architecture
* clean code organization
* layered services
* secure authentication
* DTO-based API design
* reusable frontend state management
* CI/CD workflows
* Dockerized development

---

# License

MIT License

---

# Author

Built by Varsha Rajawat.
