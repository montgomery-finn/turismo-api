# Use the official OpenJDK 17 image with Alpine Linux
FROM openjdk:17-jdk-alpine

# Copy the project files into the /app directory in the container
COPY . /app

# Set the working directory to /app
WORKDIR /app

# Give execution permissions to the Gradle wrapper script
RUN chmod +x gradlew

# Run Gradle to build the project
RUN ./gradlew build

# Define the entry point for running the application
ENTRYPOINT ["java", "-jar", "build/libs/turismo-api-0.0.1-SNAPSHOT.jar"]