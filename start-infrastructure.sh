#!/bin/bash

echo "Starting EdoRide Infrastructure..."

# Start Docker infrastructure
docker-compose up -d

echo "Waiting for services to be ready..."
sleep 30

echo "Infrastructure started!"
echo "Keycloak: http://localhost:8080 (admin/admin)"
echo "Zipkin: http://localhost:9411"
echo "Prometheus: http://localhost:9090"
echo "Grafana: http://localhost:3000 (admin/admin)"
echo ""
echo "Next steps:"
echo "1. Configure Keycloak realm 'edoride'"
echo "2. Setup config repository: ~/config-repo"
echo "3. Start microservices with: ./start-services.sh"
