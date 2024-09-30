# Stage 1: Build the Spring Boot application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Run Maven to build the application and skip tests
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy only the jar file from the first build stage
COPY --from=build /app/target/NaviGo-0.0.1-SNAPSHOT.jar NaviGo.jar

# Expose port 8080 for the application
EXPOSE 8080

# Set the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "NaviGo.jar"]
