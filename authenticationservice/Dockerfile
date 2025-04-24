# Use OpenJDK image as a base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/*.jar spring-job-auth-service.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar spring-job-auth-service.jar"]
