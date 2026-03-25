#!/bin/bash

echo "Setting up EdoRide Config Repository..."

CONFIG_DIR=~/config-repo

if [ -d "$CONFIG_DIR" ]; then
    echo "Config repository already exists at $CONFIG_DIR"
    exit 0
fi

mkdir -p $CONFIG_DIR
cd $CONFIG_DIR
git init

# Create user-service.yml
cat > user-service.yml << 'EOF'
server:
  port: 8081

spring:
  application:
    name: user-service

message: "User Service - Centralized Configuration"

# Custom properties
user-service:
  max-reliability-score: 10.0
  default-reliability-score: 5.0
EOF

# Create ride-service.yml
cat > ride-service.yml << 'EOF'
server:
  port: 8082

spring:
  application:
    name: ride-service

message: "Ride Service - Centralized Configuration"

# Custom properties
ride-service:
  max-seats: 4
  min-battery-autonomy: 50.0
EOF

# Create booking-service.yml
cat > booking-service.yml << 'EOF'
server:
  port: 8083

spring:
  application:
    name: booking-service

message: "Booking Service - Centralized Configuration"

# Custom properties
booking-service:
  auto-confirm: false
  max-bookings-per-user: 5
EOF

# Create application.yml (shared config)
cat > application.yml << 'EOF'
spring:
  cloud:
    config:
      enabled: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,refresh
EOF

git add .
git commit -m "Initial configuration"

echo "Config repository created at $CONFIG_DIR"
echo "Files created:"
ls -la $CONFIG_DIR
