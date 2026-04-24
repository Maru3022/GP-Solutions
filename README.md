# Hotel API

REST API for hotel management built with Spring Boot, Spring Data JPA, H2, Liquibase and Swagger.

## Tech stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Cache
- Redis
- H2
- PostgreSQL
- Liquibase
- Swagger / OpenAPI
- Docker / Docker Compose
- GitHub Actions CI/CD
- JUnit 5 / MockMvc

## Run

Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Application starts on `http://localhost:8092`.

Swagger UI:

```text
http://localhost:8092/swagger-ui.html
```

OpenAPI docs:

```text
http://localhost:8092/v3/api-docs
```

H2 console:

```text
http://localhost:8092/h2-console
```

## Endpoints

- `GET /property-view/hotels`
- `GET /property-view/hotels/{id}`
- `GET /property-view/search`
- `POST /property-view/hotels`
- `POST /property-view/hotels/{id}/amenities`
- `GET /property-view/histogram/{param}`

## Search examples

```text
GET /property-view/search?city=Minsk
GET /property-view/search?brand=Hilton
GET /property-view/search?amenities=Free WiFi&amenities=Spa
```

## Histogram examples

```text
GET /property-view/histogram/city
GET /property-view/histogram/brand
GET /property-view/histogram/country
GET /property-view/histogram/amenities
```

## Profiles

- `h2` default
- `postgres`
- `mysql`
- `redis`
- `prod`

Switch database profile in [application.properties](/D:/GP-Solutions/src/main/resources/application.properties) by changing `spring.profiles.active`.

`prod` profile includes PostgreSQL and Redis.

## Production-like local run

Build jar:

```bash
./mvnw clean package
```

Start full stack:

```bash
docker compose up --build
```

This starts:

- PostgreSQL on `localhost:5432`
- Redis on `localhost:6379`
- API on `localhost:8092`

## Redis caching

Redis cache is configured for:

- hotel list
- hotel details
- search results
- histogram results

Write operations clear related caches automatically.

## CI/CD

GitHub Actions pipeline is configured in [.github/workflows/ci-cd.yml](/D:/GP-Solutions/.github/workflows/ci-cd.yml):

- CI: clean build + tests + jar artifact
- CD: Docker image build and publish to `ghcr.io`
