# 🎬 AnimeWatch - Professional Anime Management Platform

> Ein vollständiges, produktionsreifes Fullstack-System für Anime-Verwaltung. JWT-Authentication, Multi-Tenancy, Admin-Panel, Watchlist & Ratings.

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?logo=spring)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue?logo=react)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)

---

## ✨ Features

✅ **JWT Authentication** (15min Access, 30-Tage Refresh Tokens)
✅ **Multi-Tenancy** (Automatic Query Filtering)
✅ **Anime CRUD** (Search, Filter, Sort)
✅ **Watchlist Management** (Plan to Watch, Watching, Completed, Dropped)
✅ **Rating System** (1-10 Score + Comments)
✅ **Admin Panel** (Anime Manager, Invitations)
✅ **Professional UI** (Crunchyroll-inspired Design)
✅ **Role-Based Access** (Admin/User)

---

## 🛠️ Tech Stack

### Backend
- **Java 21** + **Spring Boot 3.2**
- **Spring Security** + **JWT (JJWT 0.12.3)**
- **Spring Data JPA** + **PostgreSQL 16**
- **Maven 3.9** + **Lombok**

### Frontend
- **React 18** + **Vite 5.4**
- **React Router 6** + **Tailwind CSS 4**
- **Context API** (State Management)

### DevOps
- **Docker Compose** (PostgreSQL + pgAdmin)
- **Maven Wrapper** (No installation needed)

---

## 🚀 Quick Start

```bash
# 1. Repo klonen
git clone https://github.com/tunc3r93/AnimeWatchList.git
cd AnimeWatchList

# 2. Docker starten
docker-compose up -d

# 3. Backend starten (Terminal 1)
cd backend
./mvnw spring-boot:run

# 4. Frontend starten (Terminal 2)
cd frontend
npm install
npm run dev
```

**URLs:**
- Frontend: http://localhost:5174
- Backend: http://localhost:8080
- Database: localhost:5432 (pgAdmin: localhost:5050)

**Test Login:**
```
Email: admin@example.com
Password: admin123
```

---

## 📁 Projektstruktur

```
backend/
├── src/main/java/com/animewatch/
│   ├── controller/        # REST Endpoints
│   ├── service/           # Business Logic
│   ├── repository/        # Data Access (JPA)
│   ├── domain/model/      # Entities
│   ├── dto/               # Data Transfer Objects
│   ├── security/          # JWT + Auth Filter
│   ├── config/            # Spring Configuration
│   └── exception/         # Custom Exceptions
├── src/main/resources/
│   ├── application.yml    # Configuration
│   └── .mvn/wrapper/      # Maven Wrapper
└── pom.xml

frontend/
├── src/
│   ├── pages/            # React Pages
│   ├── components/       # Reusable Components
│   ├── context/          # Auth Context
│   ├── api/              # API Client
│   ├── App.jsx           # Root Component
│   └── main.jsx          # Entry Point
├── index.html
├── vite.config.js
├── tailwind.config.js
└── package.json

├── docker-compose.yml    # Docker Config
├── init.sql              # Database Schema
├── .gitignore
├── START.ps1            # Startup Script
└── README.md
```

---

## 📡 API Endpoints

### Auth
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/register` - Register
- `POST /api/v1/auth/refresh` - Refresh Token

### Anime
- `GET /api/v1/anime` - Get all (paginated, searchable)
- `GET /api/v1/anime/{id}` - Get single
- `POST /api/v1/anime` - Create (Admin)
- `PUT /api/v1/anime/{id}` - Update (Admin)
- `DELETE /api/v1/anime/{id}` - Delete (Admin)

### Watchlist
- `GET /api/v1/watchlist` - Get user watchlist
- `POST /api/v1/watchlist` - Add anime
- `PUT /api/v1/watchlist/{animeId}` - Update status
- `DELETE /api/v1/watchlist/{animeId}` - Remove

### Ratings
- `GET /api/v1/ratings/anime/{animeId}` - Get ratings
- `POST /api/v1/ratings` - Create/Update rating

### Admin
- `GET /api/v1/admin/invitations` - Get invitations
- `POST /api/v1/admin/invitations` - Create invitation

---

## 🔄 Authentication Flow

```
Login Request
    ↓
AuthController.login()
    ↓
AuthService.authenticate()
    ├─ Validate Email/Password
    ├─ Generate JWT Tokens
    └─ Return Tokens + User + Tenant
    ↓
Frontend speichert Token in localStorage
    ↓
Alle API-Requests senden JWT im Header
    ↓
JwtAuthenticationFilter validiert Token
    ├─ Extrahiert User ID
    ├─ Setzt TenantContext (Multi-Tenancy)
    └─ Erstellt Authentication Principal
    ↓
Zugriff zu Protected Endpoints
```

---

## 🏢 Multi-Tenancy Pattern

Jede Anfrage wird automatisch gefiltert nach Tenant:

```java
// TenantContext speichert Tenant per Thread
TenantContext.setTenant(tenant);

// Service Layer - Queries werden automatisch gefiltert
@Query("SELECT a FROM Anime a WHERE a.tenant.id = ?#{T(com.animewatch.domain.model.TenantContext).getTenant().id()}")

// Cleanup
TenantContext.clear();
```

---

## 📊 Database Schema

```
Tenants (1) ──┬──────── (N) Users
              ├──────── (N) Anime
              ├──────── (N) Invitations
              └──────── (N) Watchlist_Entries

Users (1) ────┬──────── (N) Watchlist_Entries
              ├──────── (N) Ratings
              └──────── (N) Invitations (created_by)

Anime (1) ────┬──────── (N) Watchlist_Entries
              └──────── (N) Ratings

Indexes:
- users(email, tenant_id)
- anime(tenant_id)
- watchlist_entries(user_id, anime_id)
- ratings(user_id, anime_id)
- invitations(code, tenant_id)
```

---

## 🔒 Security

- **JWT Tokens:** HS256 mit Secret-Key
- **Password:** Plaintext (Test), BCrypt (Production)
- **CORS:** Configured für localhost:5173/5174
- **Multi-Tenancy:** Automatic Query Filtering
- **Exceptions:** Unified Error Responses mit HTTP Status
- **Stateless:** Keine Server-Side Sessions

---

## 🚀 Production Checklist

- [ ] Environment-Variablen setzen
- [ ] JWT Secret ändern
- [ ] CORS Origins aktualisieren
- [ ] Database Backups
- [ ] HTTPS/TLS aktivieren
- [ ] Rate Limiting
- [ ] Logging & Monitoring
- [ ] Tests ausführen

---

## 📚 Tools & Libraries

| Tool | Version | Usage |
|------|---------|-------|
| Java | 21 | Backend Language |
| Maven | 3.9.8 | Build & Dependencies |
| Docker | Latest | Containerization |
| PostgreSQL | 16 | Database |
| React | 18.2 | Frontend Framework |
| Vite | 5.4 | Frontend Build Tool |
| Tailwind CSS | 4.0 | Styling |

---
