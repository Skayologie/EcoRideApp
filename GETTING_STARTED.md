# 🚀 EdoRide - Getting Started in 15 Minutes

This guide will get you from zero to a fully running microservices platform in 15 minutes.

## Prerequisites Check (2 minutes)

```bash
# Check Java
java -version  # Should be 17+

# Check Maven
mvn -version   # Should be 3.8+

# Check Docker
docker --version
docker-compose --version

# Check Git
git --version

# Check curl and jq
curl --version
jq --version
```

If any are missing, install them first.

## Step 1: Start Infrastructure (3 minutes)

```bash
cd /mnt/d/EdoRide

# Start all infrastructure services
./start-infrastructure.sh

# Wait for services to be ready (about 30 seconds)
# Check status
docker-compose ps
```

Expected output: All services should be "Up" and healthy.

## Step 2: Configure Keycloak (5 minutes)

Open browser: http://localhost:8080

### Quick Setup:
1. Login: `admin` / `admin`
2. Create Realm: `edoride`
3. Create Client: `edoride-client`
   - Client authentication: OFF
   - Standard flow: ON
   - Direct access grants: ON
4. Create Roles: `PASSENGER`, `DRIVER`, `ADMIN`
5. Create User: `testdriver` / `password`
   - Assign role: `DRIVER`

**Detailed instructions**: See `KEYCLOAK_SETUP.md`

### Verify Keycloak:
```bash
curl -X POST http://localhost:8080/realms/edoride/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=edoride-client" \
  -d "username=testdriver" \
  -d "password=password" \
  -d "grant_type=password" | jq
```

You should see an `access_token` in the response.

## Step 3: Setup Configuration Repository (1 minute)

```bash
./setup-config-repo.sh
```

This creates `~/config-repo` with all configuration files.

## Step 4: Build and Start Services (4 minutes)

```bash
# Build all services (takes 2-3 minutes)
mvn clean install -DskipTests

# Start all microservices
./start-services.sh
```

Services will start in order:
1. Config Server (8888)
2. Eureka Server (8761)
3. Gateway (8888)
4. User Service (8081)
5. Ride Service (8082)
6. Booking Service (8083)

### Monitor startup:
```bash
# Watch logs
tail -f logs/*.log

# Or check Eureka Dashboard
# Open: http://localhost:8761
# Wait until all services are registered (green)
```

## Step 5: Test the System (2 minutes)

### Get Authentication Token:
```bash
export TOKEN=$(curl -s -X POST http://localhost:8080/realms/edoride/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=edoride-client" \
  -d "username=testdriver" \
  -d "password=password" \
  -d "grant_type=password" | jq -r '.access_token')

echo "Token: $TOKEN"
```

### Create a Driver:
```bash
curl -X POST http://localhost:8888/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "keycloakId": "driver-001",
    "email": "driver@edoride.com",
    "name": "Jean Dupont",
    "role": "DRIVER",
    "vehicleType": "Tesla Model 3"
  }' | jq
```

### Create a Passenger:
```bash
curl -X POST http://localhost:8888/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "keycloakId": "passenger-001",
    "email": "passenger@edoride.com",
    "name": "Marie Martin",
    "role": "PASSENGER"
  }' | jq
```

### Create a Ride:
```bash
curl -X POST http://localhost:8888/api/rides \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "driverId": 1,
    "fromCity": "Paris",
    "toCity": "Lyon",
    "departureTime": "2026-03-10T10:00:00",
    "availableSeats": 3,
    "batteryAutonomy": 350.0
  }' | jq
```

### Search Rides:
```bash
curl "http://localhost:8888/api/rides/search?from=Paris&to=Lyon" \
  -H "Authorization: Bearer $TOKEN" | jq
```

### Create a Booking:
```bash
curl -X POST http://localhost:8888/api/bookings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "rideId": 1,
    "passengerId": 2
  }' | jq
```

## 🎉 Success! Your System is Running

### Access Dashboards:

| Dashboard | URL | Credentials |
|-----------|-----|-------------|
| Eureka | http://localhost:8761 | None |
| Zipkin | http://localhost:9411 | None |
| Prometheus | http://localhost:9090 | None |
| Grafana | http://localhost:3000 | admin/admin |
| Keycloak | http://localhost:8080 | admin/admin |

### View Distributed Tracing:
1. Open Zipkin: http://localhost:9411
2. Click "Run Query"
3. Click on any trace to see the complete flow
4. Notice the Trace ID propagating through services

### View Metrics:
1. Open Prometheus: http://localhost:9090
2. Try query: `http_server_requests_seconds_count`
3. See metrics from all services

### Setup Grafana Dashboard:
1. Open Grafana: http://localhost:3000
2. Login: admin/admin
3. Add Prometheus datasource:
   - Configuration → Data sources → Add data source
   - Select Prometheus
   - URL: `http://prometheus:9090`
   - Click "Save & Test"
4. Import Spring Boot dashboard:
   - Dashboards → Import
   - Dashboard ID: `4701`
   - Select Prometheus datasource
   - Click "Import"

## 🧪 Test Advanced Features

### Test Circuit Breaker:
```bash
# Stop Ride Service
pkill -f ride-service

# Try to create booking (should use fallback)
curl -X POST http://localhost:8888/api/bookings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"rideId": 1, "passengerId": 2}' | jq

# Restart Ride Service
cd ride-service && mvn spring-boot:run > ../logs/ride-service.log 2>&1 &
```

### Test Configuration Refresh:
```bash
# Modify configuration
cd ~/config-repo
echo "
booking-service:
  auto-confirm: true
  max-bookings-per-user: 10" >> booking-service.yml

git add .
git commit -m "Update booking config"

# Refresh without restart
curl -X POST http://localhost:8083/actuator/refresh \
  -H "Authorization: Bearer $TOKEN"
```

### Test Kafka Events:
```bash
# View Kafka topics
docker exec -it edoride-kafka-1 kafka-topics --list --bootstrap-server localhost:9092

# Consume booking events
docker exec -it edoride-kafka-1 kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic booking-created-event \
  --from-beginning
```

## 📚 Next Steps

1. **Explore the Code**
   - Check `user-service/src/main/java/com/edoride/user/`
   - Review `booking-service/src/main/java/com/edoride/booking/`
   - Understand resilience patterns in `BookingService.java`

2. **Read Documentation**
   - `QUICK_REFERENCE.md` - Quick commands
   - `ARCHITECTURE.md` - System architecture
   - `PROJECT_SUMMARY.md` - Complete feature list
   - `TEST_REPORT.md` - Testing scenarios

3. **Customize**
   - Add new endpoints
   - Modify business logic
   - Add new microservices
   - Create custom Grafana dashboards

4. **Capture Screenshots**
   - Zipkin trace showing complete flow
   - Grafana dashboard with metrics
   - Eureka dashboard with all services
   - Prometheus metrics

## 🛑 Stopping the System

### Stop Microservices:
```bash
pkill -f spring-boot:run
```

### Stop Infrastructure:
```bash
docker-compose down
```

### Stop and Remove All Data:
```bash
docker-compose down -v
```

## 🐛 Troubleshooting

### Services won't start?
```bash
# Check if ports are available
netstat -tulpn | grep -E '8080|8081|8082|8083|8761|8888|9090|9411|3000'

# Check logs
tail -f logs/[service-name].log
```

### Can't get JWT token?
- Verify Keycloak is running: `docker ps | grep keycloak`
- Check realm name is exactly `edoride`
- Verify user exists and has correct password

### Services not registering with Eureka?
- Wait 30 seconds for registration
- Check Eureka is running: http://localhost:8761
- Verify `eureka.client.service-url.defaultZone` in application.yml

### Kafka connection issues?
```bash
# Restart Kafka
docker-compose restart kafka zookeeper
```

## 📊 System Health Check

Run this to verify everything is working:

```bash
# Check Docker services
docker-compose ps

# Check Eureka registration
curl http://localhost:8761/eureka/apps | grep -o '<app>[^<]*</app>'

# Check service health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health

# Check Zipkin
curl http://localhost:9411/api/v2/services

# Check Prometheus targets
curl http://localhost:9090/api/v1/targets | jq '.data.activeTargets[] | {job: .labels.job, health: .health}'
```

## 🎯 What You've Accomplished

✅ Deployed 7 infrastructure services
✅ Started 6 microservices
✅ Configured authentication with Keycloak
✅ Enabled distributed tracing with Zipkin
✅ Setup metrics collection with Prometheus
✅ Created monitoring dashboards with Grafana
✅ Implemented event-driven architecture with Kafka
✅ Applied resilience patterns (Circuit Breaker, Retry, Rate Limiter)
✅ Configured centralized configuration with Git
✅ Enabled service discovery with Eureka

## 🚀 You're Ready!

Your complete microservices platform is now running. Explore, test, and customize as needed!

For detailed information, see:
- `QUICK_REFERENCE.md` - Command reference
- `DEPLOYMENT_GUIDE.md` - Detailed deployment steps
- `TEST_REPORT.md` - Comprehensive testing guide
- `ARCHITECTURE.md` - Architecture diagrams
