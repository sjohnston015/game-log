# Game Log Application

A full-stack application for tracking gaming progress across multiple platforms.

## Project Overview

Aims to help gamers (ME!) organize their gaming libraries, 
track progress across multiple platforms, and discover new games (maybe)!

## Tech Stack

**Backend:** Java 21 + Spring Boot 3.5.3\
**Frontend:** React (coming soon! >.<)\
**Database:** PostgreSQL (H2 for development)\
**External API:** RAWG Video Games Database\
**Testing:** JUnit 5, Mockito, Spring Boot Test

## Current Features

### User Management (CRUD)

- **Create Account** - Basic user creation (authentication coming soon)
- **Profile Management** - Update username and email
- **User Directory** - Get user by ID and list all users
- **Account Deletion** - Remove user from database

### Game Search Integration

- **RAWG API Integration** - Search through their expansive database of games!
- **Game Data** - Auto-fetch titles, cover images, ratings, platforms
- **Search Filtering** - Results order by rating and popularity

### Personal Game Library System

- **Add Games to Library** - With status tracking!
- **Game Status Management** - PLANNING, PLAYING, COMPLETED, DROPPED
- **Personal Rating** - 1-10 scale rating system
- **Library Filtering** - View games by status
- **Update Game Progress** - Change status and ratings anytime
- **Remove from Library** - Clean up your collection

### Backend Architecture

- **RESTful API** - Reliable, predictable endpoints
- **Data Transfer Objects (DTOs)** - Secure API with validation
- **Automatic Timestamps** - Track when games were added/updated
- **Security-First Queries** - User isolation at database level
- **Testing** - Repository, Controller, and Service layers

## API Endpoints

### User Management

```
POST   /api/users              # Create new user (basic, auth coming soon)
GET    /api/users/{id}         # Get user by ID
PUT    /api/users/{id}         # Update user profile
DELETE /api/users/{id}         # Delete user
GET    /api/users              # Get all users
```

### Game Search

```
GET    /api/games/search?q={query}  # Search RAWG database
```

### Personal Game Log

```
POST   /api/users/{userId}/games           # Add game to library
GET    /api/users/{userId}/games           # Get user's game library
GET    /api/users/{userId}/games?status={status}  # Filter by status
PUT    /api/users/{userId}/games/{entryId} # Update game entry
DELETE /api/users/{userId}/games/{entryId} # Remove from library
```

## Database Schema

### Users Table

```
users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(25) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
)
```

### Game Log Entries

```
user_game_log (
  id BIGINT PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  rawg_game_id BIGINT NOT NULL,
  game_title VARCHAR(255) NOT NULL,
  game_cover_image TEXT,
  status ENUM('PLANNING', 'PLAYING', 'COMPLETED', 'DROPPED'),
  rating INTEGER CHECK (rating >= 1 AND rating <= 10),
  added_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
)
```

## Development Approach

### Test-Driven Development

- Repository Tests - Data layer validation with @DataJpaTest
- Service Tests - Business logic with Mockito mocking
- Controller Tests - API behavior with @WebMvcTest
- Integration Tests - End-to-end workflow validation

### Clean Architecture

- Separation of Concerns - Controller → Service → Repository pattern
- Constructor Injection - Better testability and dependency management
- Environment Configuration - Externalized configuration for different environments

## Getting Started

### Prerequisites!

- Java 21 or higher
- Maven 3.6+
- RAWG API Key (free at http://rawg.io/)

### Running the Application

On Mac/Linux: `./mvnw spring-boot:run`\
On Windows: `mvnw.cmd spring-boot:run`

### Running Tests

`./mvnw test`

The application will start on `http://localhost:8080`

### Testing the API

- **H2 Console** - `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:devdb`)
- **Test Frontend** - `http://localhost:8080` (basic search interface)

## Planned Features

### Coming Soon

- **User Authentication** - JWT-based login system and proper password security
- **React Frontend** - Modern, responsive user interface
- **Advanced Filtering** - Search your library by genre, platform, rating
- **Social Features** - Share favorite games and see friends' libraries
- **Statistics Dashboard** - Track your gaming habits and preferences

### Coming Later

- **Review System**
- **Achievement Tracking** 
- **Wishlist Management**
- **Reading Lists**
- **Game Data Export**

## Note

This is a personal project, but I'm open to any and all feedback. Please, feel 
free to let me know of any bugs or issues, to suggest a new feature, provide 
frontend ideas, etc. 

---

*Project Started: June 20th, 2025 | Status: Updated on June 28th, 2025*


