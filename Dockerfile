# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . /app
# Run Maven build
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with only the compiled application
FROM openjdk:17-slim
WORKDIR /app
# Copy only the artifacts we need from the first stage and discard the rest
COPY --from=build /app/target/books-highlights-1.0.0-SNAPSHOT.jar /app/books-highlights-1.0.0-SNAPSHOT.jar

# Run the application
CMD ["java", "-jar", "/app/books-highlights-1.0.0-SNAPSHOT.jar"]
