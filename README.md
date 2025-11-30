# Gym CRM System 

## Overview

**Gym CRM System** is a Spring-based CRM module for managing **Trainees**, **Trainers**, and **Training sessions**, with **REST API**, **Spring Security**, and **Micrometer metrics**.
It demonstrates key concepts such as:

1. Hibernate ORM mapping
2. Many-to-Many, One-to-One, One-to-Many, Many-to-One relationships
3. Transaction management
4. Custom repository implementations
5. REST API with Swagger/OpenAPI documentation
6. Authentication and password management using Spring Security and JWT
7. Brute-force protection and logout functionality
8. Unit testing with **JUnit 5**
9. Logging at transaction and REST call levels using **SLF4J/Logback**
10. **Health indicators** and **custom metrics** with Prometheus

---

## Features

1. Trainee and Trainer registration (automatic username/password generation)
2. Authentication and JWT-based authorization
3. Change password functionality
4. Get, update, activate/deactivate, and delete Trainee/Trainer profiles
5. Assign/unassign Trainers to Trainees
6. Manage Training sessions:
    - Add Training
    - Get Trainee/Trainer Trainings with filtering (date range, type, participant)
7. Fetch Training types (read-only)
8. Brute-force attack protection (lock user after 3 failed login attempts)
9. Logout functionality
10. **Metrics**:
    - Count of Trainings created (`custom.trainings.created`)
    - Active users gauge (`custom.users.active`)
    - Timing of controller methods (`getTrainingsForTrainee`, `getTrainingsForTrainer`)
11. **Health checks**:
    - Database connectivity
    - Presence of Training Types
12. Swagger/OpenAPI documentation for all endpoints

> All functions except registration require authentication.

---

## Database Setup (PostgreSQL + Docker Compose)

Example `docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: gym-crm-postgres
    restart: always
    environment:
      POSTGRES_DB: gymdb
      POSTGRES_USER: gymuser
      POSTGRES_PASSWORD: pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

> On container startup, `import.sql` automatically populates the `training_type` reference table.

### import.sql (Training Types)

```sql
INSERT INTO training_type (id, name) VALUES (1, 'FITNESS');
INSERT INTO training_type (id, name) VALUES (2, 'YOGA');
INSERT INTO training_type (id, name) VALUES (3, 'ZUMBA');
INSERT INTO training_type (id, name) VALUES (4, 'STRETCHING');
INSERT INTO training_type (id, name) VALUES (5, 'RESISTANCE');
```

---

## Launch

1. **Start PostgreSQL via Docker Compose**:

```bash
docker-compose up -d
```

2. **Configure Hibernate (`application.properties`)**:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gymdb
spring.datasource.username=gymuser
spring.datasource.password=pass
spring.jpa.hibernate.ddl-auto=update
```

3. **Configure Security (`application.properties`)**:

```properties
security.jwt.secret=8r7u6y5t4r3e2w1q0p9o8i7u6y5t4r3e
security.jwt.expiration-ms=3600000
security.login.max-attempts=3
security.login.block-minutes=5
security.cors.allowed-origins=http://localhost:3000
```

4. **Run the application**:

```bash
mvn spring-boot:run
```

5. **Metrics & Health Endpoints**:

* Prometheus metrics: `http://localhost:8080/actuator/prometheus`
* Health check: `http://localhost:8080/actuator/health`
* Exposed via Spring Boot Actuator

---

## REST API

1. All endpoints are documented using **Swagger/OpenAPI**.
2. Access the Swagger UI at: `http://localhost:8080/swagger-ui/index.html`
3. Registration endpoints are public; all other endpoints require JWT authentication.
4. Input validation and error handling are implemented for all endpoints.

---

## Security

1. **Authentication** via username/password
2. **Authorization** via JWT tokens
3. **Password storage**: salted and hashed
4. **Brute-force protection**: 3 failed login attempts → 5-minute lock
5. **Logout** endpoint invalidates JWT tokens
6. CORS policy is configured for frontend access

---

## Logging

Two levels of logging are implemented:

1. **Transaction-level**: A unique `transactionId` is generated for each operation, which can be tracked across services.
2. **REST call-level**: Logs the endpoint called, request payload, response payload, response status, and message.

---

## Environments

The project supports multiple Spring profiles (`local`, `dev`, `stg`, `prod`) for different environments.
For this project, only the `local` profile is actively used; other profiles are present for demonstration purposes.

---

## Project Structure

```
com.gymcrm
 ├─ application
 │   ├─ request       # helpers for create/update operations
 │   ├─ response      # helpers for service responses
 │   └─ service/port & impl  # Business logic
 ├─ domain
 │   ├─ model         # Entities: User, Trainee, Trainer, Training, TrainingType
 │   └─ port          # Repository interfaces
 ├─ infrastructure
 │   ├─ config        # Hibernate, Swagger, Security, Profiles
 │   ├─ dao           # DAO classes
 │   ├─ mapper        # Entity ↔ DAO mappers
 │   ├─ repository    # Repository implementations
 │   ├─ logging       # Filter for transaction logging
 │   ├─ metrics       # Custom metrics service + AOP aspect
 │   ├─ health        # Custom health indicators
 │   └─ security      # JWT, Brute-force protection, filter, custom user details
 └─ presentation
     ├─ controller    # REST controllers
     ├─ dto           # REST request/response DTOs
     ├─ mapper        # DTO ↔ Entity mappers
     └─ advice        # Global exception handling
```

---

## Testing

1. Unit tests with **JUnit 5** covering services, repositories, and controllers.
2. Run tests via:

```bash
mvn test
```

---

## Dependencies

1. Java 8+
2. Spring Boot / Spring Core
3. Spring Security + JWT
4. Hibernate ORM
5. PostgreSQL
6. Micrometer / Prometheus
7. JUnit 5
8. SLF4J / Logback
9. Swagger/OpenAPI

---

## Notes

1. Username cannot be changed.
2. Trainee/Trainer many-to-many relationship is enforced.
3. Activate/Deactivate actions are not idempotent.
4. Deleting a Trainee performs a cascade deletion of associated Trainings.
5. Training types are constant and read-only.
6. All endpoints have proper validation and error handling.
7. Swagger provides interactive API documentation.
8. Metrics and health endpoints are exposed via Spring Boot Actuator.
