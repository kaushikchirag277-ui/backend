# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /hello-world-api

# Copy the built JAR from Maven
COPY target/hello-world-api-0.0.1-SNAPSHOT.jar app.jar
# Environment variables (Render will override these)
ENV MONGO_URI="mongodb+srv://user:1234@cluster0.tltzqod.mongodb.net/"
ENV DB_NAME="Kx"

# Expose your backend port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
