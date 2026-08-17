#!/bin/bash

# Base URL for Order Service
BASE_URL="http://localhost:8081/api/v1/orders"

echo "=== Testing Order Service ==="

echo -e "\n1. Creating a new Order..."
ORDER_RESPONSE=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust123",
    "items": [
      {
        "productId": "prod1",
        "quantity": 2,
        "price": 10.50
      }
    ]
  }')
echo "$ORDER_RESPONSE" | jq . || echo "$ORDER_RESPONSE"

# Extract orderId for subsequent tests (requires jq)
if command -v jq >/dev/null 2>&1; then
    ORDER_ID=$(echo "$ORDER_RESPONSE" | jq -r '.data.id // .data.orderId // empty')
else
    ORDER_ID="example-order-id"
fi

if [ -n "$ORDER_ID" ] && [ "$ORDER_ID" != "null" ]; then
    echo -e "\n2. Getting Order Details for $ORDER_ID..."
    curl -s -X GET "$BASE_URL/$ORDER_ID" | jq . || curl -s -X GET "$BASE_URL/$ORDER_ID"

    echo -e "\n\n4. Cancelling Order $ORDER_ID..."
    curl -s -X PUT "$BASE_URL/$ORDER_ID/cancel" | jq . || curl -s -X PUT "$BASE_URL/$ORDER_ID/cancel"
else
    echo "Could not extract ORDER_ID to test specific order endpoints."
fi

echo -e "\n\n3. Listing all Orders..."
curl -s -X GET "$BASE_URL" | jq . || curl -s -X GET "$BASE_URL"

echo -e "\n\nDone."
