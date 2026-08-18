#!/bin/bash
set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "=========================================="
echo "Building inventory-service..."
echo "=========================================="
cd "$DIR/../inventory-service"

if [ -f "mvnw" ]; then
    chmod +x mvnw
    ./mvnw clean install -DskipTests
else
    mvn clean install -DskipTests
fi
echo "inventory-service build complete."
