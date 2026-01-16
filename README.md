# Gym CRM System 

## Overview

**Gym CRM Core Service** is the central microservice of the Gym CRM system responsible for managing
users, trainees, trainers, and training sessions.
The service exposes a secured REST API, handles authentication and authorization,
manages core business logic, and **publishes trainer workload events to a dedicated Trainer Workload Service via RabbitMQ**.

The application is built with **Spring Boot** and follows **Onion Architecture** principles.

---

## Responsibilities

This service is responsible for:

- Managing Trainees and Trainers
- Authentication and JWT-based authorization
- Training session lifecycle management
- Trainer–Trainee assignments
- Publishing trainer workload events asynchronously to the Trainer Workload Service
- Metrics, logging, and health monitoring

---

## Key Features

- Trainee and Trainer registration with auto-generated credentials
- JWT-based authentication and authorization
- Change password functionality
- Activate / deactivate Trainee and Trainer accounts
- Update Trainee and Trainer profiles
- Assign and unassign Trainers to Trainees
- Training management:
    - Create training
    - Delete training
    - Retrieve trainings with filters
- Read-only access to training types
- Brute-force protection for login attempts
- Logout functionality
- Publishing trainer workload events to an external service
- Metrics and health monitoring

---

> All functions except registration require authentication.

---

## Architecture

The service follows **Onion Architecture**:

- **Domain layer** – core business entities and repository interfaces
- **Application layer** – business use cases
- **Infrastructure layer** – persistence, security, integrations
- **Presentation layer** – REST controllers and DTOs

---

## Database + RabbitMQ Setup (Docker Compose)

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

  rabbitmq:
    image: rabbitmq:3-management
    container_name: gym-crm-rabbitmq
    environment:
      RABBITMQ_DEFAULT_USER: gymuser
      RABBITMQ_DEFAULT_PASS: pass
    ports:
      - "5672:5672"
      - "15672:15672"
    restart: always

volumes:
  postgres_data:

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

1. **Start PostgreSQL + RabbitMQ via Docker Compose**:

```bash
docker-compose up -d
```

2. **Configure Hibernate**:

```properties
spring:
    datasource:
        url: jdbc:postgresql://localhost:5432/gymdb
        username: gymuser
        password: pass
    jpa:
        hibernate:
            ddl-auto: update
```

3. **Configure Security**:

```properties
security:
    jwt:
        user:
            expiration-ms: 3600000
        service:
            expiration-ms: 300000
    login:
        max-attempts: 3
        block-minutes: 5
```

4. **Configure RabbitMQ**:

```properties
spring:
    rabbitmq:
        host: localhost
        port: 5672
        username: gymuser
        password: pass
        publisher-confirm-type: correlated
        publisher-returns: true
```

5. **Run the application**:

```bash
mvn spring-boot:run
```

6. **Metrics & Health Endpoints**:

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
3. TransactionId is generated for each operation and propagated through messages sent to the Trainer Workload Service, allowing tracking across services.

---

## Inter-Service Communication

1. Trainer workload events are sent asynchronously to the Trainer Workload Service via **RabbitMQ**.
2. Messages are serialized in **JSON** format.
3. Dead Letter Queue (DLQ) is used for messages with missing or invalid information.
4. Concurrency of consumers is configured for horizontal scaling.
5. Outgoing calls are protected with **Resilience4j Circuit Breaker** to prevent service failures if RabbitMQ is unavailable.

---

## Environments

The project supports multiple Spring profiles (`local`, `dev`, `stg`, `prod`) for different environments.
For this project, only the `local` profile is actively used; other profiles are present for demonstration purposes.

---

## RabbitMQ Management

The Trainer Workload Service uses RabbitMQ for asynchronous message processing.  
You can monitor queues, messages, and dead letter queues using the **RabbitMQ Management UI**:

- URL: [http://localhost:15672](http://localhost:15672)
- Default credentials (for local development):
    - Username: `gymuser`
    - Password: `pass`

Through this interface you can:
- Inspect messages in the `trainer-workload` queue
- View messages moved to the Dead Letter Queue (DLQ)
- Monitor consumer concurrency and message throughput
- Manually publish or requeue messages if necessary

---

## Project Structure

```
com.gymcrm
 ├─ application
 │   ├─ event         # Message events (TrainerWorkloadEvent)
 │   ├─ request       # helpers for create/update operations
 │   ├─ response      # helpers for service responses
 │   └─ service/port & impl  # Orchestrators of business use cases
 ├─ domain
 │   ├─ model         # Entities: User, Trainee, Trainer, Training, TrainingType
 │   └─ port          # Repository interfaces
 ├─ infrastructure
 │   ├─ adapter       # Adapters 
 │   ├─ config        # Rabbit, Swagger, Security
 │   ├─ dao           # DAO classes
 │   ├─ messaging     # Producers 
 │   ├─ mapper        # Entity ↔ DAO mappers
 │   ├─ jpa           # JPARepositories
 │   ├─ logging       # Filter for transaction logging
 │   ├─ metrics       # Custom metrics service + AOP aspect
 │   ├─ health        # Custom health indicators
 │   └─ security      # JWT, Brute-force protection, filter, custom user details, authprovider
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
10. Spring Data JPA
11. Spring Web / Spring MVC 
12. Resilience4j 
13. Jakarta Validation 
14. Lombok 
15. Spring Boot Actuator 
16. Spring AOP 
17. Spring AMQP / RabbitMQ

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
9. All trainer workload events (ADD/DELETE) are now processed asynchronously via RabbitMQ.
10. Dead Letter Queue (DLQ) stores messages with missing required fields.

