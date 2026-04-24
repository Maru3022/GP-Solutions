FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/hotel-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8092

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]
