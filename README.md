# AI-Powered Car Management & Fuel Tracking System

A comprehensive Java-based car management system with REST API backend and command-line interface client.

## 🏗️ Architecture Overview

The system consists of two modules:

1. **Backend API Server** - Spring Boot REST API with custom servlet
2. **CLI Client** - Java command-line interface for API interaction

### Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Data Storage**: Thread-safe in-memory repositories (ConcurrentHashMap)
- **Validation**: Bean Validation (JSR-380)
- **Logging**: SLF4J with Logback
- **JSON Processing**: Jackson

## 📁 Project Structure

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

## 🚀 Getting Started

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

## 📡 Backend API Server

### Running the Server

```bash
cd backend-api
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

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
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018,
  "fuelEntries": []
}
```

#### 2. Get All Cars
```http
GET /api/cars
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2018,
    "fuelEntries": []
  }
]
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
  "id": 1,
  "liters": 45.5,
  "price": 65.50,
  "odometer": 10500,
  "timestamp": "2025-12-30T21:00:00"
}
```


#### 5. Get Fuel Statistics (REST)
```http
GET /api/cars/{id}/fuel/stats
```

**Response**: `200 OK`
```json
{
  "totalFuel": 90.0,
  "totalCost": 130.00,
  "averageConsumption": 7.5
}
```

### 📖 API Documentation (Swagger/OpenAPI)

Interactive API documentation is available when the server is running. You can use this UI to explore endpoints and execute requests directly from your browser.

**URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Servlet Endpoint

#### Get Fuel Statistics (Servlet)
```http
GET /servlet/fuel-stats?carId=1
```

**Response**: Same as REST endpoint, but processed through custom servlet

## 💻 CLI Client

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

**Output**:
```
✓ Car created successfully!
  Brand:  Toyota
  Model:  Corolla
  Year:   2018
  ID:     1
```

#### 2. Add Fuel Entry

```bash
java -jar target/car-cli.jar add-fuel --carId 1 --liters 45.5 --price 65.50 --odometer 10500
```

**Output**:
```
✓ Fuel entry added successfully!
  Entry ID:  1
  Liters:    45.50 L
  Price:     65.50
  Odometer:  10500 km
  Timestamp: 2025-12-30T21:00:00
```

#### 3. Get Fuel Statistics

```bash
java -jar target/car-cli.jar fuel-stats --carId 1
```

**Output**:
```
==================================================
  Fuel Statistics for Car ID 1
==================================================
  Total Fuel:          90.00 L
  Total Cost:          130.00
  Average Consumption: 7.50 L/100km
==================================================
```

#### 4. Display Help

```bash
java -jar target/car-cli.jar help
```

## 🔍 Key Features

### Backend API

1. **Thread-Safe In-Memory Storage**
   - Uses `ConcurrentHashMap` for concurrent access
   - `AtomicLong` for ID generation
   - No database required

2. **Comprehensive Validation**
   - Bean Validation annotations on DTOs
   - Business logic validation in service layer
   - Odometer reading progression validation

3. **Global Exception Handling**
   - Structured error responses
   - Field-level validation errors
   - Proper HTTP status codes

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
   - Formatted output with visual separators
   - Comprehensive error messages

2. **Robust Error Handling**
   - Connection errors
   - API errors (404, 400, 500)
   - Validation errors
   - Proper exit codes

3. **HTTP Client**
   - Java 11+ HttpClient
   - JSON serialization/deserialization
   - Automatic error parsing

## 🧪 Testing the System

### Complete Workflow Example

1. **Start the backend server**:
```bash
cd backend-api
mvn spring-boot:run
```

2. **In a new terminal, create a car**:
```bash
cd cli-client
java -jar target/car-cli.jar create-car --brand Honda --model Civic --year 2020
```

3. **Add first fuel entry**:
```bash
java -jar target/car-cli.jar add-fuel --carId 1 --liters 40.0 --price 58.00 --odometer 10000
```

4. **Add second fuel entry**:
```bash
java -jar target/car-cli.jar add-fuel --carId 1 --liters 42.0 --price 60.50 --odometer 10550
```

5. **Get statistics**:
```bash
java -jar target/car-cli.jar fuel-stats --carId 1
```

Expected output shows:
- Total Fuel: 82.00 L
- Total Cost: 118.50
- Average Consumption: ~7.45 L/100km

## 📊 Data Models

### Car Entity
- `Long id` - Auto-generated unique identifier
- `String brand` - Vehicle manufacturer
- `String model` - Vehicle model name
- `Integer year` - Manufacturing year (1900-2100)
- `List<FuelEntry> fuelEntries` - Associated fuel records

### FuelEntry Entity
- `Long id` - Unique entry identifier
- `Double liters` - Fuel quantity (positive value)
- `Double price` - Total cost (positive value)
- `Integer odometer` - Mileage reading (positive, must increase)
- `LocalDateTime timestamp` - Auto-generated timestamp
- `Car car` - Reference to parent car

### FuelStats DTO
- `Double totalFuel` - Sum of all liters
- `Double totalCost` - Sum of all prices
- `Double averageConsumption` - L/100km (null if < 2 entries)

## 🔒 Validation Rules

### Car Creation
- Brand: Required, not blank
- Model: Required, not blank
- Year: Required, 1900-2100, cannot be more than 1 year in future

### Fuel Entry
- Liters: Required, positive value
- Price: Required, positive value
- Odometer: Required, positive value, must exceed previous readings

## 🌐 API Error Responses

All errors return structured JSON:

```json
{
  "timestamp": "2025-12-30T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Car not found with ID: 99",
  "errorCode": "CAR_NOT_FOUND"
}
```

### HTTP Status Codes
- `200 OK` - Successful GET request
- `201 Created` - Successful POST request
- `400 Bad Request` - Validation error or invalid input
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Unexpected server error

## 📝 Logging

The backend uses SLF4J with Logback for comprehensive logging:

- **DEBUG**: Service operations, repository interactions
- **INFO**: HTTP requests, successful operations
- **ERROR**: Exceptions, validation failures

Log pattern:
```
2025-12-30 21:00:00 [thread-name] LEVEL logger-name - message
```

## 🎯 Design Patterns Used

1. **Repository Pattern** - Data access abstraction
2. **Service Layer Pattern** - Business logic separation
3. **DTO Pattern** - Data transfer and validation
4. **Dependency Injection** - Spring-managed components
5. **Global Exception Handler** - Centralized error handling
6. **Command Pattern** - CLI command execution
7. **Factory Pattern** - HTTP client creation

## 🚀 Performance Considerations

- Thread-safe concurrent collections
- In-memory storage for fast access
- Efficient stream operations for statistics
- No database overhead
- Stateless REST API

## 📄 License

This project is part of a code challenge demonstration.

## 👤 Author

Elijah Laddie

## 🤝 Contributing

This is a demonstration project. For actual deployment, consider:
- Persistent database (PostgreSQL, MySQL)
- Authentication and authorization
- Rate limiting
- Caching layer
- Containerization (Docker)
- CI/CD pipeline
