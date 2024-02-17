# Stage 1: Build the Java application
FROM maven:3.8.5-openjdk-17-slim AS java-build
WORKDIR /app
COPY . /app
# Run Maven build
RUN mvn clean package -DskipTests

# Stage 2: Build the Angular application
FROM node:14 AS angular-build
WORKDIR /web-app
COPY web/playbooks /web-app
RUN npm install
RUN npm run ng build --prod

# Stage 3: Create the final image with only the compiled Java application and Angular dist folder
FROM openjdk:17-slim
WORKDIR /app
# Copy the Java application
COPY --from=java-build /app/target/books-highlights-1.0.0-SNAPSHOT.jar /app/books-highlights-1.0.0-SNAPSHOT.jar
# Copy the Angular dist folder
COPY --from=angular-build /web-app/dist /app/web/playbooks/dist

# Run the application
CMD ["java", "-jar", "/app/books-highlights-1.0.0-SNAPSHOT.jar"]
