# InnoTableTennis 🏓

Table Tennis club management platform for Innopolis University. Track players, matches, tournaments with an ELO-like rating system.

## Quick Start (Local Dev)

### Prerequisites

- Java 17 (`brew install openjdk@17`)
- Node.js 18+
- Docker (optional, for PostgreSQL)

### 1. Backend

```bash
cd TableTennisBackend
JAVA_HOME=/opt/homebrew/opt/openjdk@17 ./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

> Uses H2 in-memory database — no external DB needed. Data resets on restart.

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**


## Features

### Match Management
- Browse all matches with sort/filter by player name, score, date range
- Leaders can create, edit, and delete matches
- Paginated results

### Player Management
- Browse players with sort by name or rating
- Filter by name, Telegram alias, rating range
- Player profiles with stats: wins/losses, win rate, streaks, rating history graph (D3.js)
- ELO-like rating system with rating deltas per match

### Tournament Management
- Full CRUD for tournaments with sort/filter/pagination

### Tournament Constructor (Wizard)
Multi-step interactive tournament creation:

1. **Create** — set title and dates
2. **Add Participants** — pick players from the club database
3. **Number of Groups** — choose group count for stage 1
4. **Configure Groups** — add matches, set scores, rank players
5. **Continue or Finish** — decide whether to add a second stage
6. **Number of Finals** — configure finals for stage 2
7. **Distribution** — assign players to finals (group or single elimination bracket)
8. **Second Stage** — run finals, finalize tournament, trigger rating recalculation

### Admin Panel
- Promote/demote leaders (club admins)
- Send broadcast messages via Telegram bot to all players

### Telegram Bot Integration
- Password retrieval and user registration via `@InnoTableTennisBot`
- Broadcast messaging to all players

### Rating System
- ELO-based calculation triggered on tournament finalization
- Uses average player rating as baseline with KT coefficient
- Sequential tournament finalization enforced
- Supports forced recalculation

### Other
- Dark/light theme toggle
- Responsive design with mobile hamburger menu
- Pagination on all list views

## Project Structure

```
├── frontend/                  # SvelteKit app (InnoTableTennis)
│   ├── src/
│   │   ├── routes/            # Pages: /login, /matches, /players, /tournaments, /admin
│   │   └── lib/
│   │       ├── client/        # Browser-side API calls & stores
│   │       ├── server/        # Server-side API calls & JWT handling
│   │       ├── components/    # UI components (tables, forms, tournament constructor)
│   │       └── types/         # TypeScript types
│   └── static/                # CSS, favicon
├── TableTennisBackend/        # Spring Boot REST API
│   ├── src/main/java/.../
│   │   ├── controller/        # REST controllers
│   │   ├── service/           # Business logic
│   │   ├── entity/            # JPA entities
│   │   ├── repository/        # Data access
│   │   ├── dto/               # Request/response DTOs
│   │   ├── config/            # Security, CORS, app config
│   │   └── filter/            # JWT auth filter
│   └── src/main/resources/    # Application configs
└── score-scout-frontend/      # Alternative SvelteKit 2 frontend (preview)
```

## API Overview

| Endpoint | Description |
|---|---|
| `POST /auth/authenticate` | Login → JWT token |
| `GET /api/matches` | List matches (public) |
| `GET /api/players` | List players (auth required) |
| `GET /api/tournaments` | List tournaments (auth required) |
| `GET /api/players/{id}` | Player profile with stats |
| `GET /api/players/{id}/stats` | Player statistics |
| `POST /api/matches` | Create match (leader+) |
| `POST /api/players` | Create player (leader+) |
| `POST /api/tournaments` | Create tournament (leader+) |
| `PATCH /api/tournaments/{id}/state` | Update tournament state |
| `PATCH /api/tournaments/{id}` | Finalize tournament |
| `GET /api/leaders` | List leaders |
| `POST /api/leaders` | Promote to leader |

## Running with PostgreSQL (Docker)

```bash
cd TableTennisBackend
# Start PostgreSQL
docker compose -f docker-compose-dev.yml up postgresql-db -d

# Set env vars
export DEV_DB_USERNAME=postgres
export DEV_DB_PASSWORD=postgres
export DEV_JWT_SECRET=$(openssl rand -base64 64)

# Run backend
JAVA_HOME=/opt/homebrew/opt/openjdk@17 ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Deployment

Both frontend and backend have Dockerfiles for containerized deployment.
