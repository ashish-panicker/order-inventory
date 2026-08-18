#!/bin/bash
set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "=========================================="
echo "Running inventory-service..."
echo "=========================================="
cd "$DIR/../inventory-service"

if [ -f "mvnw" ]; then
    chmod +x mvnw
    ./mvnw spring-boot:run
else
    mvn spring-boot:run
fi
