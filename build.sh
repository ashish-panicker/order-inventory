#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "=========================================="
echo "Starting build process for microservices"
echo "=========================================="

echo ""
echo "--> Compiling, testing, and packaging [order-service]..."
cd order-service
if [ -f "mvnw" ]; then
    chmod +x mvnw
    ./mvnw clean install
else
    mvn clean install
fi
cd ..

echo ""
echo "--> Compiling, testing, and packaging [inventory-service]..."
cd inventory-service
if [ -f "mvnw" ]; then
    chmod +x mvnw
    ./mvnw clean install
else
    mvn clean install
fi
cd ..

echo ""
echo "=========================================="
echo "All services compiled and built successfully!"
echo "=========================================="
