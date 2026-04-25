# Hotel Property View API - Setup Script (PowerShell)
# Этот скрипт инициализирует Git репозиторий и выполняет первый коммит

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Hotel Property View API - Setup Script" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Проверка наличия Git
try {
    git --version | Out-Null
    Write-Host "✅ Git найден" -ForegroundColor Green
} catch {
    Write-Host "❌ Git не установлен. Пожалуйста, установите Git." -ForegroundColor Red
    exit 1
}

# Проверка наличия Java
try {
    java -version 2>&1 | Out-Null
    Write-Host "✅ Java найдена" -ForegroundColor Green
} catch {
    Write-Host "❌ Java не установлена. Пожалуйста, установите Java 17 или выше." -ForegroundColor Red
    exit 1
}

# Проверка наличия Maven
try {
    mvn --version | Out-Null
    Write-Host "✅ Maven найден" -ForegroundColor Green
} catch {
    Write-Host "⚠️  Maven не установлен. Вы можете запустить 'mvn clean install' вручную." -ForegroundColor Yellow
}

Write-Host ""

# Инициализация Git репозитория
Write-Host "Инициализация Git репозитория..." -ForegroundColor Yellow
git init

# Добавление всех файлов
Write-Host "Добавление файлов в Stage..." -ForegroundColor Yellow
git add .

# Проверка файлов
Write-Host ""
Write-Host "Файлы готовы к коммиту:" -ForegroundColor Yellow
git status

# Первый коммит
Write-Host ""
Write-Host "Создание первого коммита..." -ForegroundColor Yellow

$commitMessage = @"
Initial commit: Hotel Property View API v1.0.0

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
License: Apache License 2.0
"@

git commit -m $commitMessage

# Проверка успеха
if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host "✅ Git репозиторий инициализирован!" -ForegroundColor Green
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Следующие шаги:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. Просмотр истории коммитов:" -ForegroundColor Yellow
    Write-Host "   git log" -ForegroundColor White
    Write-Host ""
    Write-Host "2. Просмотр статуса:" -ForegroundColor Yellow
    Write-Host "   git status" -ForegroundColor White
    Write-Host ""
    Write-Host "3. Добавление удаленного репозитория:" -ForegroundColor Yellow
    Write-Host "   git remote add origin <url>" -ForegroundColor White
    Write-Host ""
    Write-Host "4. Пуш на GitHub/GitLab:" -ForegroundColor Yellow
    Write-Host "   git push -u origin main" -ForegroundColor White
    Write-Host ""
    Write-Host "5. Сборка проекта:" -ForegroundColor Yellow
    Write-Host "   mvn clean install" -ForegroundColor White
    Write-Host ""
    Write-Host "6. Запуск приложения:" -ForegroundColor Yellow
    Write-Host "   mvn spring-boot:run" -ForegroundColor White
    Write-Host ""
    Write-Host "7. Доступ к приложению:" -ForegroundColor Yellow
    Write-Host "   API: http://localhost:8092/property-view/hotels" -ForegroundColor White
    Write-Host "   Swagger: http://localhost:8092/swagger-ui.html" -ForegroundColor White
    Write-Host "   H2 Console: http://localhost:8092/h2-console" -ForegroundColor White
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "❌ Ошибка при создании коммита!" -ForegroundColor Red
    exit 1
}
