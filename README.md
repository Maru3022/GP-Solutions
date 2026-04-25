# Hotel Property View API

RESTful API для управления информацией об отелях. Приложение запускается на порту **8092**.

## Требования

- Java 17
- Maven 3.8+
- Spring Boot 3.2.0

## Структура проекта

```
hotel-property-view-api/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/gpsolutions/hotel/
    │   │   ├── HotelPropertyViewApplication.java
    │   │   ├── controller/
    │   │   │   └── HotelController.java
    │   │   ├── service/
    │   │   │   ├── HotelService.java
    │   │   │   └── HotelServiceImpl.java
    │   │   ├── repository/
    │   │   │   ├── HotelRepository.java
    │   │   │   ├── AmenityRepository.java
    │   │   │   └── HotelSpecification.java
    │   │   ├── entity/
    │   │   │   ├── Hotel.java
    │   │   │   ├── Address.java
    │   │   │   ├── Contacts.java
    │   │   │   ├── ArrivalTime.java
    │   │   │   └── Amenity.java
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   │   ├── CreateHotelRequest.java
    │   │   │   │   ├── AddressRequest.java
    │   │   │   │   ├── ContactsRequest.java
    │   │   │   │   ├── ArrivalTimeRequest.java
    │   │   │   │   └── AddAmenitiesRequest.java
    │   │   │   └── response/
    │   │   │       ├── HotelShortResponse.java
    │   │   │       ├── HotelFullResponse.java
    │   │   │       ├── AddressResponse.java
    │   │   │       ├── ContactsResponse.java
    │   │   │       └── ArrivalTimeResponse.java
    │   │   ├── mapper/
    │   │   │   └── HotelMapper.java
    │   │   └── exception/
    │   │       ├── HotelNotFoundException.java
    │   │       ├── ErrorResponse.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       ├── application.yml
    │       ├── application-mysql.yml
    │       ├── application-postgres.yml
    │       └── db/changelog/
    │           ├── db.changelog-master.xml
    │           ├── changeset-01-create-tables.xml
    │           └── changeset-02-initial-data.xml
    └── test/
        └── java/com/gpsolutions/hotel/
            └── HotelPropertyViewApplicationTests.java
```

## Начало работы

### 1. Сборка проекта

```bash
mvn clean install
```

### 2. Запуск приложения

```bash
mvn spring-boot:run
```

Приложение запустится на `http://localhost:8092`

### 3. Доступ к документации API

Swagger UI доступен по адресу: `http://localhost:8092/swagger-ui.html`

API документация в JSON формате: `http://localhost:8092/v3/api-docs`

H2 console: `http://localhost:8092/h2-console` (username: sa, password: пусто)

## REST API Эндпоинты

Все эндпоинты имеют префикс `/property-view`

### 1. GET /property-view/hotels

Получить список всех отелей с краткой информацией.

**Ответ:**
```json
[
  {
    "id": 1,
    "name": "Marriott Hotel Minsk",
    "description": "Luxury 5-star hotel in the heart of Minsk",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375291234567"
  }
]
```

### 2. GET /property-view/hotels/{id}

Получить полную информацию об отеле по ID.

**Параметры:**
- `id` (path parameter) - ID отеля

**Ответ:**
```json
{
  "id": 1,
  "name": "Marriott Hotel Minsk",
  "description": "Luxury 5-star hotel in the heart of Minsk",
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
  "amenities": ["Free Wi-Fi", "Swimming Pool", "Fitness Center", "Restaurant", "Parking"]
}
```

### 3. GET /property-view/search

Поиск отелей по критериям.

**Параметры запроса (все опциональны):**
- `name` - имя отеля (поиск по частичному совпадению)
- `brand` - бренд отеля
- `city` - город
- `country` - страна
- `amenities` - список amenities (разделённые запятыми)

**Примеры:**
```
GET /property-view/search?name=Marriott
GET /property-view/search?brand=Hilton&city=Moscow
GET /property-view/search?country=Russia&amenities=Free%20Wi-Fi,Swimming%20Pool
```

### 4. POST /property-view/hotels

Создать новый отель.

**Тело запроса:**
```json
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
```

**Ответ:**
```json
{
  "id": 4,
  "name": "New Hotel",
  "description": "Hotel description",
  "address": "123 Main Street, New York, 10001, USA",
  "phone": "+1234567890"
}
```

### 5. POST /property-view/hotels/{id}/amenities

Добавить amenities к отелю.

**Параметры:**
- `id` (path parameter) - ID отеля

**Тело запроса (JSON массив):**
```json
["Free Wi-Fi", "Swimming Pool", "Spa"]
```

**Ответ:** HotelFullResponse (см. пример в п.2)

### 6. GET /property-view/histogram/{param}

Получить гистограмму (количество отелей, сгруппированных по параметру).

**Параметры:**
- `param` - один из: `brand`, `city`, `country`, `amenities`

**Примеры:**
```
GET /property-view/histogram/brand
GET /property-view/histogram/city
GET /property-view/histogram/country
GET /property-view/histogram/amenities
```

**Ответ:**
```json
{
  "Marriott": 1,
  "Hilton": 1,
  "InterContinental": 1
}
```

## Обработка ошибок

Все ошибки возвращаются в следующем формате:

```json
{
  "timestamp": "2026-04-25T14:54:00",
  "status": 404,
  "message": "Hotel with id 999 not found",
  "path": "/property-view/hotels/999"
}
```

### Коды ответов

| Код | Описание |
|-----|----------|
| 200 | OK - Успешный запрос |
| 201 | Created - Ресурс создан |
| 400 | Bad Request - Ошибка валидации |
| 404 | Not Found - Ресурс не найден |
| 500 | Internal Server Error - Ошибка сервера |

## Профили конфигурации

### H2 (по умолчанию)
```bash
mvn spring-boot:run
```

### MySQL
1. Раскомментируйте настройки в `application-mysql.yml`
2. Запустите:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

### PostgreSQL
1. Раскомментируйте настройки в `application-postgres.yml`
2. Запустите:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=postgres"
```

## Данные тестирования

Приложение содержит 3 тестовых отеля:

1. **Marriott Hotel Minsk** - Минск, Беларусь
2. **Hilton Moscow TVerskaya** - Москва, Россия
3. **InterContinental Saint Petersburg** - Санкт-Петербург, Россия

Доступные amenities:
- Free Wi-Fi
- Swimming Pool
- Fitness Center
- Restaurant
- Parking
- Conference Room
- Spa

## Технологии

- **Java 17** - Язык программирования
- **Spring Boot 3.2.0** - Фреймворк
- **Spring Data JPA** - Слой данных
- **Liquibase** - Управление миграциями БД
- **H2 Database** - In-memory БД
- **Lombok** - Упрощение кода
- **SpringDoc OpenAPI** - Swagger UI документация
- **Maven** - Управление зависимостями

## Архитектура

Проект использует многослойную архитектуру:

- **Controller** - REST эндпоинты
- **Service** - Бизнес-логика
- **Repository** - Доступ к данным (JPA)
- **Entity** - Сущности БД
- **DTO** - Объекты передачи данных
- **Mapper** - Преобразование между Entity и DTO
- **Exception** - Обработка ошибок

## Лицензия

Apache License 2.0

## Разработчик

GP Solutions
