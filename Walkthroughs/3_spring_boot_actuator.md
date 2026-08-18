# Walkthrough: Spring Boot Actuator

This walkthrough explains how to add and configure Spring Boot Actuator. Actuator provides built-in endpoints to monitor and manage your application (health checks, metrics, log levels, etc.).

## 1. Add the Dependency
Add the Actuator starter to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 2. Exposing Endpoints
By default, only the `/health` and `/info` endpoints are exposed over HTTP for security reasons. To expose more endpoints (like `/metrics`, `/env`, etc.), configure your `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health, info, metrics, env" # Or use "*" to expose all
```

**Security Warning**: Never expose all endpoints (`*`) in a production environment without securing them, as they can leak sensitive data (like environment variables and heap dumps).

## 3. Configuring the `/health` Endpoint
The `/health` endpoint shows the basic status of your application (`UP` or `DOWN`). You can configure it to show detailed information (like database connection health, disk space, etc.) by setting:

```yaml
management:
  endpoint:
    health:
      show-details: always
```
When `show-details: always` is enabled, navigating to `http://localhost:8080/actuator/health` will display detailed health indicators for your connected DBs, message brokers, and disk space.

## 4. Useful Actuator Endpoints

- `GET /actuator/health`: Application health.
- `GET /actuator/info`: Application information (can be populated via `application.yml`).
- `GET /actuator/metrics`: List of available metrics (JVM memory, CPU usage, HTTP requests).
  - You can dive deeper into a metric: `GET /actuator/metrics/http.server.requests`
- `GET /actuator/env`: Displays properties from the application's `Environment`.
- `GET /actuator/loggers`: Shows logging levels. You can even change logging levels at runtime via a `POST` request to this endpoint without restarting the app!

## 5. Customizing the Base Path
If you want to change the base path from `/actuator` to something else, use:
```yaml
management:
  endpoints:
    web:
      base-path: "/manage"
```
Now endpoints will be accessible at `http://localhost:8080/manage/health`.
