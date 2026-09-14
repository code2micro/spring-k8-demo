# spring-k8-demo

A beginner-friendly Spring Boot 3 application built with Java 21 and Maven.
It exposes simple JSON endpoints and an Actuator health endpoint, and is ready
to be containerized and deployed to Kubernetes later.

## Requirements

- Java 21 or newer
- Maven 3.9 or newer

## Run locally

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## Endpoints

| Method | URL | Purpose |
| --- | --- | --- |
| GET | `/api/hello` | Returns a greeting |
| GET | `/api/info` | Returns application metadata |
| GET | `/api/configured-message` | Shows the configured message |
| GET | `/actuator/health` | Reports application health |

Examples:

```bash
curl http://localhost:8080/api/hello
curl http://localhost:8080/api/info
curl http://localhost:8080/actuator/health
```

## Configuration

The application name is defined in `src/main/resources/application.properties`.
The configured message is read from the `APP_MESSAGE` environment variable. If
the variable is not set, the application uses `Hello from Configuration`.

```bash
APP_MESSAGE="Hello from Docker" mvn spring-boot:run
```

## Build

```bash
mvn clean package
java -jar target/spring-k8-demo-1.0.jar
```
