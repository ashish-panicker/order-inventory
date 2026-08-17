# Microservices: Order and Inventory Management System

This repository serves as a practical learning project to explore and implement concepts related to Microservices Architecture, Inter-service Communication, Microservice Design Patterns, and standard REST API development practices.

The project is structured around two core services:
* **Order Service**: Manages customer orders.
* **Inventory Service**: Manages product stock and availability.

---

## 1. Microservices Concepts

Microservices Architecture is an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often an HTTP resource API.

### Key Characteristics:
* **Decentralized Data Management**: Each microservice manages its own database (Database-per-service pattern) to ensure loose coupling.
* **Independent Deployability**: Services can be updated, deployed, and scaled independently.
* **Technology Heterogeneity**: Different services can be written in different programming languages and use different data storage technologies.
* **Resilience**: A failure in one service does not necessarily bring down the entire application.

---

## 2. Microservice Design Patterns

This project aims to demonstrate several key microservice design patterns:

### Database per Service
* **Concept**: Each service has its own private database, inaccessible directly by other services.
* **Implementation**: `order-service` has an Order DB; `inventory-service` has an Inventory DB.

### API Gateway (Optional/Future Enhancement)
* **Concept**: A single point of entry for all clients. It routes requests to the appropriate microservice.
* **Benefits**: Simplifies client code, allows for centralized cross-cutting concerns (authentication, rate limiting).

### Service Discovery (Discovery Server)
* **Concept**: A centralized registry where microservices register themselves and discover other services dynamically.
* **Use Case**: `order-service` queries the Discovery Server (e.g., Eureka/Consul) to find the location (IP/Port) of `inventory-service` instead of hardcoding URLs.

### Centralized Configuration (Config Server)
* **Concept**: Externalizes configuration for all microservices into a centralized repository (e.g., Git).
* **Use Case**: Properties like database credentials, feature toggles, and timeouts are managed by a Config Server and pulled dynamically by services at startup or runtime.

### Circuit Breaker Pattern
* **Concept**: Prevents an application from repeatedly trying to execute an operation that's likely to fail.
* **Use Case**: If `inventory-service` is down, `order-service` should fail fast instead of hanging and exhausting resources.

---

## 3. Inter-Service Communication

Microservices can communicate synchronously or asynchronously.

### Synchronous Communication (REST/HTTP)
* **Pattern**: Request/Response. The client sends a request and waits for a response.
* **Use Case in Project**: The `order-service` makes a synchronous HTTP call to the `inventory-service` to check stock availability before confirming an order. (Note: In a highly resilient system, asynchronous communication is often preferred for core workflows).

### Asynchronous Communication (Message Broker - e.g., RabbitMQ, Kafka)
* **Pattern**: Event-Driven. Services publish events when state changes, and other services subscribe to those events.
* **Future Enhancement**: Instead of synchronous HTTP calls, `order-service` could publish an `OrderCreated` event. `inventory-service` consumes it, reserves stock, and publishes `InventoryReserved` or `InventoryFailed` events.

---

## 4. Standard REST API Practices

* **Resource-Oriented URIs**: Use nouns, not verbs (e.g., `/orders`, not `/createOrder`).
* **HTTP Methods**: Use appropriate methods for CRUD operations (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
* **Status Codes**: Return standard HTTP status codes indicating success or failure (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`, `500 Internal Server Error`).
* **Pagination & Filtering**: Implement pagination for collections (e.g., `/orders?page=1&limit=10`).
* **Versioning**: Version APIs to handle breaking changes (e.g., `/api/v1/orders`).
* **Consistent Error Handling**: Return a standardized error format.

---

## 5. Standard API Definitions

### Common Error Schema
All services return errors in the following standard JSON format:
```json
{
  "success": false,
  "timestamp": "string (ISO-8601)",
  "status": "integer (HTTP status code)",
  "error": "string (Short error type)",
  "message": "string (Detailed error message)",
  "path": "string (Request URI)"
}
```

### Common Success Schema
For a single resource or non-paginated data:
```json
{
  "success": true,
  "data": { ... }
}
```

### Paginated Response Schema
For lists of resources:
```json
{
  "success": true,
  "data": [ ... ],
  "meta": {
    "page": "integer",
    "size": "integer",
    "totalElements": "integer",
    "totalPages": "integer",
    "sort": "string"
  }
}
```

### A. Inventory Service API (`/api/v1/inventory`)

Manages product stock operations.

#### 1. Retrieve Stock Information
* **Endpoint**: `GET /api/v1/inventory/{productId}`
* **Description**: Gets the current stock details for a specific product.
* **Response Schema (200 OK)**:
```json
{
  "success": true,
  "data": {
    "productId": "string",
    "quantity": "integer",
    "inStock": "boolean",
    "lastUpdated": "string (ISO-8601)"
  }
}
```
* **Error Responses**:
  * `404 Not Found`: Product ID does not exist.

#### 2. List Inventory
* **Endpoint**: `GET /api/v1/inventory`
* **Description**: Returns all products and their stock levels.
* **Query Parameters**:
  * `page` (optional, default: 0): Page number (0-indexed).
  * `size` (optional, default: 10): Number of records per page.
  * `sort` (optional, default: `productId,asc`): Sorting criteria.
* **Response Schema (200 OK)**:
```json
{
  "success": true,
  "data": [
    {
      "productId": "string",
      "quantity": "integer",
      "inStock": "boolean",
      "lastUpdated": "string (ISO-8601)"
    }
  ],
  "meta": {
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5,
    "sort": "productId,asc"
  }
}
```

#### 3. Add Product
* **Endpoint**: `POST /api/v1/inventory`
* **Description**: Initializes stock for a new product.
* **Request Schema**:
```json
{
  "productId": "string (required, max 255 chars)",
  "quantityToAdd": "integer (required, min 1)"
}
```
* **Success Responses**:
  * `201 Created`: Stock successfully added/initialized. Response includes the added product wrapped in the Common Success Schema.
* **Error Responses**:
  * `400 Bad Request`: Invalid input (e.g., negative quantity, missing productId).
  * `500 Internal Server Error`: Database connection failure.

#### 4. Deduct Stock
* **Endpoint**: `PUT /api/v1/inventory/{productId}/deduct`
* **Description**: Decreases the stock count. The Order Service calls this when an order is created.
* **Request Schema**:
```json
{
  "quantity": "integer (required, min 1)"
}
```
* **Success Responses**:
  * `200 OK`: Stock deducted successfully. Response wrapped in the Common Success Schema.
* **Error Responses**:
  * `400 Bad Request`: Invalid quantity provided or insufficient stock.
  * `404 Not Found`: Product ID does not exist in inventory.

---

### B. Order Service API (`/api/v1/orders`)

Manages the lifecycle of customer orders.

#### 1. Create Order
* **Endpoint**: `POST /api/v1/orders`
* **Description**: Places a new order. The service validates inventory synchronously before confirming.
* **Request Schema**:
```json
{
  "customerId": "string (required)",
  "items": [
    {
      "productId": "string (required)",
      "quantity": "integer (required, min 1)",
      "price": "number (required, decimal)"
    }
  ]
}
```
* **Success Responses**:
  * `201 Created`: Order placed successfully and stock reserved. Response includes new `orderId` wrapped in the Common Success Schema.
* **Error Responses**:
  * `400 Bad Request`: Invalid payload or insufficient stock (propagated from Inventory service).
  * `503 Service Unavailable`: Inventory service is unreachable (Circuit Breaker opened).

#### 2. Get Order Details
* **Endpoint**: `GET /api/v1/orders/{orderId}`
* **Description**: Retrieves the status and details of a specific order.
* **Response Schema (200 OK)**:
```json
{
  "success": true,
  "data": {
    "orderId": "string",
    "status": "string (e.g., PENDING, CONFIRMED, CANCELLED)",
    "totalAmount": "number (decimal)",
    "createdAt": "string (ISO-8601)"
  }
}
```
* **Error Responses**:
  * `404 Not Found`: The specified order ID does not exist.

#### 3. List Orders
* **Endpoint**: `GET /api/v1/orders`
* **Description**: Fetches a list of orders.
* **Query Parameters**:
  * `page` (optional, default: 0): Page number (0-indexed).
  * `size` (optional, default: 10): Number of records per page.
  * `sort` (optional, default: `createdAt,desc`): Sorting criteria.
* **Response Schema (200 OK)**:
```json
{
  "success": true,
  "data": [
    {
      "orderId": "string",
      "status": "string",
      "totalAmount": "number",
      "createdAt": "string"
    }
  ],
  "meta": {
    "page": 0,
    "size": 10,
    "totalElements": 150,
    "totalPages": 15,
    "sort": "createdAt,desc"
  }
}
```

---

## Inter-Service Communication Flow (Synchronous Example)

1. **Client** sends `POST /api/v1/orders` to `order-service`.
2. `order-service` validates the request payload.
3. `order-service` acts as an HTTP client and sends a synchronous `GET /api/v1/inventory/{productId}` request to `inventory-service` to check stock availability, followed by a `PUT /api/v1/inventory/{productId}/deduct` to reserve the stock.
4. `inventory-service` processes the requests:
   * **If stock is sufficient and updated successfully**: Returns `200 OK`. `order-service` saves the order to its DB with status `CONFIRMED` and returns `201 Created` to the client.
   * **If insufficient stock or product not found**: Returns `400 Bad Request` or `404 Not Found`. `order-service` aborts the order and returns the corresponding error to the client.
   * **If down/timeout**: `order-service`'s Circuit Breaker trips. `order-service` returns `503 Service Unavailable` and does not persist the order.

---

## 6. Troubleshooting Docker Volumes & Permissions

During the initial setup, the MySQL Docker containers experienced a continuous crash-loop where they would start and immediately fail.

### Why were the containers crashing?
The `docker-compose.yml` was configured to bind-mount the database data to local directories (`./data/order_db_data` and `./data/inventory_db_data`). On Linux distributions that use **SELinux** (Security-Enhanced Linux), the OS aggressively enforces access control. Because Docker containers run in their own isolated security context, SELinux blocked the MySQL processes (running inside the containers) from reading or writing to the mapped host directories.

This resulted in `Permission denied` errors inside the MySQL container logs, causing the databases to crash and restart in an infinite loop.

### The Fix: The `:z` Flag
To resolve this permission issue, the `:z` flag was appended to the volume declarations in `docker-compose.yml`:
```yaml
volumes:
  - ./data/order_db_data:/var/lib/mysql:z
```

**What does the `:z` flag do?**
* **On SELinux systems (Fedora, RHEL, CentOS):** It instructs Docker to automatically "relabel" the SELinux security context of the mapped directory on the host. This securely grants the container the necessary permissions to read and write to those files.
* **On Non-SELinux systems (Windows, macOS, Ubuntu, Debian):** Docker simply ignores the `:z` flag. It causes no harm and behaves identically to a standard volume mount.

By including the `:z` flag, the `docker-compose.yml` remains truly cross-platform. It acts as a safety net for Linux environments while remaining completely transparent to developers on Windows or Mac.