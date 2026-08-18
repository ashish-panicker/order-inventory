# Swagger and OpenAPI Notes

## 1. Introduction
Swagger (now largely synonymous with OpenAPI Specification) is a framework for describing, producing, consuming, and visualizing RESTful web services. It provides a standard, language-agnostic interface to RESTful APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code or additional documentation.

In the modern Java/Spring Boot ecosystem, **Springdoc OpenAPI** is the standard library used to generate OpenAPI 3 documentation, replacing the older Springfox Swagger 2 library.

## 2. Common Configurations (Springdoc OpenAPI)

To enable Swagger in a Spring Boot application, you typically add the `springdoc-openapi-starter-webmvc-ui` dependency.

### `application.yml` Settings
Here are the most commonly used configurations to customize the Swagger UI and API docs generation:

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs # Custom path for the OpenAPI JSON
  swagger-ui:
    path: /swagger-ui.html # Custom path for the Swagger UI
    operationsSorter: method # Sorts endpoints by HTTP method
    tagsSorter: alpha # Sorts tags alphabetically
    disable-swagger-default-url: true
  packagesToScan: com.example.myproject.controllers # Only scan specific packages
  pathsToMatch: /api/** # Only include specific path patterns
```

### Java Configuration (OpenAPI Bean)
You can define a custom `OpenAPI` bean to set up global API metadata, security schemes, and server URLs.

```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Inventory API")
                        .version("1.0")
                        .description("API documentation for Order Inventory System")
                        .contact(new Contact().name("Development Team").email("dev@example.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
```

## 3. Most Commonly Used Annotations

OpenAPI 3 uses a set of annotations from the `io.swagger.v3.oas.annotations` package.

### `@Tag`
**Use Case:** Used on Controller classes to group related endpoints in the Swagger UI.
```java
@Tag(name = "Order Management", description = "APIs for managing orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController { ... }
```

### `@Operation`
**Use Case:** Used on handler methods to describe what the endpoint does.
```java
@Operation(
    summary = "Create a new order", 
    description = "Saves a new order to the database and returns the created order ID."
)
@PostMapping
public ResponseEntity<Order> createOrder(...) { ... }
```

### `@Parameter`
**Use Case:** Used to describe path variables, request parameters, or headers.
```java
@GetMapping("/{id}")
public ResponseEntity<Order> getOrderById(
    @Parameter(description = "The unique ID of the order", example = "12345") 
    @PathVariable Long id
) { ... }
```

### `@ApiResponse` and `@ApiResponses`
**Use Case:** Used to document the possible HTTP response codes and what they mean.
```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Order found"),
    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
})
@GetMapping("/{id}")
public ResponseEntity<Order> getOrderById(@PathVariable Long id) { ... }
```

### `@Schema`
**Use Case:** Used on DTO (Data Transfer Object) classes or their fields to provide examples, descriptions, and constraints.
```java
public class OrderRequestDto {
    
    @Schema(description = "The name of the product", example = "Laptop")
    private String productName;

    @Schema(description = "Quantity ordered", example = "5", minimum = "1")
    private Integer quantity;
}
```

### `@Hidden`
**Use Case:** Used to hide a specific endpoint, controller, or field from the generated Swagger documentation.
```java
@Hidden
@GetMapping("/internal-health-check")
public String healthCheck() { return "OK"; }
```

### `@SecurityRequirement`
**Use Case:** Specifies that an operation requires a specific security scheme (defined in the OpenAPI bean).
```java
@Operation(summary = "Delete an order", security = @SecurityRequirement(name = "bearerAuth"))
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteOrder(@PathVariable Long id) { ... }
```

## 4. Advanced Topics & Integrations

### 4.1 Documenting Global Exception Handlers
Instead of repeating error responses on every controller, you can document them globally on your `@RestControllerAdvice`.
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Input", 
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // ...
    }
}
```

### 4.2 Spring Validation Integration
Springdoc automatically reads standard `jakarta.validation.constraints` on your DTOs. You often don't need `@Schema` if you use these annotations well.
```java
public class ProductDto {
    @NotNull
    @Size(min = 3, max = 50)
    // Swagger automatically knows this is a required string between 3 and 50 characters.
    private String name; 

    @Min(1)
    private Integer quantity;
}
```

### 4.3 File Uploads and Downloads
To document endpoints dealing with files (e.g., `MultipartFile`), you specify the `MediaType`.
```java
// Upload
@Operation(summary = "Upload a document")
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) { ... }

// Download
@Operation(summary = "Download a document")
@GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
public ResponseEntity<Resource> downloadFile(@PathVariable String id) { ... }
```

### 4.4 Handling Spring Data `Pageable`
Springdoc handles pagination objects correctly out of the box when using the `@ParameterObject` annotation from Springdoc, preventing Swagger from exposing internal `Pageable` fields improperly.
```java
import org.springdoc.core.annotations.ParameterObject;

@GetMapping
public Page<Order> getAllOrders(@ParameterObject Pageable pageable) { ... }
```

### 4.5 API Versioning & Grouped APIs
You can separate APIs into different Swagger UI dropdowns (e.g., v1 vs. v2, or Public vs. Admin) using `GroupedOpenApi`.
```java
@Bean
public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
            .group("public-v1")
            .pathsToMatch("/api/v1/public/**")
            .build();
}

@Bean
public GroupedOpenApi adminApi() {
    return GroupedOpenApi.builder()
            .group("admin-v1")
            .pathsToMatch("/api/v1/admin/**")
            .build();
}
```

### 4.6 Advanced Security (OAuth2 / OIDC)
For advanced security, you can configure OAuth2 directly in Swagger.
```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("oauth2", new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                    .authorizationCode(new OAuthFlow()
                        .authorizationUrl("https://auth.example.com/oauth2/authorize")
                        .tokenUrl("https://auth.example.com/oauth2/token")
                        .scopes(new Scopes().addString("read", "read access"))))));
}
```

### 4.7 Code-First vs. Design-First Approach
- **Code-First (Springdoc):** You write Java code and annotations, and Swagger dynamically generates the OpenAPI YAML/JSON at runtime. Great for fast iterations.
- **Design-First (OpenAPI Generator):** You write the `openapi.yaml` spec by hand first, then use a Maven/Gradle plugin (OpenAPI Generator) to auto-generate the Spring Boot Controller interfaces and DTOs. This enforces API contracts before coding begins and is highly recommended for enterprise architectures with many microservices.

## 5. Summary of Best Practices
- **Keep it updated:** Ensure annotations accurately reflect the actual request/response structures.
- **Use DTOs:** Always return DTOs rather than raw entities, and annotate the DTOs with `@Schema` (or standard JSR-380 Validation annotations) for clean documentation.
- **Centralize Security:** Define security schemes globally in your `@Configuration` class and apply them using `@SecurityRequirement`.
- **Don't expose sensitive info:** Use `@Hidden` to hide internal or deprecated APIs that shouldn't be consumed by external clients.
