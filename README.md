# Notification Service

Microservice for managing notifications, alerts, and communication across the system.

## Overview

The Notification Service handles sending notifications to users via email, SMS, or in-app messages for various events like leave approvals, employee updates, and system alerts.

## Architecture Overview

### System Architecture
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP/REST + JWT
       ▼
┌─────────────────────────────────┐
│ Notification Service (8085)     │
│  ┌──────────────────────────┐   │
│  │  JWT Filter              │   │
│  └────────┬─────────────────┘   │
│           ▼                      │
│  ┌──────────────────────────┐   │
│  │  Notification Controller │   │
│  └────────┬─────────────────┘   │
│           ▼                      │
│  ┌──────────────────────────┐   │
│  │  Notification Service    │   │
│  │  - Email Handler         │   │
│  │  - SMS Handler           │   │
│  │  - In-App Handler        │   │
│  └────────┬─────────────────┘   │
│           ▼                      │
│  ┌──────────────────────────┐   │
│  │  Notification Repository │   │
│  └────────┬─────────────────┘   │
└───────────┼─────────────────────┘
            ▼
    ┌──────────────┐
    │  MySQL DB    │
    │(notification_db)│
    └──────────────┘
            ▲
            │ WebClient
    ┌───────┴────────┐
    │                │
┌───▼────┐    ┌─────▼──────┐
│Auth    │    │Employee    │
│Service │    │Service     │
│(8081)  │    │(8082)      │
└────────┘    └────────────┘
```

### Component Responsibilities
- **JWT Filter**: Token validation
- **Notification Controller**: REST API endpoints
- **Notification Service**: Business logic, notification routing
- **Email Handler**: Email sending logic
- **SMS Handler**: SMS sending logic
- **In-App Handler**: In-app notification management
- **Notification Repository**: Database operations

### Notification Flow
1. Event triggered in other services
2. Service calls Notification API
3. Notification Service validates request
4. Routes to appropriate handler (Email/SMS/In-App)
5. Stores notification in database
6. Sends notification via channel
7. Updates delivery status

## Assumptions

### Technical Assumptions
- MySQL database accessible on localhost:3306
- JWT secret matches Auth Service
- SMTP server configured for email
- SMS gateway API available
- WebSocket for real-time in-app notifications
- Async processing for notifications

### Business Assumptions
- Notifications sent for: leave approvals, employee updates, system alerts
- Email is primary notification channel
- SMS for urgent notifications only
- In-app notifications stored for 30 days
- No notification preferences per user
- Retry failed notifications 3 times
- Notification templates predefined
- No notification scheduling

### Operational Assumptions
- Service runs on port 8085
- Email delivery not guaranteed
- SMS has cost implications
- Logging enabled for all notifications
- No rate limiting on notification sending

## Technology Stack

- **Java**: 21
- **Spring Boot**: 4.0.3
- **Spring Security**: JWT validation
- **Spring Data JPA**: Database operations
- **Spring WebFlux**: Reactive HTTP client
- **Spring Mail**: Email sending
- **MySQL**: Database
- **Lombok**: Reduce boilerplate code
- **JWT**: io.jsonwebtoken (0.11.5)

## Prerequisites

- JDK 21 or higher
- Maven 3.6+
- MySQL 8.0+
- SMTP server access
- Auth Service running

## Dependencies

```xml
- spring-boot-starter-data-jpa
- spring-boot-starter-mail
- spring-boot-starter-security
- spring-boot-starter-validation
- spring-boot-starter-webmvc
- spring-boot-starter-webflux
- mysql-connector-j
- lombok
- jjwt-api (0.11.5)
- jjwt-impl (0.11.5)
- jjwt-jackson (0.11.5)
```

## Environment Variables

Create `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8085

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/notification_db
spring.datasource.username=<db_username>
spring.datasource.password=<db_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=<your_secret_key>

# Service URLs
auth.service.url=http://localhost:8081
employee.service.url=http://localhost:8082

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<email_username>
spring.mail.password=<email_password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Notification Configuration
notification.retry.max=3
notification.retention.days=30
```

## Setup Instructions

1. **Navigate to project directory**
   ```bash
   cd notification-service
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE notification_db;
   ```

3. **Configure application.properties**
   - Update database credentials
   - Set JWT secret key (must match auth-service)
   - Configure SMTP settings
   - Set service URLs

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

All endpoints require JWT authentication via `Authorization: Bearer <token>` header.

### Notification Management

#### Send Notification
```http
POST /api/notifications
Authorization: Bearer <token>
Content-Type: application/json

{
  "recipientId": 1,
  "type": "EMAIL",
  "subject": "Leave Approved",
  "message": "Your leave request has been approved",
  "priority": "NORMAL"
}
```

#### Get All Notifications
```http
GET /api/notifications
Authorization: Bearer <token>

Query Parameters:
- page (default: 0)
- size (default: 10)
- type (EMAIL, SMS, IN_APP)
- status (PENDING, SENT, FAILED)
```

#### Get Notification by ID
```http
GET /api/notifications/{id}
Authorization: Bearer <token>
```

#### Get User Notifications
```http
GET /api/notifications/user/{userId}
Authorization: Bearer <token>
```

#### Mark as Read
```http
PUT /api/notifications/{id}/read
Authorization: Bearer <token>
```

#### Delete Notification
```http
DELETE /api/notifications/{id}
Authorization: Bearer <token>
```

### Notification Types

- **EMAIL**: Email notification
- **SMS**: SMS notification
- **IN_APP**: In-app notification

### Notification Status

- **PENDING**: Queued for sending
- **SENT**: Successfully sent
- **FAILED**: Failed to send
- **READ**: Read by user (in-app only)

## Project Structure

```
notification-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/company/notification/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── enums/
│   │   │       ├── exception/
│   │   │       ├── handler/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   └── test/
└── pom.xml
```

## Testing Instructions

### Unit Tests
Run all unit tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=NotificationServiceTest
```

### Integration Tests
Run integration tests:
```bash
mvn verify
```

### Test Coverage
Generate coverage report:
```bash
mvn clean test jacoco:report
```
View at: `target/site/jacoco/index.html`

### Manual API Testing

#### 1. Get JWT token
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@example.com","password":"admin123"}'
```

#### 2. Send email notification
```bash
curl -X POST http://localhost:8085/api/notifications \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"recipientId":1,"type":"EMAIL","subject":"Test","message":"Test message","priority":"NORMAL"}'
```

#### 3. Get user notifications
```bash
curl -X GET http://localhost:8085/api/notifications/user/1 \
  -H "Authorization: Bearer <token>"
```

#### 4. Mark notification as read
```bash
curl -X PUT http://localhost:8085/api/notifications/1/read \
  -H "Authorization: Bearer <token>"
```

### Test Data Setup
```sql
USE notification_db;
INSERT INTO notifications (recipient_id, type, subject, message, status, priority, created_at) VALUES 
(1, 'EMAIL', 'Welcome', 'Welcome to the system', 'SENT', 'NORMAL', NOW()),
(2, 'IN_APP', 'Leave Approved', 'Your leave has been approved', 'SENT', 'HIGH', NOW());
```

### Testing Checklist
- [ ] Send email notification
- [ ] Send SMS notification
- [ ] Send in-app notification
- [ ] Get user notifications
- [ ] Mark notification as read
- [ ] Test retry mechanism for failed notifications
- [ ] Validate recipient exists
- [ ] Test notification priority
- [ ] Test pagination

## Test Cases Documentation

### Test Coverage Summary
- **Total Tests**: 28
- **Test Classes**: 10
- **Coverage Target**: 80%+

### Test Structure

#### 1. Service Layer Tests
**EmailServiceTest** (2 tests)
- `sendWelcomeEmail_Success`: Validates welcome email sending for new employees
- `sendLeaveUpdateEmail_Success`: Validates leave status notification emails

#### 2. Consumer Layer Tests
**EmployeeNotificationConsumerTest** (2 tests)
- `consume_ValidEvent_SendsEmail`: Tests RabbitMQ message consumption and email trigger
- `consume_InvalidJson_HandlesException`: Tests error handling for malformed messages

**LeaveNotificationConsumerTest** (2 tests)
- `consume_ValidEvent_SendsEmail`: Tests leave event consumption and notification
- `consume_InvalidJson_HandlesException`: Tests exception handling for invalid JSON

#### 3. Configuration Tests
**RabbitConfigTest** (12 tests)
- `exchange_CreatesExchange`: Validates main exchange creation
- `deadLetterExchange_CreatesExchange`: Validates DLX creation
- `employeeQueue_CreatesQueue`: Tests employee notification queue
- `leaveQueue_CreatesQueue`: Tests leave notification queue
- `employeeDLQ_CreatesDeadLetterQueue`: Tests employee DLQ
- `leaveDLQ_CreatesDeadLetterQueue`: Tests leave DLQ
- `employeeBinding_CreatesBinding`: Tests employee queue binding
- `leaveBinding_CreatesBinding`: Tests leave queue binding
- `employeeDLQBinding_CreatesBinding`: Tests employee DLQ binding
- `leaveDLQBinding_CreatesBinding`: Tests leave DLQ binding
- `messageConverter_CreatesConverter`: Tests JSON message converter
- `rabbitListenerContainerFactory_CreatesFactory`: Tests listener factory

#### 4. DTO Tests
**EmployeeCreatedEventTest** (1 test)
- `setAndGetFields`: Validates DTO field setters and getters

**LeaveStatusChangedEventTest** (1 test)
- `setAndGetFields`: Validates DTO field setters and getters

#### 5. Exception Handling Tests
**GlobalExceptionHandlerTest** (4 tests)
- `handleNotificationProcessing_ReturnsInternalServerError`: Tests notification processing exception
- `handleIllegalArgument_ReturnsBadRequest`: Tests validation error handling
- `handleValidationExceptions_ReturnsBadRequest`: Tests method argument validation
- `handleGenericException_ReturnsInternalServerError`: Tests generic exception handling

**ErrorResponseTest** (1 test)
- `builder_CreatesErrorResponse`: Tests error response builder pattern

**NotificationProcessingExceptionTest** (1 test)
- `constructor_CreatesException`: Tests custom exception creation

#### 6. Mapper Tests
**NotificationMapperTest** (1 test)
- `mapperInterface_Exists`: Validates MapStruct mapper interface

#### 7. Application Tests
**NotificationServiceApplicationTests** (1 test)
- `contextLoads`: Validates Spring context loads successfully

### Running Tests

```bash
# Run all tests
mvn clean test

# Run with coverage report
mvn clean test jacoco:report

# Run specific test class
mvn test -Dtest=EmailServiceTest

# View coverage report
open target/site/jacoco/index.html
```

### Test Results
```
Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
```

## Docker Support

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/notification-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t notification-service .
docker run -p 8085:8085 notification-service
```

## Validation Rules

- Recipient ID: Required, must be valid user
- Type: Required (EMAIL, SMS, IN_APP)
- Subject: Required for EMAIL, max 200 characters
- Message: Required, max 1000 characters
- Priority: NORMAL, HIGH, URGENT

## Error Handling

Standard error response format:
```json
{
  "timestamp": "2024-02-22T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid recipient ID",
  "path": "/api/notifications"
}
```

## Integration with Other Services

- **Auth Service**: JWT token validation
- **Employee Service**: User information retrieval

## Contributing

1. Create feature branch
2. Commit changes
3. Push to branch
4. Create Pull Request

## License

Proprietary
