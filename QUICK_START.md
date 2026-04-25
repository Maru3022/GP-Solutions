# 🚀 QUICK START - Hotel Property View API

**Project:** Hotel Property View API v1.0.0  
**Status:** ✅ Ready to Run  
**Location:** `D:\GP-Solutions\hotel-property-view-api`  
**Time to First Request:** ~5 minutes  

---

## ⚡ 30-Second Setup

```bash
# 1. Navigate to project
cd D:\GP-Solutions\hotel-property-view-api

# 2. Build (first time only)
mvn clean install

# 3. Run
mvn spring-boot:run

# 4. Test (in another terminal)
curl http://localhost:8092/property-view/hotels

# 5. Visit Swagger
open http://localhost:8092/swagger-ui.html
```

---

## 📋 Prerequisites

### Required
- ✅ **Java 17 or higher** 
  ```bash
  java -version
  # Output: openjdk version "21.0.6" or higher
  ```

- ✅ **Maven 3.8 or higher**
  ```bash
  mvn -v
  # Output: Apache Maven 3.8.0 or higher
  ```

### Optional
- Git (for version control)
- Postman or curl (for API testing)
- IntelliJ IDEA or Eclipse (for development)

---

## 🏃 Running the Application

### Option 1: Via Maven (Recommended)

```bash
cd D:\GP-Solutions\hotel-property-view-api
mvn spring-boot:run
```

**Output:**
```
2026-04-25 15:05:00 INFO  o.s.b.w.e.t.TomcatWebServer - Tomcat started on port(s): 8092
2026-04-25 15:05:01 INFO  c.g.h.HotelPropertyViewApplication - Started HotelPropertyViewApplication
```

### Option 2: Build JAR and Run

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/hotel-property-view-api-1.0.0.jar
```

### Option 3: From IDE

1. Open project in IntelliJ IDEA / Eclipse
2. Right-click on `HotelPropertyViewApplication.java`
3. Select "Run" or "Debug"

---

## ✅ Verify Application is Running

### Check 1: API Health
```bash
curl -i http://localhost:8092/property-view/hotels
```

Expected Response:
```
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "name": "Marriott Hotel Minsk",
    ...
  }
]
```

### Check 2: Swagger UI
```
Open: http://localhost:8092/swagger-ui.html
```

Expected: Interactive Swagger documentation with all 6 endpoints

### Check 3: H2 Console
```
Open: http://localhost:8092/h2-console
Username: sa
Password: (leave empty)
JDBC URL: jdbc:h2:mem:hoteldb
```

Expected: H2 database web console loads

---

## 🔗 API Endpoints Quick Reference

### 1. List All Hotels
```bash
curl http://localhost:8092/property-view/hotels
```

### 2. Get Hotel Details (ID=1)
```bash
curl http://localhost:8092/property-view/hotels/1
```

### 3. Search Hotels
```bash
# By name
curl "http://localhost:8092/property-view/search?name=Marriott"

# By country
curl "http://localhost:8092/property-view/search?country=Russia"

# By multiple criteria
curl "http://localhost:8092/property-view/search?country=Russia&city=Moscow"
```

### 4. Create New Hotel
```bash
curl -X POST http://localhost:8092/property-view/hotels \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Hotel",
    "brand": "TestBrand",
    "description": "Test description",
    "address": {
      "houseNumber": "100",
      "street": "Main St",
      "city": "TestCity",
      "country": "TestCountry",
      "postCode": "12345"
    },
    "contacts": {
      "phone": "+1234567890",
      "email": "test@example.com"
    },
    "arrivalTime": {
      "checkIn": "14:00",
      "checkOut": "12:00"
    }
  }'
```

### 5. Add Amenities to Hotel
```bash
curl -X POST http://localhost:8092/property-view/hotels/1/amenities \
  -H "Content-Type: application/json" \
  -d '["Free Wi-Fi", "Swimming Pool", "Spa"]'
```

### 6. Get Statistics (Histogram)
```bash
# By brand
curl http://localhost:8092/property-view/histogram/brand

# By city
curl http://localhost:8092/property-view/histogram/city

# By country
curl http://localhost:8092/property-view/histogram/country

# By amenities
curl http://localhost:8092/property-view/histogram/amenities
```

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

Expected:
```
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
```

### Run Specific Test
```bash
mvn test -Dtest=HotelPropertyViewApplicationTests#testGetAllHotels
```

### Run Tests with Coverage
```bash
mvn test jacoco:report
# Report: target/site/jacoco/index.html
```

---

## 📁 Project Structure

```
hotel-property-view-api/
├── src/main/java/com/gpsolutions/hotel/
│   ├── controller/     ← REST endpoints (modify to add new endpoints)
│   ├── service/        ← Business logic (modify to add features)
│   ├── repository/     ← Database queries (modify to add queries)
│   ├── entity/         ← Data models (modify to add new entities)
│   ├── dto/            ← Transfer objects (modify to add new DTOs)
│   ├── mapper/         ← Entity ↔ DTO conversion (modify to add mappings)
│   └── exception/      ← Error handling (modify to add error types)
│
├── src/main/resources/
│   ├── application.yml          ← Main config
│   ├── application-mysql.yml    ← MySQL profile
│   ├── application-postgres.yml ← PostgreSQL profile
│   └── db/changelog/            ← Database migrations
│
├── src/test/java/               ← Tests (modify to add test cases)
│
├── pom.xml                       ← Maven config (modify to add dependencies)
├── README.md                     ← Usage guide
├── ARCHITECTURE.md               ← Design details
├── FINAL_REPORT.md              ← Completion report
└── FILE_INVENTORY.md            ← This file list
```

---

## ⚙️ Configuration

### Change Port
Edit `src/main/resources/application.yml`:
```yaml
server:
  port: 9000  # Changed from 8092
```

### Switch to MySQL
Edit `src/main/resources/application-mysql.yml` (uncomment):
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hoteldb
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_password
```

Run with MySQL profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

### Switch to PostgreSQL
Similar to MySQL, edit `application-postgres.yml` and run with `--spring.profiles.active=postgres`

---

## 🐛 Troubleshooting

### Issue: Port 8092 already in use
**Solution:**
```bash
# Option 1: Kill process on port 8092
lsof -ti:8092 | xargs kill -9

# Option 2: Change port in application.yml
server:
  port: 8093
```

### Issue: Maven not found
**Solution:**
```bash
# Install Maven or use Maven Wrapper
./mvnw spring-boot:run  # Linux/macOS
mvnw.cmd spring-boot:run  # Windows
```

### Issue: Java version mismatch
**Solution:**
```bash
# Check version
java -version

# Install Java 17+
# Download from: https://adoptopenjdk.net/
```

### Issue: Database error
**Solution:**
```bash
# H2 is in-memory, no setup needed
# If using MySQL/PostgreSQL, ensure database is running
mysql -u root -p
# CREATE DATABASE hoteldb;
```

### Issue: Tests failing
**Solution:**
```bash
# Run with verbose output
mvn test -X

# Run single test
mvn test -Dtest=HotelPropertyViewApplicationTests#testGetAllHotels
```

---

## 📚 Next Steps

### 1. Explore the Code
- Read **ARCHITECTURE.md** for system design
- Read **README.md** for API documentation
- Check **src/main/java** for implementation

### 2. Try the API
- Use Swagger UI at http://localhost:8092/swagger-ui.html
- Use curl commands from above
- Use Postman to test endpoints

### 3. Modify the Code
- Add new endpoint in `HotelController.java`
- Add new entity in `entity/` folder
- Add new query in `HotelRepository.java`
- Add migration in `db/changelog/`

### 4. Run Tests
- View existing tests in `HotelPropertyViewApplicationTests.java`
- Add new test methods
- Run with `mvn test`

### 5. Deploy
- Build JAR with `mvn clean package`
- Run JAR with `java -jar target/hotel-property-view-api-1.0.0.jar`
- Deploy to cloud (AWS, Azure, GCP, Heroku, etc.)

---

## 📖 Documentation Map

| Document | Purpose | Read When |
|----------|---------|-----------|
| **README.md** | Getting started & API guide | First time setup |
| **ARCHITECTURE.md** | System design & details | Want to understand design |
| **PROJECT_SUMMARY.md** | Project overview | Need quick summary |
| **FINAL_REPORT.md** | Complete report | Need all details |
| **FILE_INVENTORY.md** | File listing | Need to find files |
| **COMPLETION_CHECKLIST.md** | Verification checklist | Need to verify completion |
| **QUICK_START.md** | This file - Fast start | Need to run quickly |

---

## 🔗 Useful Links

### Local Services
- API: http://localhost:8092/property-view/hotels
- Swagger: http://localhost:8092/swagger-ui.html
- API Docs: http://localhost:8092/v3/api-docs
- H2 Console: http://localhost:8092/h2-console

### External Resources
- Spring Boot Docs: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Swagger/OpenAPI: https://swagger.io/
- Maven: https://maven.apache.org/

### Git Repository (if connected)
```bash
git log --oneline
git status
git add .
git commit -m "Your message"
git push origin main
```

---

## 💡 Tips & Tricks

### Tip 1: Fast Development Loop
```bash
# Terminal 1: Run application
mvn spring-boot:run

# Terminal 2: Run tests in watch mode
mvn test -t
```

### Tip 2: Skip Tests During Build
```bash
mvn clean package -DskipTests
```

### Tip 3: Debug Mode
```bash
# Add breakpoints in IDE, then run in debug mode
mvn spring-boot:run -Dagentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
```

### Tip 4: View SQL Queries
Already enabled in `application.yml`:
```yaml
spring:
  jpa:
    show-sql: true
```

### Tip 5: Check Dependencies
```bash
mvn dependency:tree
```

---

## 🎯 Common Tasks

### Add New Endpoint
1. Create method in `HotelController.java`
2. Add service method in `HotelService.java` / `HotelServiceImpl.java`
3. Add @Operation and @ApiResponse annotations for Swagger
4. Write test in `HotelPropertyViewApplicationTests.java`
5. Run `mvn test` to verify

### Add New Database Field
1. Modify entity in `entity/` folder
2. Create new Liquibase changeset in `db/changelog/`
3. Add migration SQL (ALTER TABLE)
4. Update DTOs in `dto/` folder
5. Update mapper in `mapper/HotelMapper.java`
6. Update tests

### Add New Search Filter
1. Modify `HotelSpecification.java`
2. Add new predicate condition
3. Update `HotelService.searchHotels()` method
4. Add query parameter to `HotelController.searchHotels()`
5. Update test in `HotelPropertyViewApplicationTests.java`

---

## 📞 Support & Questions

### Project Documentation
- See **README.md** for API reference
- See **ARCHITECTURE.md** for design details
- See **FINAL_REPORT.md** for complete information

### Code Documentation
- All classes have JavaDoc comments
- All methods have detailed comments
- All endpoints have @Operation annotations

### Testing
- Run tests with `mvn test`
- Check test output for errors
- View test coverage with `mvn jacoco:report`

---

## ✅ Verification Checklist

Before considering setup complete, verify:

- [ ] Application starts without errors
- [ ] Can access http://localhost:8092/property-view/hotels
- [ ] Swagger UI loads at http://localhost:8092/swagger-ui.html
- [ ] All tests pass with `mvn test`
- [ ] Can create a new hotel via POST endpoint
- [ ] Can search hotels via GET /search
- [ ] H2 Console accessible at http://localhost:8092/h2-console
- [ ] Git repository initialized (if applicable)

---

**Ready to start?** Run `mvn spring-boot:run` and visit http://localhost:8092/swagger-ui.html! 🎉

---

*Last Updated: 2026-04-25 15:05 UTC*  
*For complete documentation, see README.md and ARCHITECTURE.md*
