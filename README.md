# 🏨 Hotel Property View API

[![CI/CD](https://github.com/Maru3022/GP-Solutions/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/Maru3022/GP-Solutions/actions/workflows/ci-cd.yml)
[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

A production-ready **RESTful API** for comprehensive hotel management, built with modern Java technologies and enterprise-grade architecture.

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [API Endpoints](#-api-endpoints)
- [Quick Start](#-quick-start)
- [Configuration Profiles](#-configuration-profiles)
- [Testing](#-testing)
- [API Documentation](#-api-documentation)
- [Docker Support](#-docker-support)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Project Structure](#-project-structure)
- [Design Patterns](#-design-patterns)
- [Database Migration](#-database-migration)
- [Error Handling](#-error-handling)

---

## ✨ Features

- ✅ **Full CRUD Operations** - Create, read, update hotels with complete information
- 🔍 **Advanced Search** - Multi-criteria search with name, brand, city, country, amenities
- 📊 **Analytics & Statistics** - Histogram endpoints for data aggregation
- 🏷️ **Amenity Management** - Dynamic amenity assignment to hotels
- 📚 **Swagger Documentation** - Interactive API documentation with OpenAPI 3.0
- 🗄️ **Multi-Database Support** - H2 (default), MySQL, PostgreSQL, MongoDB-ready
- 🧪 **Comprehensive Testing** - Unit tests, integration tests, code coverage
- 🐳 **Docker Ready** - Containerized deployment with multi-stage builds
- 🔒 **Security Scanning** - Automated vulnerability detection with Trivy & OWASP
- 📈 **Code Quality** - Checkstyle, SpotBugs, JaCoCo coverage enforcement

---

## 🛠 Technology Stack

### Core Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Programming language with modern features |
| **Spring Boot** | 3.2.0 | Application framework & auto-configuration |
| **Spring Data JPA** | 3.2.0 | Data access layer with ORM |
| **Maven** | 3.8+ | Dependency management & build automation |

### Database & Migration

| Technology | Version | Purpose |
|------------|---------|---------|
| **H2 Database** | Latest | In-memory database (development/testing) |
| **PostgreSQL** | 16 | Production-ready relational database |
| **MySQL** | 8.0+ | Alternative production database |
| **Liquibase** | Latest | Database version control & migrations |

### Libraries & Tools

| Technology | Purpose |
|------------|---------|
| **Lombok** | Boilerplate code reduction (getters, setters, builders) |
| **SpringDoc OpenAPI** | Swagger UI & OpenAPI 3.0 documentation |
| **Spring Validation** | Request validation with Bean Validation API |
| **Spring Actuator** | Application health monitoring & metrics |

### Code Quality & Security

| Tool | Purpose |
|------|---------|
| **JaCoCo** | Code coverage analysis (minimum 60% enforced) |
| **SpotBugs** | Static code analysis for bug detection |
| **Checkstyle** | Code style enforcement (Google Java Style) |
| **OWASP Dependency Check** | Vulnerable dependency detection |
| **Trivy** | Container image vulnerability scanning |
| **Maven Enforcer** | Build environment consistency |

### DevOps & CI/CD

| Tool | Purpose |
|------|---------|
| **GitHub Actions** | Automated CI/CD pipeline |
| **Docker** | Containerization & deployment |
| **Docker Buildx** | Multi-platform image builds |
| **Hadolint** | Dockerfile linting |

---

## 🏗 Architecture

### Layered Architecture Pattern

The application follows a clean **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│    (Controller + DTOs + Validation)     │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Business Logic Layer           │
│       (Service + Business Rules)        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Data Access Layer               │
│    (Repository + JPA + Specifications)  │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Database Layer                 │
│      (H2 / PostgreSQL / MySQL)          │
└─────────────────────────────────────────┘
```

### Architecture Layers

1. **Controller Layer** (`controller/`)
   - REST endpoint definitions
   - Request/response handling
   - Input validation
   - HTTP status codes

2. **Service Layer** (`service/`)
   - Business logic implementation
   - Transaction management
   - Data orchestration
   - Exception handling

3. **Repository Layer** (`repository/`)
   - Data access operations
   - JPA Specifications for dynamic queries
   - Custom query methods
   - Database interactions

4. **Entity Layer** (`entity/`)
   - JPA entity definitions
   - Database table mappings
   - Relationships (OneToMany, ManyToMany)
   - Domain models

5. **DTO Layer** (`dto/request/`, `dto/response/`)
   - Data Transfer Objects
   - API contract definitions
   - Request validation
   - Response formatting

6. **Mapper Layer** (`mapper/`)
   - Entity ↔ DTO conversion
   - Data transformation
   - Object mapping logic

7. **Exception Layer** (`exception/`)
   - Custom exception classes
   - Global error handling
   - Standardized error responses

---

## 🔌 API Endpoints

**Base URL:** `http://localhost:8092/property-view`

### 1. Get All Hotels

```http
GET /property-view/hotels
```

**Description:** Retrieve a list of all hotels with short information.

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "DoubleTree by Hilton Minsk",
    "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375 17 309-80-00"
  }
]
```

---

### 2. Get Hotel by ID

```http
GET /property-view/hotels/{id}
```

**Description:** Retrieve detailed information about a specific hotel.

**Path Parameters:**
- `id` (Long) - Hotel identifier

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
  "brand": "Hilton",
  "address": {
    "houseNumber": "9",
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  },
  "amenities": [
    "Free parking",
    "Free WiFi",
    "Non-smoking rooms",
    "Fitness center"
  ]
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2026-04-25T14:54:00",
  "status": 404,
  "message": "Hotel with id 999 not found",
  "path": "/property-view/hotels/999"
}
```

---

### 3. Search Hotels

```http
GET /property-view/search?[name=...]&[brand=...]&[city=...]&[country=...]&[amenities=...]
```

**Description:** Search hotels by multiple criteria. All parameters are optional and can be combined.

**Query Parameters:**
- `name` (String, optional) - Hotel name (partial match)
- `brand` (String, optional) - Hotel brand
- `city` (String, optional) - City location
- `country` (String, optional) - Country location
- `amenities` (String, optional) - Comma-separated amenity names

**Examples:**
```
GET /property-view/search?city=minsk
GET /property-view/search?brand=Hilton&country=Belarus
GET /property-view/search?amenities=Free WiFi,Fitness center
```

**Response:** `200 OK` (Same format as Get All Hotels)

---

### 4. Create Hotel

```http
POST /property-view/hotels
```

**Description:** Create a new hotel with complete information.

**Request Body:**
```json
{
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
  "brand": "Hilton",
  "address": {
    "houseNumber": "9",
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "/property-view/hotels",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}
```

**Response:** `201 Created`
```json
{
  "id": 4,
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
  "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
  "phone": "+375 17 309-80-00"
}
```

**Validation Errors:** `400 Bad Request`
```json
{
  "timestamp": "2026-04-25T14:54:00",
  "status": 400,
  "message": "Validation failed",
  "errors": [
    "name: must not be blank",
    "brand: must not be blank"
  ],
  "path": "/property-view/hotels"
}
```

---

### 5. Add Amenities to Hotel

```http
POST /property-view/hotels/{id}/amenities
```

**Description:** Add one or more amenities to an existing hotel. Creates new amenities if they don't exist.

**Path Parameters:**
- `id` (Long) - Hotel identifier

**Request Body:**
```json
[
  "Free parking",
  "Free WiFi",
  "Non-smoking rooms",
  "Fitness center",
  "Room service"
]
```

**Response:** `200 OK` (Same format as Get Hotel by ID)

---

### 6. Get Histogram

```http
GET /property-view/histogram/{param}
```

**Description:** Get count of hotels grouped by specified parameter for analytics.

**Path Parameters:**
- `param` (String) - Grouping parameter: `brand`, `city`, `country`, or `amenities`

**Example 1 - By City:**
```
GET /property-view/histogram/city
```

**Response:**
```json
{
  "Minsk": 1,
  "Moscow": 2,
  "Saint Petersburg": 1
}
```

**Example 2 - By Amenities:**
```
GET /property-view/histogram/amenities
```

**Response:**
```json
{
  "Free parking": 1,
  "Free WiFi": 20,
  "Non-smoking rooms": 5,
  "Fitness center": 3
}
```

**Error Response:** `400 Bad Request`
```json
{
  "timestamp": "2026-04-25T14:54:00",
  "status": 400,
  "message": "Invalid histogram parameter: invalid. Must be one of: brand, city, country, amenities",
  "path": "/property-view/histogram/invalid"
}
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 17 or higher** - [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/downloads)

### Installation & Run

```bash
# 1. Clone the repository
git clone https://github.com/Maru3022/GP-Solutions.git
cd GP-Solutions

# 2. Build the project
./mvnw clean install

# 3. Run the application
./mvnw spring-boot:run
```

**Application starts at:** `http://localhost:8092`

### Verify Installation

```bash
# Check health endpoint
curl http://localhost:8092/actuator/health

# Get all hotels
curl http://localhost:8092/property-view/hotels
```

---

## ⚙️ Configuration Profiles

The application supports multiple database configurations through Spring profiles.

### H2 Database (Default - Development)

In-memory database, perfect for development and testing.

```bash
./mvnw spring-boot:run
```

**H2 Console:** `http://localhost:8092/h2-console`
- JDBC URL: `jdbc:h2:mem:hoteldb`
- Username: `sa`
- Password: _(empty)_

### MySQL Profile

```bash
# 1. Configure application-mysql.yml with your MySQL credentials
# 2. Run with mysql profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

### PostgreSQL Profile

```bash
# Option 1: Using Docker Compose (recommended)
docker-compose up -d

# Option 2: Run with postgres profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Production Profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 🧪 Testing

### Run All Tests

```bash
# Unit tests only
./mvnw test

# Integration tests only
./mvnw verify -DskipTests=false

# All tests with coverage report
./mvnw verify
```

### Test Coverage

JaCoCo generates coverage reports in `target/site/jacoco/`. Minimum **60% coverage** is enforced.

```bash
# Open coverage report
open target/site/jacoco/index.html  # macOS
start target/site/jacoco/index.html # Windows
```

### Test Categories

- **Unit Tests** - Service and mapper layer testing
- **Integration Tests** - Full application context testing
- **Smoke Tests** - Docker container health verification

---

## 📖 API Documentation

### Swagger UI

Interactive API documentation with request/response examples:

**URL:** `http://localhost:8092/swagger-ui.html`

**Features:**
- 📋 All endpoints with descriptions
- 🧪 Try it out - execute requests directly from browser
- 📝 Request/response schemas
- 🔍 Search and filter endpoints

### OpenAPI Specification

**JSON Format:** `http://localhost:8092/v3/api-docs`

---

## 🐳 Docker Support

### Build Docker Image

```bash
# Build image
docker build -t hotel-api:latest .

# Run container
docker run -d -p 8092:8092 \
  -e SPRING_PROFILES_ACTIVE=h2 \
  --name hotel-api \
  hotel-api:latest
```

### Docker Compose

Run with PostgreSQL database:

```bash
docker-compose up -d
```

This starts:
- **PostgreSQL** database on port 5432
- **Hotel API** application on port 8092

### Docker Multi-Stage Build

The Dockerfile uses a multi-stage build for optimal image size:
1. **Builder stage** - Compiles the application with JDK
2. **Runtime stage** - Runs with minimal JRE (~150MB final image)

---

## 🔧 CI/CD Pipeline

Automated CI/CD pipeline using GitHub Actions.

### Pipeline Stages

1. **Code Quality** - Checkstyle, SpotBugs, Maven Enforcer
2. **Unit Tests** - Java 17 & 21 compatibility testing
3. **Integration Tests** - Full application testing with JaCoCo coverage
4. **Security Scan** - OWASP Dependency Check for vulnerabilities
5. **Docker Lint** - Hadolint Dockerfile validation
6. **Docker Build & Scan** - Build image + Trivy vulnerability scan
7. **Smoke Test** - Container health verification
8. **Docker Push** - Push to GitHub Container Registry (main branch only)

### Pipeline Status

[![CI/CD](https://github.com/Maru3022/GP-Solutions/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/Maru3022/GP-Solutions/actions/workflows/ci-cd.yml)

### Required Secrets

- `NVD_API_KEY` - OWASP NVD API key (optional, improves dependency check)

[Get your free NVD API key](https://nvd.nist.gov/developers/request-an-api-key)

---

## 📁 Project Structure

```
GP-Solutions/
├── .github/workflows/          # CI/CD pipeline configuration
│   └── ci-cd.yml
├── src/
│   ├── main/
│   │   ├── java/com/gpsolutions/hotel/
│   │   │   ├── controller/     # REST API endpoints
│   │   │   │   └── HotelController.java
│   │   │   ├── service/        # Business logic
│   │   │   │   ├── HotelService.java
│   │   │   │   └── HotelServiceImpl.java
│   │   │   ├── repository/     # Data access layer
│   │   │   │   ├── HotelRepository.java
│   │   │   │   ├── AmenityRepository.java
│   │   │   │   └── HotelSpecification.java
│   │   │   ├── entity/         # JPA entities
│   │   │   │   ├── Hotel.java
│   │   │   │   ├── Address.java
│   │   │   │   ├── Contacts.java
│   │   │   │   ├── ArrivalTime.java
│   │   │   │   └── Amenity.java
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── request/    # Request DTOs
│   │   │   │   └── response/   # Response DTOs
│   │   │   ├── mapper/         # Entity-DTO mapping
│   │   │   │   └── HotelMapper.java
│   │   │   ├── exception/      # Error handling
│   │   │   │   ├── HotelNotFoundException.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── HotelPropertyViewApplication.java
│   │   └── resources/
│   │       ├── application.yml              # Main configuration
│   │       ├── application-h2.yml           # H2 profile
│   │       ├── application-mysql.yml        # MySQL profile
│   │       ├── application-postgres.yml     # PostgreSQL profile
│   │       ├── application-prod.yml         # Production profile
│   │       └── db/changelog/                # Liquibase migrations
│   │           ├── db.changelog-master.xml
│   │           ├── changeset-01-create-tables.xml
│   │           └── changeset-02-initial-data.xml
│   └── test/java/com/gpsolutions/hotel/    # Test classes
│       ├── mapper/
│       │   └── HotelMapperTest.java
│       ├── service/
│       │   └── HotelServiceImplTest.java
│       └── HotelPropertyViewApplicationIT.java
├── Dockerfile                   # Docker multi-stage build
├── docker-compose.yml           # Docker Compose configuration
├── pom.xml                      # Maven configuration
├── spotbugs-exclude.xml         # SpotBugs exclusions
├── .trivyignore                 # Trivy vulnerability ignore list
└── README.md                    # This file
```

---

## 🎨 Design Patterns

### 1. **Layered Architecture Pattern**
Clear separation between presentation, business, and data access layers.

### 2. **DTO Pattern**
Data Transfer Objects decouple internal entities from API contracts.

### 3. **Mapper Pattern**
Centralized object transformation between Entity and DTO layers.

### 4. **Repository Pattern**
Abstracts data access logic using Spring Data JPA repositories.

### 5. **Specification Pattern**
Dynamic query building using JPA Specifications for flexible search.

### 6. **Service Layer Pattern**
Business logic encapsulation with transaction management.

### 7. **Global Exception Handling**
Centralized error handling using `@ControllerAdvice`.

### 8. **Builder Pattern**
Lombok `@Builder` for fluent object construction.

---

## 🗄 Database Migration

### Liquibase Changelog

Database schema is version-controlled using Liquibase:

```
db/changelog/
├── db.changelog-master.xml           # Master changelog
├── changeset-01-create-tables.xml    # Schema creation
└── changeset-02-initial-data.xml     # Sample data
```

### Automatic Migration

Liquibase runs automatically on application startup:
- Creates tables if they don't exist
- Applies pending migrations
- Maintains migration history in `DATABASECHANGELOG` table

### Adding New Migrations

1. Create new changeset file: `changeset-03-*.xml`
2. Add to master changelog: `<include file="db/changelog/changeset-03-*.xml"/>`
3. Restart application - migration applies automatically

---

## ⚠️ Error Handling

### Error Response Format

All errors follow a consistent JSON structure:

```json
{
  "timestamp": "2026-04-25T14:54:00",
  "status": 404,
  "message": "Detailed error message",
  "path": "/property-view/hotels/999"
}
```

### HTTP Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| **200** | OK | Successful GET/POST requests |
| **201** | Created | New hotel created successfully |
| **400** | Bad Request | Validation errors, invalid parameters |
| **404** | Not Found | Hotel or resource not found |
| **500** | Internal Server Error | Unexpected server errors |

### Validation

Request validation using Bean Validation annotations:
- `@NotNull` - Required fields
- `@NotBlank` - Non-empty strings
- `@Email` - Email format validation
- `@Size` - Length constraints

---

## 📊 Monitoring & Health Checks

### Actuator Endpoints

```bash
# Application health
curl http://localhost:8092/actuator/health

# Application info
curl http://localhost:8092/actuator/info
```

### Health Response

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **Apache License 2.0** - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Developer

**GP Solutions**  
🌐 [Website](https://gpsolutions.com)  
📧 Contact: [Available through GitHub](https://github.com/Maru3022)

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- All open-source contributors
- JetBrains for development tools

---

<div align="center">

**Built with ❤️ using Java & Spring Boot**

[⭐ Star this repository](https://github.com/Maru3022/GP-Solutions) | [🐛 Report Issues](https://github.com/Maru3022/GP-Solutions/issues) | [📖 Documentation](https://github.com/Maru3022/GP-Solutions#-api-endpoints)

</div>
