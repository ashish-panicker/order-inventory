# Spring Boot Actuator Guide

## What is Spring Boot Actuator?
Spring Boot Actuator is a sub-project of Spring Boot that provides production-ready features to help you monitor and manage your application. It brings built-in endpoints to check application health, metrics, environment variables, loggers, thread dumps, and more, without requiring you to write custom code for these operational tasks.

## How to Add It
To add Spring Boot Actuator to a Maven project, include the following dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

For Gradle, add this to your `build.gradle`:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

## Standard Endpoints
Actuator provides numerous built-in endpoints. Some of the most common are:
- `/actuator/health`: Shows application health information.
- `/actuator/info`: Displays arbitrary application info.
- `/actuator/metrics`: Shows 'metrics' information for the current application.
- `/actuator/env`: Exposes properties from Spring's `ConfigurableEnvironment`.
- `/actuator/loggers`: Shows and modifies the configuration of loggers in the application.
- `/actuator/threaddump`: Performs a thread dump.
- `/actuator/heapdump`: Returns an HPROF heap dump file.
- `/actuator/beans`: Displays a complete list of all the Spring beans in your application.
- `/actuator/mappings`: Describes all URI routes (controllers).

## What is Enabled and Exposed by Default?
Actuator endpoints have two states: **enabled** and **exposed**.
- By default, almost all endpoints are **enabled** (except for `shutdown`).
- However, for security reasons, only two endpoints are **exposed** over HTTP by default:
  - `health`
  - `info`

*(Note: JMX exposes all enabled endpoints by default).*

## What Needs to Be Enabled/Exposed, Why, and How?
To access other endpoints (like `metrics`, `env`, etc.) over HTTP, you must explicitly expose them.

### Why do we need to expose them explicitly?
Exposing all endpoints by default is a security risk. Endpoints like `/env` can leak sensitive configuration data (like database passwords), and `/heapdump` can contain sensitive memory data. By requiring explicit configuration, Spring Boot ensures you are aware of what operational data is available over the network, allowing you to properly secure it (e.g., with Spring Security).

### How to configure exposure
You can configure which endpoints are exposed over HTTP in your `application.properties` or `application.yml`.

**To expose specific endpoints:**
```properties
management.endpoints.web.exposure.include=health,info,metrics,env
```

**To expose all endpoints:**
```properties
management.endpoints.web.exposure.include=*
```

**To exclude specific endpoints (e.g., exposing all except env):**
```properties
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env
```

To enable the `shutdown` endpoint (which is disabled by default):
```properties
management.endpoint.shutdown.enabled=true
```

## Pros and Cons of Using Spring Actuator

### Pros
- **Out-of-the-box Monitoring:** Instant visibility into application health, metrics, and environment without writing boilerplate code.
- **Integration with APM Tools:** Easily integrates with Prometheus, Grafana, Datadog, New Relic, etc., via Micrometer.
- **Troubleshooting:** Endpoints like `threaddump`, `heapdump`, and `loggers` (which allows changing log levels at runtime) are invaluable for debugging production issues.
- **Customizable:** You can create your own custom actuator endpoints and custom health indicators.

### Cons
- **Security Risks:** If misconfigured (e.g., exposing `*` without securing the `/actuator` path), sensitive information can be leaked to malicious actors.
- **Overhead:** While minimal, generating metrics and heap dumps does consume some CPU and memory resources.
- **Dependency Bloat:** Adds extra dependencies to your application.

## Using Actuator with Eureka Server
When using Spring Cloud Netflix Eureka for service discovery, Actuator plays a crucial role in determining the true health of a service.

1. **Default Behavior:** Eureka clients periodically send heartbeats to the Eureka Server. By default, Eureka uses the client's heartbeat to determine if it's up. However, a client might be able to send heartbeats even if its database connection is down, meaning it's "up" but not actually ready to process requests.
2. **Integrating Actuator Health with Eureka:** You can configure Eureka to use the Actuator `/health` endpoint to determine the true status of the instance. If the Actuator health check reports `DOWN` (e.g., due to a DB failure), Eureka will mark the instance as `DOWN` and stop routing traffic to it.

**Configuration:**
To make Eureka use Actuator's health check, add this to your client application's `application.properties`:

```properties
eureka.client.healthcheck.enabled=true
```

Ensure the `health` endpoint is exposed (it is by default). When configured this way, the Eureka client will propagate its health status (derived from Actuator's `HealthIndicators`) to the Eureka server.
