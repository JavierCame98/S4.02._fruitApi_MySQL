# Fruit API - MySQL

A RESTful API for managing fruit inventory with provider management, built with Spring Boot and MySQL.

## Overview

This project implements a CRUD system for managing fruits and their suppliers. Each fruit is associated with a provider, allowing you to track the origin of each product and query which fruits are supplied by each company.

## Technology Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**
- **Docker & Docker Compose**
- **Lombok**
- **Jakarta Validation**

## Project Structure

```
src/
├── main/java/cat/itacademy/s04/t02/n02/fruit/
│   ├── controller/        # REST controllers
│   ├── model/             # Entities and DTOs
│   ├── repository/        # JPA repositories
│   ├── service/           # Business logic
│   ├── mappers/           # Entity-DTO converters
│   └── exceptions/        # Custom exceptions and handlers
├── main/resources/
│   └── application.properties
└── test/                  # Unit and integration tests
```

## Database Schema

### Provider Entity
| Field    | Type   | Constraints        |
|----------|--------|-------------------|
| id       | Long   | Primary Key, Auto |
| name     | String | Unique, Not Null  |
| country  | String | Not Null          |

### Fruit Entity
| Field        | Type      | Constraints        |
|--------------|-----------|-------------------|
| id           | Long      | Primary Key, Auto |
| name         | String    | Unique, Not Null  |
| weightKg     | Double    | Not Null, Positive|
| provider     | Provider  | ManyToOne, Not Null|

## API Endpoints

### Providers

| Method | Endpoint                | Description              |
|--------|-------------------------|--------------------------|
| POST   | `/providers/add`        | Create a new provider    |
| GET    | `/providers/getAll`     | List all providers       |
| GET    | `/providers/getById/{id}`| Get provider by ID      |
| PUT    | `/providers/update/{id}`| Update a provider        |
| DELETE | `/providers/delete/{id}`| Delete a provider        |

### Fruits

| Method | Endpoint                    | Description                    |
|--------|-----------------------------|--------------------------------|
| POST   | `/fruits/add`               | Create a new fruit             |
| GET    | `/fruits/getAll`            | List all fruits                |
| GET    | `/fruits/getOne/{id}`        | Get fruit by ID                |
| GET    | `/fruits?providerId={id}`    | Get fruits by provider         |
| PUT    | `/fruits/update/{id}`       | Update a fruit                 |
| DELETE | `/fruits/delete/{id}`       | Delete a fruit                 |

## Request/Response Examples

### Create Provider
```json
POST /providers/add
Content-Type: application/json

{
  "name": "Fresh Farms",
  "country": "Spain"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Fresh Farms",
  "country": "Spain"
}
```

### Create Fruit
```json
POST /fruits/add
Content-Type: application/json

{
  "name": "Apple",
  "weightKg": 5.0,
  "providerId": 1
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "Apple",
  "weightKg": 5.0,
  "providerId": 1
}
```

## Configuration

Environment variables for database connection:

| Variable     | Description                     | Default |
|--------------|---------------------------------|---------|
| `DB_URL`     | JDBC URL for MySQL              | -       |
| `DB_USER`     | Database username               | -       |
| `DB_PASSWORD` | Database password              | -       |

## Running the Application

### Prerequisites
- Java 21
- Maven 3.6+
- Docker & Docker Compose

### Local Development

1. **Start MySQL with Docker Compose:**
   ```bash
   docker-compose up -d mysql
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Run tests:**
   ```bash
   ./mvnw test
   ```

### Docker Deployment

Build and run the entire stack:
```bash
docker-compose up --build
```

## HTTP Status Codes

| Code | Description                                    |
|------|------------------------------------------------|
| 200  | OK - Successful GET/PUT                        |
| 201  | Created - Successful POST                     |
| 204  | No Content - Successful DELETE                 |
| 400  | Bad Request - Validation failed                |
| 404  | Not Found - Resource doesn't exist             |
| 409  | Conflict - Resource already exists             |
| 500  | Internal Server Error                         |

## Business Rules

- Provider names must be unique and non-empty
- Fruits require an associated provider
- A provider cannot be deleted if it has associated fruits
- Fruit names must be unique within the database

## License

This project is for educational purposes.
