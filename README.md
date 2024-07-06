# Event Booking System

A REST API to manage event bookings, allowing users to create, find, and reserve tickets for events, view and manage
their reservations, and receive notifications before events start.

## Features

- User account creation and authentication
- Event creation, updating, deletion, and search
- Ticket reservation and management
- Notifications for upcoming events

## Prerequisites

- JDK 11+
- Maven 3.6+

## Running the Application

### Using H2 Database

1. Build the project:
    ```bash
    mvn clean install
    ```

2. Compile the project:
    ```bash
    mvn compile
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

The application will be accessible at `http://localhost:8080`.

### Using Docker (Optional)

If you prefer to use Docker, you can set up a containerized environment. Ensure you have Docker installed and running on
your machine.

## Swagger Documentation

Access the API documentation at `http://localhost:8080/documentation/swagger-ui/index.html#/`.

## Running Tests

1. Run unit tests:
    ```bash
    mvn test
    ```

2. Run integration tests:
    ```bash
    mvn verify
    ```

Integration tests use PostgreSQLTestContainer to simulate the database environment.

## Preloaded Data

The application uses H2 in-memory database for local development. Necessary reference data and dummy data are preloaded
on startup.

For integration tests, PostgreSQLTestContainer is used to ensure the tests run in a controlled environment with data
preloaded as needed.

## Additional Configuration

- Application properties can be configured in `src/main/resources/application.properties`.
- For test configurations, use `src/test/resources/application-test.properties`.

## CURL Commands for Endpoints

Here are some common CURL commands for the endpoints:

User Endpoints

- Create a new user:

```bash
  curl -X POST "http://localhost:8080/api/v1/event-booking/users/" -H "Content-Type: application/json" -d '{"name":"Joy Osayi","email":"osayijoy17@gmail.com","password":"password"}'
```

- User login:

```bash
curl -X POST "http://localhost:8080/api/v1/event-booking/authentication/login" -H "Content-Type: application/json" -d '{"email":"osayijoy17@gmail.com","password":"password"}'
```

- Get user by email:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/users/osayijoy17@gmail.com" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json"
```

- Update user by email:

```bash
curl -X PUT "http://localhost:8080/api/v1/event-booking/users/osayijoy17@gmail.com" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json" -d '{"name":"Updated Name"}'
```

- Get all users:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/users/?page=1&size=10" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json"
```

- Delete user by email:

```bash
curl -X DELETE "http://localhost:8080/api/v1/event-booking/users/osayijoy17@gmail.com"H "Authorization: Bearer <access_token>"
```

Event Endpoints

- Create a new event:

```bash
curl -X POST "http://localhost:8080/api/v1/event-booking/events/" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json" -d '{"name":"Conference","date":"2024-07-15T10:00:00","availableAttendeesCount":100,"description":"Tech Conference","category":"CONCERT"}'
```

- Update event by id:

```bash
curl -X PUT "http://localhost:8080/api/v1/event-booking/events/1" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json" -d '{"name":"Updated Conference Name"}'
```

- Get event by id:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/events/1" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json"
```

- Get all events with filters:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/events/?name=conference&startDate=2024-07-15&endDate=2024-07-20&category=CONCERT&page=1&size=10" H "Authorization: Bearer <access_token>" -H "Content-Type: application/json"
```

- Delete event by id:

```bash
curl -X DELETE "http://localhost:8080/api/v1/event-booking/events/1" H "Authorization: Bearer <access_token>"
```

Reservation Endpoints

- Reserve a ticket for an event:

```bash
curl -X POST "http://localhost:8080/api/v1/event-booking/reservations/" H "Authorization: Bearer <access_token>" -H "Content-Type: application/x-www-form-urlencoded" -d "eventId=1&email=osayijoy17@gmail.com&attendeesCount=1"
```

- Get all reservations for a user:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/reservations/user/osayijoy17@gmail.com?page=1&size=10" H "Authorization: Bearer <access_token>"
```

- Get all reservations for an event:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/reservations/event/1?page=1&size=10" H "Authorization: Bearer <access_token>"
```

- Get a reservation by event id and user email:

```bash
curl -X GET "http://localhost:8080/api/v1/event-booking/reservations/event/1/osayijoy17@gmail.com" H "Authorization: Bearer <access_token>"
```

- Cancel a reservation by reservation id:

```bash
curl -X DELETE "http://localhost:8080/api/v1/event-booking/reservations/1" H "Authorization: Bearer <access_token>"
```
