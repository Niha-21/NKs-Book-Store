# Using official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre

# Setting working directory inside the container
WORKDIR /app

# Copying the jar file from target directory into the container
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

# Exposing the port Spring Boot app listens on
EXPOSE 8081

# Command to run the jar file
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar","app.jar"]
