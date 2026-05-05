# Task Manager

A full-stack task management application with JWT authentication, role-based access control, and an OpenAI GPT-4o integration for AI-assisted task management.

**Backend:** Spring Boot 3 · Java 17 · PostgreSQL · Flyway  
**Frontend:** React 18 · TypeScript · Redux Toolkit · Tailwind CSS  
**AI:** OpenAI GPT-4o (task summaries + priority suggestions)  
**DevOps:** Docker · GitHub Actions CI

---

## Features

- Register and log in with JWT-based authentication (access token + rotating refresh token)
- Create, edit, delete, and filter tasks by status, priority, assignee, and keyword
- Role-based access: `USER` and `ADMIN` roles with method-level security
- AI "Summarize" button — generates a 2-3 sentence summary of any task via GPT-4o
- AI "Suggest Priority" button — classifies a task as LOW / MEDIUM / HIGH based on its content
- Swagger UI for exploring the API at `/swagger-ui.html`
- Full test suite: JUnit 5 + Mockito (backend), Jest + React Testing Library (frontend)
- One-command local setup via Docker Compose

---

## Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes Docker Compose)
- An OpenAI API key if you want to use the AI features (the app works fine without one)

### Run locally

```bash
# 1. Clone the repo
git clone https://github.com/varsharajawat/task-management-app.git
cd task-management-app

# 2. (Optional) Add your OpenAI API key
export OPENAI_API_KEY=sk-...

# 3. Start everything
docker compose up --build
```

| Service   | URL                              |
|-----------|----------------------------------|
| Frontend  | http://localhost:3000            |
| Backend   | http://localhost:8080            |
| Swagger   | http://localhost:8080/swagger-ui.html |
| Postgres  | localhost:5432                   |

To stop: `docker compose down`  
To stop and remove the database volume: `docker compose down -v`

---

## Running Without Docker

### Backend

```bash
cd backend

# Start a local Postgres instance (or update application.yml to point at yours)
# Default expects: host=localhost, port=5432, db=taskmanager, user=admin, password=password

mvn spring-boot:run
```

The app runs on port 8080. Flyway runs the migrations automatically on startup.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The app runs on port 3000. The Vite dev server proxies `/api/...` requests to `localhost:8080`, so no CORS issues during development.

---

## Project Structure

```
task-management-app/
├── backend/
│   ├── src/main/java/com/varsha/taskmanager/
│   │   ├── config/          # SecurityConfig, OpenApiConfig
│   │   ├── controller/      # AuthController, TaskController, UserController
│   │   ├── service/         # AuthService, TaskService, AiService
│   │   ├── repository/      # Spring Data JPA interfaces
│   │   ├── model/           # User, Task, RefreshToken entities + enums
│   │   ├── dto/             # Request/Response DTOs
│   │   ├── exception/       # GlobalExceptionHandler + custom exceptions
│   │   └── security/        # JwtUtil, JwtFilter, UserDetailsServiceImpl
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/    # Flyway SQL scripts (V1, V2, V3)
│   └── src/test/            # JUnit 5 + Mockito unit tests
│
├── frontend/
│   └── src/
│       ├── api/             # Axios instance + authApi, tasksApi
│       ├── store/slices/    # Redux: authSlice, tasksSlice
│       ├── routes/          # ProtectedRoute, AppRouter
│       ├── pages/           # LoginPage, RegisterPage, DashboardPage, TasksPage
│       ├── hooks/           # useAuth
│       ├── types/           # TypeScript interfaces (mirrors backend DTOs)
│       └── __tests__/       # Jest + React Testing Library
│
├── docker-compose.yml
├── .github/workflows/ci.yml
└── DEVELOPER_GUIDE.md
```

---

## API Overview

All protected endpoints require `Authorization: Bearer <access_token>`.

### Auth

| Method | Endpoint              | Auth | Description                        |
|--------|-----------------------|------|------------------------------------|
| POST   | `/api/auth/register`  | No   | Register, returns token pair       |
| POST   | `/api/auth/login`     | No   | Login, returns token pair          |
| POST   | `/api/auth/refresh`   | No   | Exchange refresh token for new access token |
| POST   | `/api/auth/logout`    | Yes  | Invalidate all refresh tokens      |

### Tasks

| Method | Endpoint                        | Auth  | Description                              |
|--------|---------------------------------|-------|------------------------------------------|
| GET    | `/api/tasks`                    | Yes   | Paginated list with filters              |
| POST   | `/api/tasks`                    | Yes   | Create a task                            |
| GET    | `/api/tasks/{id}`               | Yes   | Get task by ID                           |
| PUT    | `/api/tasks/{id}`               | Yes   | Update task (creator or ADMIN)           |
| PATCH  | `/api/tasks/{id}/status`        | Yes   | Update status only                       |
| DELETE | `/api/tasks/{id}`               | Yes   | Delete task (creator or ADMIN)           |
| GET    | `/api/tasks/dashboard-stats`    | Yes   | Summary counts for the dashboard         |
| POST   | `/api/tasks/{id}/summarize`     | Yes   | AI: generate task summary (GPT-4o)       |
| POST   | `/api/tasks/{id}/suggest-priority` | Yes | AI: suggest priority (GPT-4o)         |

### Users

| Method | Endpoint       | Auth         | Description         |
|--------|----------------|--------------|---------------------|
| GET    | `/api/users/me`| Yes          | Current user profile|
| GET    | `/api/users`   | Yes (ADMIN)  | List all users      |

Query params for `GET /api/tasks`: `status`, `priority`, `assigneeId`, `search`, `page`, `size`, `sortBy`, `sortDir`

---

## Tech Decisions Worth Knowing

**Why UUID primary keys?** Sequential integer IDs let anyone enumerate your data (`/tasks/1`, `/tasks/2`...). UUIDs reveal nothing and work across distributed systems.

**Why refresh token rotation?** On each refresh, the old token is deleted and a new one issued. If a token is stolen, the attacker is cut off the moment the legitimate user refreshes — the old token no longer exists in the database.

**Why tokens in memory, not localStorage?** `localStorage` is accessible to any JavaScript on the page. XSS attacks can steal it. Redux state (in-memory) can only be read by your own app's code.

**Why Flyway instead of `ddl-auto: update`?** Flyway keeps schema changes version-controlled alongside code. `ddl-auto: update` is non-deterministic — two developers running it in different orders can produce different schemas. Flyway runs migrations in a guaranteed order and records what has run in a `flyway_schema_history` table.

**Why JPA Specifications for task filtering?** A task list with 4 optional filters (status, priority, assignee, search) would need 16 separate query methods to cover every combination. Specifications let you build the WHERE clause dynamically at runtime — one method handles all cases.

---

## Running Tests

```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test

# Frontend with coverage report
npm run test:coverage
```

---

## Environment Variables

| Variable                    | Where used | Description                              | Default (dev) |
|-----------------------------|------------|------------------------------------------|---------------|
| `SPRING_DATASOURCE_URL`     | Backend    | PostgreSQL JDBC URL                      | localhost:5432/taskmanager |
| `SPRING_DATASOURCE_USERNAME`| Backend    | Database username                        | admin         |
| `SPRING_DATASOURCE_PASSWORD`| Backend    | Database password                        | password      |
| `JWT_SECRET`                | Backend    | HMAC signing key (min 32 chars)          | dev default   |
| `OPENAI_API_KEY`            | Backend    | OpenAI API key — AI features disabled without it | (empty) |

Never commit real values for these. Use a `.env` file locally (it's in `.gitignore`) or inject them via your deployment environment.

---

## CI/CD

GitHub Actions runs on every pull request:

1. **Backend tests** — `mvn test` with Java 17
2. **Frontend tests** — `npm test` + TypeScript type check (`tsc --noEmit`)
3. **Docker build check** — both Dockerfiles must build successfully (runs only if tests pass)

See `.github/workflows/ci.yml` for the full configuration.
