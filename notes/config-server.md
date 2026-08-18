# Spring Cloud Config Server

## Overview
Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system. With the Config Server, you have a central place to manage external properties for applications across all environments.

## Core Concepts

- **Centralized Configuration:** Instead of each microservice having its own `application.properties` or `application.yml` packed into its JAR, they fetch their configurations from a central Config Server at startup.
- **Environment Management:** It manages configurations seamlessly across different environments (e.g., `dev`, `test`, `prod`). The configurations are usually stored as `application-{profile}.properties` in the backend repository.
- **Backend Storage:** The Config Server works as an intermediary between the microservices and the storage backend where the properties actually live. The most common backend is **Git**, but it also supports SVN, HashiCorp Vault, JDBC, and a local file system.

## Architecture

1. **Config Server:** A Spring Boot application annotated with `@EnableConfigServer`. It exposes a REST API to serve configurations.
2. **Config Client:** A Spring Boot microservice that binds to the Config Server via `spring.config.import=optional:configserver:<url>` (Spring Boot 2.4+) or `bootstrap.properties` (older versions) to fetch its own configuration.
3. **Storage Repository:** A remote repository (e.g., Git) containing the configuration files.

## Benefits

1. **Dynamic Updates:** With the help of Spring Cloud Bus and `@RefreshScope`, you can update configurations dynamically without restarting the microservices.
2. **Security:** Sensitive data like passwords or tokens can be stored securely. It integrates with solutions like Vault, or you can use Jasypt for encryption, or simply use environment variables mapped to the Config Server environment.
3. **Version Control:** By using Git as a backend, all configuration changes are versioned. You can easily audit changes and rollback if a bad configuration is deployed.
4. **Single Source of Truth:** Avoids configuration drift and ensures all services are aligned with the intended infrastructure state.

## Security Considerations

- **Securing the Config Server:** The endpoints of the Config Server should be secured (e.g., via Spring Security with Basic Auth) to prevent unauthorized access to configuration data.
- **Securing the Credentials:** As we did with the Git PAT, avoid hardcoding credentials in the Config Server's own `application.properties`. Use environment variables or encrypted property managers.
