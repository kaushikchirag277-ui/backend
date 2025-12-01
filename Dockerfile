
# Stage 1 — Build using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# This will run Maven inside the container (mvnd NOT required)
RUN mvn -e -X clean package -DskipTests
# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /hello-world-api

# Copy the built JAR from Maven
COPY --from=build /app/target/hello-world-api-0.0.1-SNAPSHOT.jar app.jar
# Environment variables (Render will override these)
ENV MONGO_URI="mongodb+srv://user:1234@cluster0.tltzqod.mongodb.net/"
ENV DB_NAME="Kx"

# Expose your backend port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
