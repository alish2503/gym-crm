
# Gym CRM System (Spring Core)

## Overview
**Gym CRM System** is a Spring-based module for managing **Trainees**, **Trainers**, and **Training sessions** in a simple in-memory CRM. It demonstrates key Spring features including **dependency injection, bean configuration, auto-wiring, and bean post-processing**, along with **unit testing** and **logging**.

---

## Features

- **Trainee Service**: Create, update, delete, and list trainees. Automatically generates a **username** and a random **10-character password**.  
- **Trainer Service**: Create, update, and list trainers. Username and password generation similar to trainees.  
- **Training Service**: Create and list training sessions.  
- **In-memory Storage**: Stores each entity type in a separate namespace (`Map`) and can initialize data from external files.  
- **Facade Pattern**: Provides a single entry point to all services.  
- **Logging**: Tracks operations for auditing and debugging.  

---

## Launch

Configure `application.properties`:
```properties
storage.init.file=data/initial-data.csv
```

Build and run with Maven:
```bash
mvn exec:java -Dexec.mainClass="com.gymcrm.GymApp"
```

---

## Usage

- Run the `GymApp` class.  
- Interact with services through the facade:
```java
GymFacade facade = context.getBean(GymFacade.class);

Trainee trainee = facade.createTrainee(new Trainee("John", "Doe", LocalDate.of(1995, 1, 1), "NY"));
Trainer trainer = facade.createTrainer(new Trainer("Alex", "Stone", new TrainingType(TrainingTypeEnum.YOGA)));
Training training = facade.createTraining(new Training("Morning Yoga", new TrainingType(TrainingTypeEnum.YOGA),
                LocalDate.of(2025, 10, 22), 60, trainer, trainee));
```

- Usernames are automatically generated: `FirstName.LastName`.  
- Duplicate names get numeric suffixes (e.g., `John.Doe2`).  
- Passwords are random 10-character strings.  

---

## Project Structure

```
src/main/java
 ├─ aop
 ├─ config 
 ├─ model 
 ├─ dao  
 ├─ service
 ├─ storage
 ├─ facade
 ├─ util 
 └─ GymApp.java

src/test/java
 └─ (unit tests for Service layers)
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
- JUnit 5  
- SLF4J / Logback for logging  

---

## Notes

- All data is **in-memory** and not persisted to a database.  
- Username collisions are automatically handled.  
- Passwords are random 10-character alphanumeric strings.  
- External files can initialize storage at startup using Spring bean post-processing.  

---


