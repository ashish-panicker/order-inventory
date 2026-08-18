# Walkthrough: Spring Boot Profiles

This walkthrough guides you through adding and configuring Spring Boot profiles in an existing service. Profiles allow you to map your configuration to different environments (e.g., `dev`, `qa`, `prod`).

## 1. Understanding Profiles
By default, Spring Boot uses `application.yml` or `application.properties`. When you need different configurations for different environments, you can use profiles.

## 2. Splitting a Single File into Multiple Files
Instead of keeping all configurations in one large `application.yml`, you should split them by environment.

Create the following files in your `src/main/resources/` directory:
- `application.yml` (Common configurations applied across all environments)
- `application-dev.yml` (Development-specific configurations)
- `application-qa.yml` (QA-specific configurations)
- `application-prod.yml` (Production-specific configurations)

### Example `application.yml` (Common)
```yaml
spring:
  application:
    name: inventory-service

server:
  port: 8080
```

### Example `application-dev.yml`
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devdb
    driver-class-name: org.h2.Driver
```

### Example `application-prod.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://prod-db-server:3306/inventory_db
    username: root
    password: securePassword
```

## 3. Loading Multiple Profiles at the Same Time

You can activate one or more profiles simultaneously. For example, you might want to load a `dev` profile and a `security` profile together.

### Option A: Using `application.yml` (Active Profile)
You can set the active profiles in your main `application.yml`:
```yaml
spring:
  profiles:
    active: "dev, security"
```

### Option B: Using Command-Line Arguments
When running your jar file, pass the active profiles as an argument:
```bash
java -jar my-service.jar --spring.profiles.active=prod,metrics
```

### Option C: Using Environment Variables
Useful for Docker containers or CI/CD pipelines:
```bash
export SPRING_PROFILES_ACTIVE=qa,messaging
```

## 4. Profile-Specific Beans
You can also restrict Java Beans to load only when a specific profile is active using the `@Profile` annotation.
```java
@Component
@Profile("dev")
public class DevMockDatabaseInitializer {
    // Only runs when the 'dev' profile is active
}
```
