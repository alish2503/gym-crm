
# Gym CRM System (Spring Core)

## Overview
**Gym CRM System** is a Spring-based module for managing **Trainees**, **Trainers**, and **Training sessions** using **Hibernate** and **PostgreSQL**.  
It demonstrates key concepts such as:

- Hibernate ORM mapping
- Many-to-Many, One-to-One, One-to-Many, Many-to-One relationships
- Transaction management
- Custom repository implementations
- Authentication and password management
- Unit testing with **JUnit 5**
- Logging using **SLF4J/Logback**

This module replaces the previous in-memory CRM implementation with a real database-backed solution.

---

## Features

- Create Trainer profile
- Create Trainee profile 
- Trainee username and password matching (authentication)
- Trainer username and password matching (authentication)
- Select Trainer profile by username 
- Select Trainee profile by username 
- Trainee password change 
- Trainer password change 
- Update Trainer profile 
- Update Trainee profile 
- Activate/Deactivate Trainee 
- Activate/Deactivate Trainer 
- Delete Trainee profile by username (hard delete with cascade deletion of trainings)
- Get Trainee Trainings List by trainee username and criteria (from date, to date, trainer name, training type)
- Get Trainer Trainings List by trainer username and criteria (from date, to date, trainee name)
- Add training 
- Get Trainers list not assigned to a Trainee by trainee username 
- Update Trainee's trainers list

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

---

## import.sql (Training Types)

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

2. **Hibernate Configuration (`GymAppConfig.java`)**:

    - URL: `jdbc:postgresql://localhost:5432/gymdb`
    - Username: `gymuser`
    - Password: `pass`
    - Dialect: `org.hibernate.dialect.PostgreSQLDialect`

3. **Run the application**:

    ```bash
    mvn exec:java -Dexec.mainClass="com.gymcrm.GymApp"
    ```

---

## Project Structure

```
com.gymcrm
 ├─ application
 │   ├─ facade           # GymFacade and implementation
 │   ├─ service          # Services: Trainee, Trainer, Training, Auth
 │   └─ request          # Auxiliary classes for Create/Update operations
 ├─ domain
 │   ├─ model            # domain entities: User, Trainee, Trainer, Training, TrainingType
 │   └─ port             # Repository interfaces
 ├─ infrastructure
 │   ├─ config           # Hibernate + DataSource configuration
 │   ├─ mapper           # Domain entity ↔ DAO mappers
 │   ├─ persistence/dao  # DAO classes
 │   └─ repository       # Hibernate repositories
 └─ GymApp.java          # Application entry point
```

---

## Testing

- Unit tests use **JUnit 5**.  
- Covers CRUD operations, username/password generation:
```bash
mvn test
```

---

## Dependencies

- Java 8+  
- Spring Core  
- Hibernate ORM
- PostgreSQL
- JUnit 5  
- SLF4J / Logback for logging  

---


