# API Tests

## Order Service (Port 8081 assumed)

### 1. Create Order
- **URL**: `POST /api/v1/orders`
- **Description**: Creates a new order.
- **Sample Data**:
```json
{
  "customerId": "cust123",
  "items": [
    {
      "productId": "prod1",
      "quantity": 2,
      "price": 10.50
    }
  ]
}
```
- **Expected Response**: `201 CREATED`
```json
{
  "status": "SUCCESS",
  "data": {
    "orderId": "order-id-uuid",
    "status": "CREATED",
    "..."
  }
}
```

### 2. Get Order Details
- **URL**: `GET /api/v1/orders/{orderId}`
- **Description**: Gets the details of a specific order.
- **Expected Response**: `200 OK`
```json
{
  "status": "SUCCESS",
  "data": {
    "orderId": "order-id-uuid",
    "status": "CREATED",
    "..."
  }
}
```

### 3. List Orders
- **URL**: `GET /api/v1/orders`
- **Description**: Gets a paginated list of orders.
- **Expected Response**: `200 OK`
```json
{
  "content": [...],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

### 4. Cancel Order
- **URL**: `PUT /api/v1/orders/{orderId}/cancel`
- **Description**: Cancels a specific order.
- **Expected Response**: `200 OK`
```json
{
  "status": "SUCCESS",
  "data": {
    "orderId": "order-id-uuid",
    "status": "CANCELLED"
  }
}
```

---

## Inventory Service (Port 8082 assumed)

### 1. Add Stock
- **URL**: `POST /api/v1/inventory`
- **Description**: Adds stock for a product.
- **Sample Data**:
```json
{
  "productId": "prod1",
  "quantityToAdd": 100
}
```
- **Expected Response**: `201 CREATED`
```json
{
  "status": "SUCCESS",
  "data": {
    "productId": "prod1",
    "availableQuantity": 100,
    "..."
  }
}
```

### 2. Get Stock Info
- **URL**: `GET /api/v1/inventory/{productId}`
- **Description**: Gets the current stock of a product.
- **Expected Response**: `200 OK`
```json
{
  "status": "SUCCESS",
  "data": {
    "productId": "prod1",
    "availableQuantity": 100,
    "..."
  }
}
```

### 3. List Inventory
- **URL**: `GET /api/v1/inventory`
- **Description**: Gets a paginated list of inventory items.
- **Expected Response**: `200 OK`
```json
{
  "content": [...],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

### 4. Deduct Stock
- **URL**: `PUT /api/v1/inventory/{productId}/deduct`
- **Description**: Deducts stock for a product.
- **Sample Data**:
```json
{
  "quantity": 2
}
```
- **Expected Response**: `200 OK`
```json
{
  "status": "SUCCESS",
  "data": {
    "productId": "prod1",
    "availableQuantity": 98,
    "..."
  }
}
```
