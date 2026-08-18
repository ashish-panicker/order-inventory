#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "=========================================="
echo "Starting all services in the background..."
echo "=========================================="

"$DIR/run-discovery-service.sh" &
DISCOVERY_PID=$!

# Optionally wait a bit for discovery service to start up
sleep 5

"$DIR/run-order-service.sh" &
ORDER_PID=$!

"$DIR/run-inventory-service.sh" &
INVENTORY_PID=$!

echo "=========================================="
echo "All services are starting up!"
echo "Press [CTRL+C] to stop all services."
echo "=========================================="

# Trap ctrl-c and kill all children
trap "echo 'Stopping all services...'; kill $DISCOVERY_PID $ORDER_PID $INVENTORY_PID; exit 0" SIGINT SIGTERM

wait
