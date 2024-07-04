# Event Booking System

A REST API to manage event bookings, allowing users to create, find, and reserve tickets for events, view and manage their reservations, and receive notifications before events start.

## Features
- User account creation and authentication
- Event creation, updating, deletion, and search
- Ticket reservation and management
- Notifications for upcoming events
- 
## Prerequisites

- JDK 11+
- Maven 3.6+

## Running the Application

### Using H2 Database

1. Clone the repository:
    ```bash
    git clone <repository_url>
    cd <repository_directory>
    ```

2. Build the project:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

The application will be accessible at `http://localhost:8080`.

### Using Docker (Optional)

If you prefer to use Docker, you can set up a containerized environment. Ensure you have Docker installed and running on your machine.

1. Build the Docker image:
    ```bash
    docker build -t event-booking-system .
    ```

2. Run the Docker container:
    ```bash
    docker run -p 8080:8080 event-booking-system
    ```

The application will be accessible at `http://localhost:8080`.

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

The application uses H2 in-memory database for local development. Necessary reference data and dummy data are preloaded on startup.

For integration tests, PostgreSQLTestContainer is used to ensure the tests run in a controlled environment with data preloaded as needed.

## Additional Configuration

- Application properties can be configured in `src/main/resources/application.properties`.
- For test configurations, use `src/test/resources/application-test.properties`.

