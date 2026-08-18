# Walkthrough: Adding Swagger/OpenAPI Docs

This walkthrough guides you through adding interactive OpenAPI (Swagger) documentation to an existing Spring Boot REST service.

## 1. Add the Dependency
Add the Springdoc OpenAPI dependency to your `pom.xml`. This library automatically reads your Spring Boot REST controllers and generates the OpenAPI 3 specification.

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.1.0</version>
</dependency>
```

## 2. Global Configuration
Configure Swagger UI paths and metadata. Create an `OpenAPI` bean in a configuration class to set up the API title, version, and global security schemes.

```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Microservice API")
                        .version("1.0.0")
                        .description("API documentation for the service."));
    }
}
```

You can customize the UI path in your `application.yml`:
```yaml
springdoc:
  swagger-ui:
    path: /api-docs-ui.html
    operationsSorter: method
```

## 3. Using Annotations

### Grouping with `@Tag`
Use `@Tag` on your Controller to group operations.
```java
@Tag(name = "User Management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/users")
public class UserController { ... }
```

### Describing Endpoints with `@Operation`
Use `@Operation` to describe what an endpoint does.
```java
@Operation(summary = "Get User", description = "Fetches user details by their ID")
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) { ... }
```

### Describing Parameters with `@Parameter`
Document path variables and request parameters.
```java
public User getUser(@Parameter(description = "User's unique ID") @PathVariable Long id) { ... }
```

### Documenting Responses with `@ApiResponse`
Define the expected HTTP status codes.
```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User found successfully"),
    @ApiResponse(responseCode = "404", description = "User not found")
})
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) { ... }
```

### Defining Models with `@Schema`
Use `@Schema` on your DTO fields to provide descriptions and example values.
```java
public class UserDto {
    @Schema(description = "The user's full name", example = "John Doe")
    private String name;
}
```

## 4. Viewing the Documentation
Start your Spring Boot application and navigate to:
`http://localhost:8080/swagger-ui.html`
