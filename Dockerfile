# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build file to the container's work directory
COPY target/NaviGo-0.0.1-SNAPSHOT.jar /app/NaviGo.jar

# Expose the port that the application will listen on
EXPOSE 8080

# Default command to run the application
CMD ["java", "-jar", "/app/NaviGo.jar"]
