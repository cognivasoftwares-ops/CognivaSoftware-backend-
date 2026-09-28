# Cogniva Softwares – Backend

Spring Boot 3.5 (Java 17) + PostgreSQL REST API for the Cogniva Softwares website.

## What it does

| Area | Endpoints | Access |
|---|---|---|
| Contact form | `POST /api/contact` | Public (rate-limited: 5 per IP / 10 min, honeypot spam check) |
| Services | `GET /api/services`, `GET /api/services/{slug}` | Public |
| Portfolio | `GET /api/projects`, `GET /api/projects/{slug}` | Public |
| Admin login | `POST /api/auth/login`, `GET /api/auth/me` | Login is public, `/me` needs a token |
| Enquiries | `GET/PATCH/DELETE /api/admin/enquiries[/{id}]`, `GET /api/admin/enquiries/stats` | Admin (JWT) |
| Manage services | `GET/POST/PUT/DELETE /api/admin/services[/{id}]` | Admin (JWT) |
| Manage projects | `GET/POST/PUT/DELETE /api/admin/projects[/{id}]` | Admin (JWT) |
| Health | `GET /actuator/health` | Public |

The database is created and seeded by **Flyway** on first start-up — the 6 services and 8 projects
are copied from the frontend's `src/data/services.js` and `src/data/projects.js`.

## Project structure

```
src/main/java/com/cogniva/backend
├── CognivaBackendApplication.java
├── config/        AppProperties (app.* settings), SecurityConfig (JWT, CORS)
├── security/      JwtService, JwtAuthenticationFilter
├── auth/          AdminUser entity, login controller, first-admin initializer
├── enquiry/       Contact form: entity, public + admin controllers, rate limiter
├── catalog/
│   ├── service/   ServiceOffering entity + public/admin APIs
│   └── project/   Project entity + public/admin APIs
└── common/        BaseEntity, error handling, paging helpers
src/main/resources
├── application.yml
└── db/migration/  V1 schema, V2 services seed, V3 projects seed
```

## Run it locally

### 1. Start PostgreSQL

**Option A – installed PostgreSQL:** open pgAdmin or `psql` and run

```sql
CREATE DATABASE cogniva_db;
```

**Option B – Docker:** `docker compose up -d` (uses `docker-compose.yml` in this folder).

### 2. Configure (optional)

Defaults in `application.yml` connect to `localhost:5432/cogniva_db` as `postgres` / `postgres`.
If your PostgreSQL password is different, set environment variables in IntelliJ
(**Run → Edit Configurations → Environment variables**) — see `.env.example`:

```
DB_PASSWORD=your_password;JWT_SECRET=some-long-random-string-at-least-32-chars
```

### 3. Run

- **IntelliJ:** open this folder, let it import `pom.xml` as a Maven project (set Project SDK to 17+),
  enable annotation processing if asked (Lombok), then run `CognivaBackendApplication`.
- **Command line:** `mvn spring-boot:run`

The API starts on **http://localhost:8080**. Try `http://localhost:8080/api/services` in your browser.

### 4. Test the endpoints

Open `api-requests.http` in IntelliJ and click ▶ next to any request. The login request stores the
token automatically for the admin calls.

## Admin account

On first start-up, if no admin exists, one is created from:

| Variable | Default |
|---|---|
| `ADMIN_EMAIL` | `admin@cognivasoftwares.com` |
| `ADMIN_PASSWORD` | `Admin@12345` |

**Change `ADMIN_PASSWORD` and `JWT_SECRET` before deploying.** (Changing `ADMIN_PASSWORD` later
does not update an existing admin — it only applies when the table is empty.)

## Configuration reference

| Env variable | Purpose | Default |
|---|---|---|
| `PORT` | HTTP port | `8080` |
| `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` | PostgreSQL connection | local `cogniva_db`, `postgres/postgres` |
| `JWT_SECRET` | HMAC signing key (≥ 32 chars) | dev-only value |
| `JWT_EXPIRATION_MINUTES` | Token lifetime | `480` (8 h) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated frontend URLs | `http://localhost:5173,http://localhost:4173` |

## Changing the database schema

Never edit an existing `V*.sql` file after it has run. Add a new file, e.g.
`V4__add_newsletter_table.sql`, and restart — Flyway applies it automatically.

## Error format

Every error returns the same JSON shape:

```json
{
  "timestamp": "2026-09-28T06:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/contact",
  "fieldErrors": { "email": "Enter a valid email address" }
}
```

## Next steps (not included yet)

- Email notification to the team when a new enquiry arrives (`spring-boot-starter-mail`)
- An admin dashboard UI in the React app that uses the `/api/admin/**` endpoints
