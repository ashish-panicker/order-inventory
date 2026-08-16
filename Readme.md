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
  "timestamp": "string (ISO-8601)",
  "status": "integer (HTTP status code)",
  "error": "string (Short error type)",
  "message": "string (Detailed error message)",
  "path": "string (Request URI)"
}
```

### A. Inventory Service API (`/api/v1/inventory`)

Manages product stock operations.

#### 1. Add New Stock
* **Endpoint**: `POST /api/v1/inventory/stock`
* **Description**: Initializes or adds stock for a product.
* **Request Schema**:
```json
{
  "productId": "string (required, max 255 chars)",
  "quantityToAdd": "integer (required, min 1)"
}
```
* **Success Responses**:
  * `201 Created`: Stock successfully added/initialized.
* **Error Responses**:
  * `400 Bad Request`: Invalid input (e.g., negative quantity, missing productId).
  * `500 Internal Server Error`: Database connection failure.

#### 2. Update Stock Quantity
* **Endpoint**: `PUT /api/v1/inventory/stock/{productId}`
* **Description**: Replaces the current stock quantity for an existing product.
* **Request Schema**:
```json
{
  "newQuantity": "integer (required, min 0)"
}
```
* **Success Responses**:
  * `200 OK`: Stock updated successfully.
* **Error Responses**:
  * `400 Bad Request`: Invalid quantity provided.
  * `404 Not Found`: Product ID does not exist in inventory.

#### 3. Retrieve Stock Information
* **Endpoint**: `GET /api/v1/inventory/stock/{productId}`
* **Description**: Gets the current stock details for a specific product.
* **Response Schema (200 OK)**:
```json
{
  "productId": "string",
  "quantity": "integer",
  "inStock": "boolean",
  "lastUpdated": "string (ISO-8601)"
}
```
* **Error Responses**:
  * `404 Not Found`: Product ID does not exist.

---

### B. Order Service API (`/api/v1/orders`)

Manages the lifecycle of customer orders.

#### 1. Place a New Order
* **Endpoint**: `POST /api/v1/orders/place-order`
* **Description**: Submits a new order. The service validates inventory synchronously before confirming.
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
  * `201 Created`: Order placed successfully and stock reserved. Response includes new `orderId`.
* **Error Responses**:
  * `400 Bad Request`: Invalid payload or insufficient stock (propagated from Inventory service).
  * `503 Service Unavailable`: Inventory service is unreachable (Circuit Breaker opened).

#### 2. Check Order Status
* **Endpoint**: `GET /api/v1/orders/status/{orderId}`
* **Description**: Fetches the current status of an existing order.
* **Response Schema (200 OK)**:
```json
{
  "orderId": "string",
  "status": "string (e.g., PENDING, CONFIRMED, CANCELLED)",
  "totalAmount": "number (decimal)",
  "createdAt": "string (ISO-8601)"
}
```
* **Error Responses**:
  * `404 Not Found`: The specified order ID does not exist.

#### 3. Cancel an Order
* **Endpoint**: `PUT /api/v1/orders/cancel/{orderId}`
* **Description**: Cancels a previously placed order. (In a real system, this should trigger an inventory restoration).
* **Request Schema**: Empty body.
* **Success Responses**:
  * `200 OK`: Order cancelled successfully.
* **Error Responses**:
  * `400 Bad Request`: Order is in a state that cannot be cancelled (e.g., already SHIPPED).
  * `404 Not Found`: Order ID does not exist.

---

## Inter-Service Communication Flow (Synchronous Example)

1. **Client** sends `POST /api/v1/orders` to `order-service`.
2. `order-service` validates the request.
3. `order-service` acts as an HTTP client and sends a synchronous `POST /api/v1/inventory/deduct` request to `inventory-service`.
4. `inventory-service` processes the deduction:
   * **If success**: Returns `200 OK`. `order-service` then saves the order to its DB with status `CREATED` and returns `201 Created` to the client.
   * **If insufficient stock**: Returns `400 Bad Request`. `order-service` aborts order creation and returns `400 Bad Request` to the client.
   * **If down/timeout**: `order-service`'s Circuit Breaker trips. `order-service` returns `503 Service Unavailable` or gracefully falls back.