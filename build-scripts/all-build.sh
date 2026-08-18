#!/bin/bash
set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "=========================================="
echo "Starting all-build process"
echo "=========================================="

"$DIR/build-config-service.sh"
"$DIR/build-discovery-service.sh"
"$DIR/build-order-service.sh"
"$DIR/build-inventory-service.sh"

echo "=========================================="
echo "All projects built successfully!"
echo "=========================================="
