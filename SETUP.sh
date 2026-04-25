#!/bin/bash

# Hotel Property View API - Setup Script
# Этот скрипт инициализирует Git репозиторий и выполняет первый коммит

echo "=========================================="
echo "Hotel Property View API - Setup Script"
echo "=========================================="
echo ""

# Проверка наличия Git
if ! command -v git &> /dev/null; then
    echo "❌ Git не установлен. Пожалуйста, установите Git."
    exit 1
fi

# Проверка наличия Maven
if ! command -v mvn &> /dev/null; then
    echo "⚠️  Maven не установлен. Вы можете запустить mvn clean install вручную."
fi

# Проверка наличия Java
if ! command -v java &> /dev/null; then
    echo "❌ Java не установлена. Пожалуйста, установите Java 17 или выше."
    exit 1
fi

echo "✅ Все зависимости найдены"
echo ""

# Инициализация Git репозитория
echo "Инициализация Git репозитория..."
git init

# Добавление всех файлов
echo "Добавление файлов в Stage..."
git add .

# Проверка файлов
echo ""
echo "Файлы готовы к коммиту:"
git status

# Первый коммит
echo ""
echo "Создание первого коммита..."
git commit -m "Initial commit: Hotel Property View API v1.0.0

Features:
- 6 REST endpoints for hotel management
- Multi-layer architecture (Controller → Service → Repository)
- JPA Specification for dynamic filtering
- Liquibase database migrations (3 changesets)
- Swagger/OpenAPI 3.0 documentation
- Comprehensive error handling with @ControllerAdvice
- 15+ integration tests
- Support for H2, MySQL, PostgreSQL databases
- Lombok annotations for reduced boilerplate
- Jakarta validation annotations

Project Structure:
- 28 Java classes
- 5 Entity classes with relationships
- 10 DTO classes (Request/Response)
- 3 Repository interfaces
- 2 Service classes
- 1 REST Controller
- 3 Exception classes
- Comprehensive documentation

Database:
- 5 tables with proper relationships
- 5 indices for performance
- 14 test data records
- Liquibase versioning

Technology Stack:
- Spring Boot 3.2.0
- Spring Data JPA
- Hibernate 6.x
- Liquibase Core
- H2 Database (default)
- Lombok
- SpringDoc OpenAPI
- Jakarta Validation

Author: GP Solutions
License: Apache License 2.0"

# Проверка успеха
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ Git репозиторий инициализирован!"
    echo "=========================================="
    echo ""
    echo "Следующие шаги:"
    echo ""
    echo "1. Просмотр истории коммитов:"
    echo "   git log"
    echo ""
    echo "2. Просмотр статуса:"
    echo "   git status"
    echo ""
    echo "3. Добавление удаленного репозитория:"
    echo "   git remote add origin <url>"
    echo ""
    echo "4. Пуш на GitHub/GitLab:"
    echo "   git push -u origin main"
    echo ""
    echo "5. Сборка проекта:"
    echo "   mvn clean install"
    echo ""
    echo "6. Запуск приложения:"
    echo "   mvn spring-boot:run"
    echo ""
    echo "7. Доступ к приложению:"
    echo "   API: http://localhost:8092/property-view/hotels"
    echo "   Swagger: http://localhost:8092/swagger-ui.html"
    echo "   H2 Console: http://localhost:8092/h2-console"
    echo ""
    echo "=========================================="
else
    echo ""
    echo "❌ Ошибка при создании коммита!"
    exit 1
fi
