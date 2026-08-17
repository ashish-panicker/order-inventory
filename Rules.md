# Development Rules & Guidelines

When contributing to or developing the microservices in this project (`order-service` and `inventory-service`), strictly adhere to the following rules to maintain consistency, reliability, and code quality.

## Core Architecture & Design

### 1. Layered Architecture
Strictly enforce a standard multi-tier architecture to ensure separation of concerns:
- **Controller Layer** (`@RestController`): Handles incoming HTTP requests, input validation, and delegates business logic. Does not contain business logic.
- **Service Layer** (`@Service`): Contains the core business logic, orchestrates data retrieval/saving, and handles transactions.
- **Repository Layer** (`@Repository`): Interfaces with the database (e.g., Spring Data JPA interfaces).

### 2. DTOs and Data Immutability
- **Use Java Records for DTOs**: Use `record` for all Data Transfer Objects (Requests and Responses) to ensure immutability, thread-safety, and boilerplate-free code. 
- Never expose internal database Entities directly to the API consumer. Always map Entities to DTOs.

## API Standards

### 3. Unified Responses and Pagination
- **Standardized Wrapping**: Every API endpoint must return either the Common Success Schema or Paginated Response Schema as defined in `Readme.md`. No naked arrays or primitives should be returned.
- **Pagination, Sorting, & Filtering**: For all list endpoints (`GET /orders`, `GET /inventory`), implement pagination (`page`, `size`) and sorting (`sort`) exactly as defined in the API specs. Use Spring Data's `Pageable` where applicable.

### 4. Input Validations
- Use **Jakarta Bean Validation** (`@Valid`, `@NotNull`, `@Min`, `@NotBlank`, etc.) on all incoming DTOs.
- Validation should fail fast at the Controller layer before reaching the Service layer.

### 5. Global Exception Handling
- Implement a centralized Global Exception Handler (e.g., using `@RestControllerAdvice`).
- Catch standard exceptions (e.g., `MethodArgumentNotValidException`, `EntityNotFoundException`) and custom business exceptions, translating them strictly into the **Common Error Schema** defined in `Readme.md`.

---

## Recommended Additional Rules

Here are some extra best-practice rules I recommend adding to the project to ensure enterprise-grade quality:

### 6. Dependency Injection
- Use **Constructor Injection** (easily achieved via Lombok's `@RequiredArgsConstructor` and `final` fields) instead of `@Autowired` field injection. This ensures immutability and makes unit testing much easier.

### 7. Entity Mapping
- Use a dedicated mapping library like **MapStruct** for type-safe, performant conversion between Entities and DTOs. This avoids manual, error-prone mapping logic cluttering the Service layer.

### 8. Soft Deletion
- Instead of hard-deleting database records (`DELETE` SQL statements), implement soft deletion (using a boolean flag like `isDeleted` or `deletedAt` timestamp, combined with Hibernate's `@SQLDelete` and `@Where`). This retains historical data and prevents cascading data loss.

### 9. Transaction Management
- Use `@Transactional` explicitly at the Service layer for methods that modify data (create, update, delete) or require a consistent read state.

### 10. Audit Logging
- Implement JPA Auditing (`@CreatedDate`, `@LastModifiedDate`) on a mapped superclass for all database entities so you automatically track when records are inserted or updated.
