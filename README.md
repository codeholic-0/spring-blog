# Spring Blog Platform

A full-stack blog platform built with Spring Boot 4 and React. Users can create, edit, and delete posts, and add or remove comments on each post.

---

## Tech Stack

**Backend**
- Java 25, Spring Boot 4.1.0-RC1
- Maven multi-module project (4 modules)
- JPA / Hibernate with H2 (dev) or PostgreSQL (prod)
- Jakarta Validation for request DTO validation
- JUnit 5 + Mockito + MockMvc (25 tests)

**Frontend**
- React 19, TypeScript 6
- Vite 8, Tailwind CSS 4
- React Router 7

---

## Project Structure

```
spring-blog/
├── backend/                           Maven multi-module project
│   ├── pom.xml                        Parent POM (pom packaging)
│   ├── mvnw / mvnw.cmd                Maven wrapper scripts
│   ├── blog-common/                   DTOs, exceptions
│   ├── blog-persistence/              JPA entities, repositories
│   ├── blog-core/                     Business logic (services)
│   └── blog-web/                      Controllers, entry point, config
│       └── src/test/                  25 unit + integration tests
│
├── frontend/                          React + Vite + Tailwind
│   ├── vite.config.ts                 Dev server proxy /api → :8080
│   └── src/
│       ├── types/                     TypeScript interfaces
│       ├── api/                       Fetch wrappers (post + comment)
│       └── components/                PostList, PostForm, PostDetail
│
└── README.md
```

### Backend Module Dependency Flow

```
blog-web  →  blog-core  →  blog-persistence
                  ↓               ↓
             blog-common  ←───────┘
```

Dependencies flow strictly one way. `blog-common` has no internal dependencies. Lower modules cannot access higher modules.

---

## Prerequisites

- **Java 25** (OpenJDK)
- **Node.js 22+** and **npm**
- **PostgreSQL** (only for production — optional for development)

---

## Quick Start

### 1. Backend

```bash
cd backend

# Build everything
./mvnw clean install -DskipTests

# Start dev server (H2 in-memory, port 8080)
./mvnw spring-boot:run -pl blog-web
```

The API is available at `http://localhost:8080`.

### 2. Frontend

```bash
cd frontend

# Install dependencies (first time only)
npm install

# Start dev server with HMR (port 5173)
npm run dev
```

Open `http://localhost:5173` in the browser. The Vite dev server proxies `/api/*` requests to the backend.

### 3. Run Tests

```bash
cd backend

# Run all tests
./mvnw test

# Run tests for a specific module (rebuilds upstream dependencies)
./mvnw clean test -pl blog-web -am
```

---

## API Endpoints

### Posts

| Method | URL | Description | Response |
|--------|-----|-------------|----------|
| POST | `/api/posts` | Create a post | `201 Created` |
| GET | `/api/posts` | List all posts | `200 OK` |
| GET | `/api/posts/{id}` | Get post by ID | `200 OK` / `404` |
| PUT | `/api/posts/{id}` | Update a post | `200 OK` / `404` |
| DELETE | `/api/posts/{id}` | Delete a post | `204 No Content` / `404` |

### Comments

| Method | URL | Description | Response |
|--------|-----|-------------|----------|
| POST | `/api/posts/{postId}/comments` | Add a comment | `201 Created` / `404` |
| GET | `/api/posts/{postId}/comments` | List comments for a post | `200 OK` |
| DELETE | `/api/posts/{postId}/comments/{commentId}` | Delete a comment | `204 No Content` / `404` |

### Request Bodies

```json

// POST /api/posts  |  PUT /api/posts/{id}
{
    "title": "Post Title",
    "content": "Post content here...",
    "author": "Author Name"
}

// POST /api/posts/{postId}/comments
{
    "body": "Comment text",
    "author": "Commenter Name"
}
```

---

## Profiles

| Profile | Database | Usage |
|---------|----------|-------|
| `dev` (default) | H2 in-memory | Local development |
| `prod` | PostgreSQL | Production |

To run with the production profile:

```bash
./mvnw spring-boot:run -pl blog-web -P prod
```

Copy `application-prod.properties.example` to `application-prod.properties` and set your PostgreSQL credentials via the `POSTGRES_PASSWORD` environment variable.

---

## Build

```bash
# Backend fat JAR (includes embedded Tomcat)
cd backend
./mvnw clean package -DskipTests
java -jar blog-web/target/blog-web-1.0-SNAPSHOT.jar

# Frontend static build
cd frontend
npm run build
# Output in frontend/dist/
```

---

## Development Workflow

```
Terminal 1:  cd backend   && ./mvnw spring-boot:run -pl blog-web
Terminal 2:  cd frontend  && npm run dev
Browser:     http://localhost:5173
```

The Vite dev server proxies `/api` → `http://localhost:8080`, so all API calls from the frontend work without CORS configuration during development.
