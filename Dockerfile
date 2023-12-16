# Use a base image that includes JDK and Maven
# Replace with the specific version of JDK you need, here I'm using JDK 17 as an example
FROM maven:3.8.5-openjdk-17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container at /app
# COPY . /app

# Compile and run the application
# Note: The exec format CMD ["command", "param1", "param2"] is used here
CMD ["mvn", "clean", "compile", "exec:java", "-Dexec.mainClass=com.filavents.books_highlights.MainVerticle"]
