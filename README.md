# NotificationHub

NotificationHub is a Spring Boot application for processing notification events and routing them through different delivery channels such as email, SMS, or other downstream services.

## Features

- Kafka-based event ingestion
- Dead-letter topic support for failed messages
- Delivery log tracking
- Notification template rendering
- Email delivery channel
- Spring Boot + JPA + MongoDB integration

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring Kafka
- Spring Data JPA
- H2 in-memory database (default for local development)
- MongoDB support via Spring Data MongoDB

## Prerequisites

- Java 21 or newer
- Maven
- Kafka broker running locally on `localhost:9092`
- Optional: MongoDB running locally if you use the Mongo repositories in the app

## Local Configuration

The app uses the following defaults in `src/main/resources/application.properties`:

```properties
spring.kafka.bootstrap-servers=localhost:9092
notifyhub.kafka.topic=notification-events
notifyhub.kafka.consumer-group=notification-service

spring.mail.host=localhost
spring.mail.port=2525
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
```

These values are sufficient for local startup and tests when Kafka and SMTP are not yet configured in your environment.

## Run the Application

The app is configured to run locally on `http://127.0.0.1:8080`.

```bash
./mvnw spring-boot:run
```

## Run Tests

```bash
./mvnw test
```

## Project Structure

```text
src/
  main/
    java/
      com/
        NotificationHub/
          config/
          delivery/
          deliverylog/
          exception/
          notification/
          template/
    resources/
      application.properties
      templates/
  test/
    java/
```

## Notes

- The application currently uses an H2 database for local development and tests.
- Kafka consumers are configured to connect to `localhost:9092` by default.
- If you connect to a real external Kafka or SMTP service, override the corresponding values in `application.properties` or environment variables.
