# Car Management & Fuel Tracking System

A Java-based car management system with REST API backend and command-line interface client.

## Architecture Overview

The system consists of two modules:

1. **Backend API Server** - Spring Boot REST API with custom servlet
2. **CLI Client** - Java command-line interface for API interaction

### Technology Stack

- **Framework**: Spring Boot
- **Language**: Java
- **Build Tool**: Maven
- **Data Storage**: in-memory storage
- **Validation**: Bean Validation
- **Logging**
- **JSON Processing**

## Project Structure

```
car-management-and-fuel-tracking/
├── backend-api/              # Spring Boot REST API Server
│   ├── src/main/java/com/carmanagement/
│   │   ├── model/           # Car and FuelEntry entities
│   │   ├── repository/      # In-memory data repositories
│   │   ├── service/         # Business logic layer
│   │   ├── controller/      # REST API controllers
│   │   ├── servlet/         # Custom servlet implementation
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # Exception handling
│   │   └── config/         # Configuration classes
│   └── src/main/resources/
│       ├── application.properties
│       └── logback.xml
│
└── cli-client/              # Command Line Interface Client
    └── src/main/java/com/carmanagement/cli/
        ├── client/         # HTTP API client
        ├── model/          # CLI data models
        ├── command/        # Command execution logic
        ├── parser/         # Command-line argument parser
        └── Main.java       # CLI entry point
```

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Maven 3.6+
- Internet connection (for Maven dependencies)

### Building the Projects

#### 1. Build Backend API

```bash
cd backend-api
mvn clean package
```

#### 2. Build CLI Client

```bash
cd cli-client
mvn clean package
```

## Backend API Server

### Running the Server

```bash
cd backend-api
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

### API Response Format

All API endpoints return a standardized JSON structure relative to the instructions provided.

#### Response Object Definition

| Field | Type | Description |
|-------|------|-------------|
| `success` | Boolean | Indicates if the operation was successful (`true`) or failed (`false`). |
| `resp_msg` | String | A descriptive message about the operation result (e.g., "Success", "Car not found"). |
| `resp_code` | Integer | Application-specific response code. Default: `100` for success, `101` for generic error. |
| `data` | Object | The actual payload of the response. Can be an object, list, or null depending on the endpoint. |
| `errors` | Object/Null | Contains detailed validation errors or error objects if `success` is false. Null on success. |
| `pagination` | Object/Null | Pagination details if applicable (optional). |

#### Standard Response Example (Success)

```json
{
  "success": true,
  "resp_msg": "Success",
  "resp_code": 100,
  "data": { ... },
  "errors": null
}
```

#### Standard Response Example (Error)

```json
{
  "success": false,
  "resp_msg": "Car not found with ID: 99",
  "resp_code": 101,
  "data": null,
  "errors": null
}
```

### REST API Endpoints

#### 1. Create Car
```http
POST /api/cars
Content-Type: application/json

{
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018
}
```

**Response**: `201 Created`
```json
{
  "success": true,
  "resp_msg": "Car created successfully",
  "resp_code": 100,
  "data": {
    "id": 1,
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2018,
    "fuelEntries": []
  },
  "errors": null
}
```

#### 2. Get All Cars
```http
GET /api/cars
```

**Response**: `200 OK`
```json
{
  "success": true,
  "resp_msg": "Success",
  "resp_code": 100,
  "data": [
    {
      "id": 1,
      "brand": "Toyota",
      "model": "Corolla",
      "year": 2018,
      "fuelEntries": []
    }
  ],
  "errors": null
}
```

#### 3. Get Car by ID
```http
GET /api/cars/{id}
```

**Response**: `200 OK` or `404 Not Found`

#### 4. Add Fuel Entry
```http
POST /api/cars/{id}/fuel
Content-Type: application/json

{
  "liters": 45.5,
  "price": 65.50,
  "odometer": 10500
}
```

**Response**: `201 Created`
```json
{
  "success": true,
  "resp_msg": "Fuel entry added successfully",
  "resp_code": 100,
  "data": {
    "id": 1,
    "liters": 45.5,
    "price": 65.50,
    "odometer": 10500,
    "timestamp": "2025-12-30T21:00:00"
  },
  "errors": null
}
```

#### 5. Get Fuel Statistics (REST)
```http
GET /api/cars/{id}/fuel/stats
```

**Response**: `200 OK`
```json
{
  "success": true,
  "resp_msg": "Success",
  "resp_code": 100,
  "data": {
    "totalFuel": 90.0,
    "totalCost": 130.00,
    "averageConsumption": 7.5
  },
  "errors": null
}
```

### API Documentation (Swagger/OpenAPI)

Interactive API documentation is available when the server is running. You can use this UI to explore endpoints and execute requests directly from your browser.

**URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Servlet Endpoint

#### Get Fuel Statistics (Servlet)
```http
GET /servlet/fuel-stats?carId=1
```

**Response**: Same as REST endpoint.

## CLI Client

### Running the CLI

Navigate to the `cli-client` directory and use:

```bash
java -jar target/car-cli.jar <command> <arguments>
```

### Available Commands

#### 1. Create a Car

```bash
java -jar target/car-cli.jar create-car --brand Toyota --model Corolla --year 2018
```

#### 2. Add Fuel Entry

```bash
java -jar target/car-cli.jar add-fuel --carId 1 --liters 45.5 --price 65.50 --odometer 10500
```

#### 3. Get Fuel Statistics

```bash
java -jar target/car-cli.jar fuel-stats --carId 1
```

#### 4. Display Help

```bash
java -jar target/car-cli.jar help
```

## Key Features

### Backend API

1. **In-Memory Storage**
   - Uses `ConcurrentHashMap` for concurrent access
   - `AtomicLong` for ID generation
   - No database required

2. **Validation**
   - Bean Validation annotations on DTOs
   - Business logic validation in service layer
   - Odometer reading progression validation

3. **Exception Handling**
   - Structured error responses via `ResponseHandler`
   - Field-level validation errors
   - HTTP status codes

4. **Dual Interface**
   - REST Controller endpoints
   - Custom Servlet implementation

5. **Fuel Consumption Calculation**
   - Formula: `(totalFuel / (maxOdometer - minOdometer)) * 100`
   - Requires minimum 2 fuel entries
   - Returns null if insufficient data

### CLI Client

1. **User-Friendly Interface**
   - Clear command syntax
   - Formatted output with separators
   - User friendly error messages

2. **Error Handling**
   - Connection errors
   - API errors (404, 400, 500)
   - Validation errors
   - Exit codes

3. **HTTP Client**
   - Java 11+ HttpClient
   - JSON serialization/deserialization
   - Automatic generic ApiResponse parsing

## Design Patterns Used

1. **Repository Pattern** - Data access abstraction
2. **Service Layer Pattern** - Business logic separation
3. **DTO Pattern** - Data transfer and validation
4. **Dependency Injection** - Spring-managed components
5. **Global Exception Handler** - Centralized error handling
6. **Command Pattern** - CLI command execution
7. **Factory Pattern** - HTTP client creation

## Author

Elie Kuradusenge
