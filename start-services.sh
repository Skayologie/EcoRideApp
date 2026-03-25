#!/bin/bash

echo "Building all services..."
mvn clean install -DskipTests

echo "Starting services..."

cd config-server
mvn spring-boot:run > ../logs/config-server.log 2>&1 &
echo "Config Server starting..."
sleep 10

cd ../eureka-server
mvn spring-boot:run > ../logs/eureka-server.log 2>&1 &
echo "Eureka Server starting..."
sleep 15

cd ../gateway
mvn spring-boot:run > ../logs/gateway.log 2>&1 &
echo "Gateway starting..."
sleep 10

cd ../user-service
mvn spring-boot:run > ../logs/user-service.log 2>&1 &
echo "User Service starting..."

cd ../ride-service
mvn spring-boot:run > ../logs/ride-service.log 2>&1 &
echo "Ride Service starting..."

cd ../booking-service
mvn spring-boot:run > ../logs/booking-service.log 2>&1 &
echo "Booking Service starting..."

cd ..

echo ""
echo "All services started!"
echo "Check logs in ./logs/ directory"
echo ""
echo "Service URLs:"
echo "- Eureka Dashboard: http://localhost:8761"
echo "- Gateway: http://localhost:8888"
echo "- User Service: http://localhost:8081"
echo "- Ride Service: http://localhost:8082"
echo "- Booking Service: http://localhost:8083"
