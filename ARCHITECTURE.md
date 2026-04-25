# Архитектура проекта Hotel Property View API

## Обзор проекта

**Hotel Property View API** — это RESTful веб-сервис для управления информацией об отелях. Приложение построено на базе Spring Boot 3.2.0, использует JPA/Hibernate для работы с БД, Liquibase для миграций, и предоставляет полнофункциональный REST API для CRUD операций и поиска по отелям.

### Основные характеристики:
- **Порт**: 8092
- **Database**: H2 (in-memory по умолчанию, поддержка MySQL и PostgreSQL через профили)
- **Framework**: Spring Boot 3.2.0, Spring Data JPA
- **Documentation**: Swagger UI (OpenAPI 3.0)
- **Migrations**: Liquibase
- **Build**: Maven, Java 17

---

## 📁 Структура проекта

```
hotel-property-view-api/
├── pom.xml                                 # Maven конфигурация
├── README.md                               # Документация проекта
├── ARCHITECTURE.md                         # Файл архитектуры (этот файл)
├── .gitignore                              # Git ignore правила
│
├── src/main/java/com/gpsolutions/hotel/
│   ├── HotelPropertyViewApplication.java   # Spring Boot точка входа + OpenAPI конфигурация
│   │
│   ├── controller/
│   │   └── HotelController.java            # REST контроллер (6 эндпоинтов)
│   │
│   ├── service/
│   │   ├── HotelService.java               # Интерфейс сервиса
│   │   └── HotelServiceImpl.java            # Имплементация бизнес-логики
│   │
│   ├── repository/
│   │   ├── HotelRepository.java            # JpaRepository + @Query для гистограмм
│   │   ├── AmenityRepository.java          # JpaRepository для amenities
│   │   └── HotelSpecification.java         # JPA Specification для динамической фильтрации
│   │
│   ├── entity/
│   │   ├── Hotel.java                      # Главная сущность отеля
│   │   ├── Address.java                    # Адрес (OneToOne с Hotel)
│   │   ├── Contacts.java                   # Контакты (OneToOne с Hotel)
│   │   ├── ArrivalTime.java                # Время заезда/выезда (@Embedded)
│   │   └── Amenity.java                    # Удобства (ManyToMany с Hotel)
│   │
│   ├── dto/
│   │   ├── request/
│   │   │   ├── AddressRequest.java
│   │   │   ├── ContactsRequest.java
│   │   │   ├── ArrivalTimeRequest.java
│   │   │   ├── CreateHotelRequest.java
│   │   │   └── AddAmenitiesRequest.java
│   │   └── response/
│   │       ├── AddressResponse.java
│   │       ├── ContactsResponse.java
│   │       ├── ArrivalTimeResponse.java
│   │       ├── HotelShortResponse.java    # Краткая инфо (для списков)
│   │       └── HotelFullResponse.java     # Полная инфо (для детальных запросов)
│   │
│   ├── mapper/
│   │   └── HotelMapper.java                # Преобразование Entity <-> DTO
│   │
│   └── exception/
│       ├── HotelNotFoundException.java      # Custom exception
│       ├── ErrorResponse.java              # Стандартный формат ошибки
│       └── GlobalExceptionHandler.java     # @ControllerAdvice для глобальной обработки
│
├── src/main/resources/
│   ├── application.yml                     # Основная конфигурация (H2)
│   ├── application-mysql.yml               # Профиль MySQL
│   ├── application-postgres.yml            # Профиль PostgreSQL
│   └── db/changelog/
│       ├── db.changelog-master.xml         # Главный файл Liquibase
│       ├── changeset-01-create-tables.xml  # DDL всех таблиц
│       └── changeset-02-initial-data.xml   # Тестовые данные (3 отеля)
│
└── src/test/java/com/gpsolutions/hotel/
    └── HotelPropertyViewApplicationTests.java  # Интеграционные тесты

```

---

## 🔄 Многослойная архитектура

Проект следует классической многослойной архитектуре:

```
┌─────────────────────────────────────────┐
│        REST Controller Layer            │
│  (HotelController - 6 REST endpoints)   │
└──────────────────┬──────────────────────┘
                   │ HTTP Request/Response
┌──────────────────▼──────────────────────┐
│      Service Layer (Business Logic)     │
│  (HotelService - HotelServiceImpl)       │
│  - Validation                           │
│  - Business rules                       │
│  - Transaction management               │
└──────────────────┬──────────────────────┘
                   │ Entity/DTO Mapping
┌──────────────────▼──────────────────────┐
│        Mapper Layer                     │
│  (HotelMapper - Entity <-> DTO)         │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│     Repository Layer (Data Access)      │
│  (HotelRepository, AmenityRepository)   │
│  - CRUD operations                      │
│  - Custom queries (JPQL, Specification) │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│      Entity Layer (Domain Model)        │
│  - Hotel, Address, Contacts, Amenity    │
│  - Relationships (OneToOne, ManyToMany) │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│      Database Layer (H2/MySQL/PG)       │
│  - Liquibase migrations                 │
│  - Schema management                    │
└─────────────────────────────────────────┘
```

---

## 📊 Модель данных

### Entity диаграмма

```
┌─────────────────┐
│     HOTEL       │
├─────────────────┤
│ id (PK)         │
│ name            │
│ description     │
│ brand           │
│ address_id (FK) │──────┐
│ contacts_id(FK) │──────┤
│ check_in        │      │
│ check_out       │      │
└─────────────────┘      │
        ▲                 │
        │                 │
        │         ┌───────┴────────┐
        │         │                │
        │    ┌────▼──────┐    ┌────▼──────┐
        │    │  ADDRESS  │    │ CONTACTS  │
        │    ├──────────┤    ├──────────┤
        │    │ id (PK)  │    │ id (PK)  │
        │    │ house... │    │ phone    │
        │    │ street   │    │ email    │
        │    │ city     │    └──────────┘
        │    │ country  │
        │    │ post_code│
        │    └──────────┘
        │
  ┌─────┴──────────────────┐
  │                        │
┌─┴──────────────┐  ┌──────┴───────┐
│ HOTEL_AMENITIES│  │   AMENITY    │
├────────────────┤  ├──────────────┤
│ hotel_id (FK)  │  │ id (PK)      │
│ amenity_id(FK) │──│ name         │
└────────────────┘  └──────────────┘
  (ManyToMany)
```

### Таблицы БД

#### 1. **HOTEL**
| Поле | Тип | Constraints |
|------|-----|-------------|
| id | BIGINT | PK, AUTO_INCREMENT |
| name | VARCHAR(200) | NOT NULL |
| description | TEXT | NULL |
| brand | VARCHAR(100) | NOT NULL |
| address_id | BIGINT | FK (address), NOT NULL, UNIQUE |
| contacts_id | BIGINT | FK (contacts), NOT NULL, UNIQUE |
| check_in | VARCHAR(5) | NOT NULL (формат HH:mm) |
| check_out | VARCHAR(5) | NULL (формат HH:mm) |

#### 2. **ADDRESS**
| Поле | Тип | Constraints |
|------|-----|-------------|
| id | BIGINT | PK, AUTO_INCREMENT |
| house_number | VARCHAR(10) | NOT NULL |
| street | VARCHAR(100) | NOT NULL |
| city | VARCHAR(100) | NOT NULL |
| country | VARCHAR(100) | NOT NULL |
| post_code | VARCHAR(20) | NOT NULL |

#### 3. **CONTACTS**
| Поле | Тип | Constraints |
|------|-----|-------------|
| id | BIGINT | PK, AUTO_INCREMENT |
| phone | VARCHAR(20) | NOT NULL |
| email | VARCHAR(100) | NOT NULL |

#### 4. **AMENITY**
| Поле | Тип | Constraints |
|------|-----|-------------|
| id | BIGINT | PK, AUTO_INCREMENT |
| name | VARCHAR(100) | NOT NULL, UNIQUE |

#### 5. **HOTEL_AMENITIES** (ManyToMany junction table)
| Поле | Тип | Constraints |
|------|-----|-------------|
| hotel_id | BIGINT | FK (hotel), NOT NULL |
| amenity_id | BIGINT | FK (amenity), NOT NULL |
| | | PK (hotel_id, amenity_id) |

---

## 🔌 REST API Эндпоинты

### Обзор (6 эндпоинтов)

| Method | Endpoint | Описание | Status |
|--------|----------|---------|--------|
| GET | `/property-view/hotels` | Список всех отелей (краткая инфо) | 200 |
| GET | `/property-view/hotels/{id}` | Полная инфо по отелю | 200/404 |
| GET | `/property-view/search` | Поиск по критериям | 200 |
| POST | `/property-view/hotels` | Создание нового отеля | 201/400 |
| POST | `/property-view/hotels/{id}/amenities` | Добавление amenities | 200/400/404 |
| GET | `/property-view/histogram/{param}` | Гистограмма (GROUP BY) | 200 |

### Детальное описание

#### 1️⃣ **GET /property-view/hotels**
```
Возвращает: List<HotelShortResponse>

Пример ответа:
[
  {
    "id": 1,
    "name": "Marriott Hotel Minsk",
    "description": "Luxury 5-star hotel",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375291234567"
  },
  ...
]
```

#### 2️⃣ **GET /property-view/hotels/{id}**
```
Параметры: id (Long)
Возвращает: HotelFullResponse

Пример ответа:
{
  "id": 1,
  "name": "Marriott Hotel Minsk",
  "description": "Luxury 5-star hotel",
  "brand": "Marriott",
  "address": {
    "houseNumber": "9",
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375291234567",
    "email": "marriott.minsk@example.com"
  },
  "arrivalTime": {
    "checkIn": "15:00",
    "checkOut": "11:00"
  },
  "amenities": ["Free Wi-Fi", "Swimming Pool", "Fitness Center"]
}
```

#### 3️⃣ **GET /property-view/search?name=...&brand=...&city=...&country=...&amenities=...**
```
Query параметры (все опциональны):
- name: String (поиск по частичному совпадению)
- brand: String
- city: String
- country: String
- amenities: String (разделенные запятыми)

Возвращает: List<HotelShortResponse>

Примеры:
GET /property-view/search?name=Marriott
GET /property-view/search?country=Russia&city=Moscow
GET /property-view/search?amenities=Free%20Wi-Fi,Swimming%20Pool
```

#### 4️⃣ **POST /property-view/hotels**
```
Тело запроса: CreateHotelRequest
{
  "name": "New Hotel",
  "description": "Hotel description",
  "brand": "Brand Name",
  "address": {
    "houseNumber": "123",
    "street": "Main Street",
    "city": "New York",
    "country": "USA",
    "postCode": "10001"
  },
  "contacts": {
    "phone": "+1234567890",
    "email": "hotel@example.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}

Возвращает: HotelShortResponse (201 Created)
```

#### 5️⃣ **POST /property-view/hotels/{id}/amenities**
```
Параметры: id (Long)
Тело запроса: List<String>
["Free Wi-Fi", "Spa", "Swimming Pool"]

Возвращает: HotelFullResponse (200 OK)
```

#### 6️⃣ **GET /property-view/histogram/{param}**
```
Параметры: param (brand|city|country|amenities)

Возвращает: Map<String, Long>

Примеры ответов:

GET /property-view/histogram/brand
{
  "Marriott": 1,
  "Hilton": 1,
  "InterContinental": 1
}

GET /property-view/histogram/city
{
  "Minsk": 1,
  "Moscow": 1,
  "Saint Petersburg": 1
}

GET /property-view/histogram/amenities
{
  "Free Wi-Fi": 3,
  "Swimming Pool": 3,
  "Fitness Center": 3,
  "Restaurant": 3,
  "Parking": 3,
  "Conference Room": 1,
  "Spa": 1
}
```

---

## 🔍 Обработка ошибок

### Стандартный формат ошибки

```json
{
  "timestamp": "2026-04-25T15:01:00",
  "status": 404,
  "message": "Hotel with id 999 not found",
  "path": "/property-view/hotels/999"
}
```

### HTTP Коды ответов

| Код | Описание | Случай использования |
|-----|---------|----------------------|
| 200 | OK | Успешный GET, успешный POST для update |
| 201 | Created | Успешный POST для создания новых ресурсов |
| 400 | Bad Request | Ошибки валидации, некорректные параметры |
| 404 | Not Found | Ресурс не найден |
| 500 | Internal Server Error | Ошибки сервера |

### Exception Handling

Все исключения обрабатываются через **GlobalExceptionHandler** (@ControllerAdvice):

1. **HotelNotFoundException** → 404 Not Found
2. **MethodArgumentNotValidException** → 400 Bad Request (со списком ошибок валидации)
3. **Exception** (все остальные) → 500 Internal Server Error

---

## 🔐 Валидация

Используется Spring Validation с аннотациями:

- `@NotBlank` - обязательное строковое поле (не пусто)
- `@NotNull` - обязательное поле (не null)
- `@NotEmpty` - коллекция не пуста
- `@Email` - валидный email
- `@Pattern` - регулярное выражение (например, для времени HH:mm)
- `@Valid` - рекурсивная валидация вложенных объектов

---

## 📝 Технические детали

### Spring Boot Dependencies

```xml
<dependencies>
  <dependency>spring-boot-starter-web</dependency>
  <dependency>spring-boot-starter-data-jpa</dependency>
  <dependency>spring-boot-starter-validation</dependency>
  <dependency>liquibase-core</dependency>
  <dependency>h2 (runtime)</dependency>
  <dependency>lombok</dependency>
  <dependency>springdoc-openapi-starter-webmvc-ui</dependency>
</dependencies>
```

### Configuration

**application.yml** (H2 - по умолчанию):
```yaml
server:
  port: 8092

spring:
  datasource:
    url: jdbc:h2:mem:hoteldb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate.ddl-auto: validate
    show-sql: true
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml
  h2:
    console.enabled: true

springdoc:
  swagger-ui.path: /swagger-ui.html
```

### H2 Console

При запуске с H2 можно открыть консоль:
```
URL: http://localhost:8092/h2-console
Username: sa
Password: (пусто)
JDBC URL: jdbc:h2:mem:hoteldb
```

---

## 🧪 Тестирование

### Интеграционные тесты

Файл: `HotelPropertyViewApplicationTests.java`

Охватывает:
- ✅ Загрузка контекста приложения
- ✅ GET /hotels (список)
- ✅ GET /hotels/{id} (успешно и 404)
- ✅ GET /search (с разными параметрами)
- ✅ POST /hotels (создание)
- ✅ POST /hotels/{id}/amenities
- ✅ GET /histogram/{param}
- ✅ Обработка ошибок валидации

Запуск тестов:
```bash
mvn test
```

---

## 🚀 Запуск приложения

### Требования
- Java 17+
- Maven 3.8+

### Сборка

```bash
# Полная сборка (compile + test + package)
mvn clean install

# Только компиляция
mvn clean compile

# Запуск тестов
mvn test
```

### Запуск

```bash
# Через Maven
mvn spring-boot:run

# Через JAR
mvn clean package
java -jar target/hotel-property-view-api-1.0.0.jar

# С конкретным профилем
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

### Доступ к приложению

```
API: http://localhost:8092/property-view/hotels
Swagger UI: http://localhost:8092/swagger-ui.html
API Docs: http://localhost:8092/v3/api-docs
H2 Console: http://localhost:8092/h2-console
```

---

## 📚 Профили конфигурации

### H2 (In-Memory) - Default

```bash
mvn spring-boot:run
```

### MySQL

1. Раскомментируйте в `application-mysql.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hoteldb
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_password
  jpa:
    hibernate:
      dialect: org.hibernate.dialect.MySQL8Dialect
```

2. Запустите:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

### PostgreSQL

Аналогично MySQL, используя `application-postgres.yml`

---

## 📦 Структура пакетов

```
com.gpsolutions.hotel
├── controller           # REST endpoints
├── service             # Business logic (interface + impl)
├── repository          # Data access (JpaRepository + custom queries)
├── entity              # JPA entities (domain model)
├── dto
│   ├── request         # Input DTOs
│   └── response        # Output DTOs
├── mapper              # Entity <-> DTO mapping
├── exception           # Custom exceptions + global handler
└── HotelPropertyViewApplication.java  # Application entry point
```

---

## 🔄 Flow примера: Создание отеля

```
1. CLIENT отправляет POST /property-view/hotels с CreateHotelRequest
   │
2. HotelController.createHotel() получает запрос
   │ Аннотация @Valid срабатывает
   │
3. Валидация DTO (AddressRequest, ContactsRequest, ArrivalTimeRequest)
   │ Если ошибки → GlobalExceptionHandler → 400 Bad Request
   │
4. HotelService.createHotel() в Controller
   │ (бизнес-логика, логирование)
   │
5. HotelMapper.toEntity() преобразует DTO в Entity
   │ (CreateHotelRequest → Hotel + Address + Contacts + ArrivalTime)
   │
6. HotelRepository.save(hotel)
   │ Hibernate генерирует INSERT SQL
   │ Liquibase управляет схемой
   │ H2 / MySQL / PG выполняет запрос
   │
7. HotelMapper.toShortResponse() преобразует Entity обратно в DTO
   │ (Hotel → HotelShortResponse)
   │
8. HotelController возвращает ResponseEntity.status(201).body(response)
   │
9. Spring сериализует DTO в JSON
   │
10. CLIENT получает 201 Created с JSON ответом
```

---

## 🎯 Key Design Patterns

1. **DTO Pattern** - разделение внутренней модели и API контракта
2. **Specification Pattern** - динамическая фильтрация запросов
3. **Mapper Pattern** - преобразование между Entity и DTO
4. **Repository Pattern** - абстракция доступа к данным
5. **Service Pattern** - бизнес-логика отдельно от контроллера
6. **ControllerAdvice Pattern** - глобальная обработка ошибок
7. **Embedded Pattern** - встроенный тип (ArrivalTime)

---

## 📊 Статистика проекта

| Метрика | Значение |
|---------|----------|
| Java классов | 28 |
| Entity классов | 5 |
| DTO классов | 10 |
| Repository интерфейсов | 3 |
| Service классов | 2 |
| REST endpoints | 6 |
| Таблиц БД | 5 |
| Liquibase changesets | 6 |
| Тестов | 15+ |
| Строк кода | ~3000 |

---

## 🔗 Зависимости

### Основные
- Spring Boot 3.2.0
- Spring Data JPA
- Hibernate 6.x
- H2 Database
- Liquibase Core

### Дополнительные
- Lombok (code generation)
- SpringDoc OpenAPI (Swagger UI)
- Jakarta Bean Validation
- Jackson (JSON)

---

## 📝 Автор

GP Solutions

## 📄 Лицензия

Apache License 2.0

---
