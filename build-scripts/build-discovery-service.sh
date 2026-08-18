#!/bin/bash
set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "=========================================="
echo "Building discovery-service..."
echo "=========================================="
cd "$DIR/../discovery-service"

if [ -f "mvnw" ]; then
    chmod +x mvnw
    ./mvnw clean install -DskipTests
else
    mvn clean install -DskipTests
fi
echo "discovery-service build complete."
