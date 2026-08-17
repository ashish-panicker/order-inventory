#!/bin/bash

# Base URL for Inventory Service
BASE_URL="http://localhost:8082/api/v1/inventory"

echo "=== Testing Inventory Service ==="

echo -e "\n1. Adding stock for prod1..."
curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "prod1",
    "quantityToAdd": 100
  }' | jq . || curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "prod1",
    "quantityToAdd": 100
  }'

echo -e "\n\n2. Getting stock info for prod1..."
curl -s -X GET "$BASE_URL/prod1" | jq . || curl -s -X GET "$BASE_URL/prod1"

echo -e "\n\n3. Deducting stock for prod1..."
curl -s -X PUT "$BASE_URL/prod1/deduct" \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 2
  }' | jq . || curl -s -X PUT "$BASE_URL/prod1/deduct" \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 2
  }'

echo -e "\n\n4. Listing all inventory..."
curl -s -X GET "$BASE_URL" | jq . || curl -s -X GET "$BASE_URL"

echo -e "\n\nDone."
