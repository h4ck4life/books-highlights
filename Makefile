# Makefile for Books-highlights

# Phony targets
.PHONY: test package run docker-build docker-run build-angular

# Default target
default: run

# Test the application
test:
	./mvnw clean test

# Package the application
package:
	./mvnw clean package -DskipTests

# Run the application
run:
	./mvnw clean compile exec:java

# Build Docker image
docker-build:
	docker build -t playbooks .

# Run Docker image
docker-run:
	docker run -v $(PWD)/.env:/app/.env -v $(PWD)/booknotes_full.sqlite:/app/booknotes_full.sqlite -p 8088:8088 playbooks

# Build Angular application
build-angular:
	cd web/playbooks && npm install && npx ng build --configuration production

# Help
help:
	@echo "Available commands:"
	@echo "  make test          - Run tests."
	@echo "  make package       - Package the application."
	@echo "  make run           - Run the application."
	@echo "  make docker-build  - Build Docker image."
	@echo "  make docker-run    - Run Docker image."
	@echo "  make build-angular - Build Angular application."
	@echo "  make native-image  - Build native image using GraalVM."
