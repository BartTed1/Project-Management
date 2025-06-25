# Notification System Implementation

This document describes the complete notification system implementation with WebSocket real-time support and HTTP REST API.

## Overview

The notification system provides:
- Real-time notifications via WebSocket
- HTTP REST API for retrieving historical notifications
- User authentication and authorization
- Database persistence with JPA/Hibernate

## Components

### 1. Entity Layer

#### Notification Entity
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/entity/Notification.kt`
- **Fields**:
  - `id`: Primary key
  - `title`: Notification title
  - `content`: Notification content/message
  - `createdAt`: Timestamp (auto-generated)
  - `read`: Read status (default: false)
  - `user`: Many-to-One relationship with User
  - `team`: Many-to-One relationship with Team

### 2. Repository Layer

#### NotificationRepository
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/repository/NotificationRepository.kt`
- **Key Methods**:
  - `findByUserIdAndReadFalseOrderByCreatedAtDesc()`: Get unread notifications
  - `findByUserIdOrderByCreatedAtDesc()`: Get all notifications with pagination
  - `countByUserIdAndReadFalse()`: Count unread notifications
  - `markAsReadByIdAndUserId()`: Mark specific notification as read
  - `markAllAsReadByUserId()`: Mark all user's notifications as read

### 3. Service Layer

#### NotificationService Interface
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/service/NotificationService.kt`

#### NotificationServiceImpl
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/service/NotificationServiceImpl.kt`
- **Key Features**:
  - Create notifications with automatic real-time sending
  - Retrieve notifications with pagination
  - Mark notifications as read
  - Count unread notifications

### 4. Controller Layer

#### NotificationController
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/controller/NotificationController.kt`
- **Endpoints**:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications` | Get all notifications (paginated) |
| GET | `/api/notifications/unread` | Get unread notifications |
| GET | `/api/notifications/count` | Get unread notifications count |
| PUT | `/api/notifications/{id}/read` | Mark specific notification as read |
| PUT | `/api/notifications/read-all` | Mark all notifications as read |

### 5. WebSocket Layer

#### WebSocket Configuration
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/config/WebSocketConfig.kt`
- **Endpoint**: `/ws/notifications`

#### NotificationWebSocketHandler
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/websocket/NotificationWebSocketHandler.kt`
- **Features**:
  - JWT token authentication
  - Connection management
  - Error handling

#### WebSocketSessionManager
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/websocket/WebSocketSessionManager.kt`
- **Features**:
  - Session management per user
  - Real-time message broadcasting
  - Connection cleanup

### 6. DTO Layer

#### NotificationResponse
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/dto/response/NotificationResponse.kt`

#### NotificationCountResponse
- **File**: `src/main/kotlin/xyz/teodorowicz/pm/dto/response/NotificationCountResponse.kt`

## Dependencies Added

Added to `build.gradle.kts`:
```kotlin
implementation("org.springframework.boot:spring-boot-starter-websocket")
```

## Usage Examples

### HTTP API Usage

#### Get All Notifications
```bash
GET /api/notifications?page=0&size=20
Authorization: Bearer <jwt-token>
```

#### Get Unread Notifications
```bash
GET /api/notifications/unread
Authorization: Bearer <jwt-token>
```

#### Mark Notification as Read
```bash
PUT /api/notifications/123/read
Authorization: Bearer <jwt-token>
```

### WebSocket Usage

#### JavaScript Client Example
```javascript
// Connect to WebSocket with JWT token
const token = "your-jwt-token";
const ws = new WebSocket(`ws://localhost:8080/ws/notifications?token=${token}`);

ws.onopen = function(event) {
    console.log("Connected to notification WebSocket");
};

ws.onmessage = function(event) {
    const notification = JSON.parse(event.data);
    console.log("New notification:", notification);
    // Handle notification (show popup, update UI, etc.)
};

ws.onclose = function(event) {
    console.log("WebSocket connection closed");
};

ws.onerror = function(error) {
    console.error("WebSocket error:", error);
};
```

### Programmatic Usage

#### Creating Notifications
```kotlin
@Autowired
private lateinit var notificationService: NotificationService

// Create a notification
notificationService.createNotification(
    userId = 1L,
    teamId = 1L,
    title = "New Task Assigned",
    content = "You have been assigned to task: Sample Task"
)
```

See `src/main/kotlin/xyz/teodorowicz/pm/example/NotificationUsageExample.kt` for more examples.

## Authentication

### WebSocket Authentication
The WebSocket endpoint supports JWT token authentication via:
1. **Query Parameter**: `?token=<jwt-token>`
2. **Authorization Header**: `Authorization: Bearer <jwt-token>`

### HTTP API Authentication
All HTTP endpoints require JWT token authentication using the `@JwtToken` annotation.

## Database Schema

The notification system uses the existing database with the `notification` table:

```sql
CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL REFERENCES app_user(id),
    team_id BIGINT NOT NULL REFERENCES team(id)
);
```

## Error Handling

- WebSocket connections are automatically closed on authentication failure
- HTTP endpoints return appropriate status codes
- Comprehensive logging for debugging

## Security Considerations

- JWT token validation for all connections
- CORS configuration for WebSocket (currently set to allow all origins - configure for production)
- Input validation and sanitization
- Session cleanup on connection errors

## Integration Points

The notification system can be integrated into existing services by:
1. Injecting `NotificationService` into other services
2. Calling `createNotification()` when events occur (task assignment, project updates, etc.)
3. Using the provided usage examples as templates

## Monitoring

The `WebSocketSessionManager` provides methods for monitoring:
- `isUserConnected(userId)`: Check if user has active WebSocket connection
- `getActiveSessionsCount()`: Get total number of active WebSocket sessions
