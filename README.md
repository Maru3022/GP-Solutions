# Hotel API

REST API for hotel management built with Spring Boot, Spring Data JPA, H2, Liquibase and Swagger.

## Tech stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- H2
- Liquibase
- Swagger / OpenAPI
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

Switch database profile in [application.properties](/D:/GP-Solutions/src/main/resources/application.properties) by changing `spring.profiles.active`.
