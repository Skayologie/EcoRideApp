# EdoRide - Electric Vehicle Carpooling Platform

Microservices architecture for an electric vehicle carpooling platform with Spring Cloud, Keycloak, Kafka, and full observability.

## Architecture

- **Config Server** (8888): Centralized configuration management
- **Eureka Server** (8761): Service discovery
- **Gateway** (8888): API Gateway with JWT validation
- **User Service** (8081): User profiles and reliability scores
- **Ride Service** (8082): Ride management and search
- **Booking Service** (8083): Booking management with resilience patterns

## Infrastructure

- **Keycloak** (8080): Authentication and authorization
- **Kafka**: Event-driven communication
- **Zipkin** (9411): Distributed tracing
- **Prometheus** (9090): Metrics collection
- **Grafana** (3000): Monitoring dashboards

## Quick Start

### 1. Start Infrastructure

```bash
docker-compose up -d
```

### 2. Configure Keycloak

1. Access Keycloak at http://localhost:8080
2. Login with admin/admin
3. Create realm: `edoride`
4. Create client: `edoride-client` (public, standard flow enabled)
5. Create roles: `PASSENGER`, `DRIVER`, `ADMIN`
6. Create test users and assign roles

### 3. Setup Config Repository

```bash
mkdir -p ~/config-repo
cd ~/config-repo
git init
```

Create configuration files:

**user-service.yml**:
```yaml
server:
  port: 8081
message: "User Service Configuration"
```

**ride-service.yml**:
```yaml
server:
  port: 8082
message: "Ride Service Configuration"
```

**booking-service.yml**:
```yaml
server:
  port: 8083
message: "Booking Service Configuration"
```

Commit files:
```bash
git add .
git commit -m "Initial config"
```

### 4. Build and Run Services

```bash
# Build all services
mvn clean install

# Start services in order
cd config-server && mvn spring-boot:run &
cd eureka-server && mvn spring-boot:run &
cd gateway && mvn spring-boot:run &
cd user-service && mvn spring-boot:run &
cd ride-service && mvn spring-boot:run &
cd booking-service && mvn spring-boot:run &
```

## Testing

### Get JWT Token from Keycloak

```bash
TOKEN=$(curl -X POST http://localhost:8080/realms/edoride/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=edoride-client" \
  -d "username=<username>" \
  -d "password=<password>" \
  -d "grant_type=password" | jq -r '.access_token')
```

### Create User

```bash
curl -X POST http://localhost:8888/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "keycloakId": "user-123",
    "email": "driver@test.com",
    "name": "John Driver",
    "role": "DRIVER",
    "vehicleType": "Tesla Model 3"
  }'
```

### Create Ride

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
  }'
```

### Search Rides

```bash
curl "http://localhost:8888/api/rides/search?from=Paris&to=Lyon" \
  -H "Authorization: Bearer $TOKEN"
```

### Create Booking

```bash
curl -X POST http://localhost:8888/api/bookings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "rideId": 1,
    "passengerId": 2
  }'
```

## Observability

### Zipkin Tracing
- Access: http://localhost:9411
- View distributed traces with Trace IDs
- Identify bottlenecks and latency

### Prometheus Metrics
- Access: http://localhost:9090
- Query metrics: `http_server_requests_seconds_count`

### Grafana Dashboards
- Access: http://localhost:3000 (admin/admin)
- Add Prometheus datasource: http://prometheus:9090
- Import Spring Boot dashboard (ID: 4701)

## Features Implemented

✅ Service Discovery (Eureka)
✅ Centralized Configuration (Config Server)
✅ API Gateway with JWT validation
✅ Keycloak authentication
✅ Distributed tracing (Zipkin)
✅ Metrics & monitoring (Prometheus + Grafana)
✅ Event-driven architecture (Kafka)
✅ Resilience patterns (Circuit Breaker, Retry, Rate Limiter)
✅ JWT token propagation (Feign Interceptor)
✅ @RefreshScope for dynamic config updates

## Refresh Configuration

```bash
curl -X POST http://localhost:8081/actuator/refresh \
  -H "Authorization: Bearer $TOKEN"
```

## Project Structure

```
edoride-parent/
├── config-server/
├── eureka-server/
├── gateway/
├── user-service/
├── ride-service/
├── booking-service/
├── docker-compose.yml
└── monitoring/
    └── prometheus.yml
```

## Technologies

- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Keycloak 23.0
- Apache Kafka 7.5.0
- Zipkin 2.24
- Prometheus 2.48
- Grafana 10.2
- Resilience4j 2.1.0
