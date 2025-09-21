# E-commerce Microservices Platform

This repository contains a minimal microservices architecture for an e-commerce platform. It includes product catalog, cart and order management, API gateway, monitoring with Actuator/Prometheus, and a lightweight UI.

## Modules

- `services/product-service`: product search, details, stock management
- `services/order-service`: cart operations, order creation, user authentication
- `gateway`: edge routing to internal services
- `ui`: simple Thymeleaf UI using the gateway

## Repository layout

```
services/
  user-service/
  product-service/
  order-service/
gateway/
ui/
pom.xml (parent)
README.md
run-all.ps1
```

## Technology

- Java 21, Spring Boot 3, Spring Cloud Gateway
- Spring Data JPA with in-memory H2 for persistence
- Spring Security with JWT authentication
- Actuator + Micrometer Prometheus for metrics
- Thymeleaf for server-rendered UI

## Architecture

Request flow: browser → `ui` → `gateway` → domain services (`user-service`, `product-service`, `order-service`). Each service has its own database schema (H2). Gateway centralizes routing. User authentication is handled by `user-service` with JWT tokens. Actuator endpoints expose health and metrics.

## Local Development

### Using Docker (Recommended)
```bash
docker-compose up --build
```

### Manual Setup
```bash
# 1. Build all services
mvn -q -DskipTests package

# 2. Start services in order
mvn -q -pl services/product-service spring-boot:run &
mvn -q -pl services/order-service spring-boot:run &
mvn -q -pl gateway spring-boot:run &
mvn -q -pl ui spring-boot:run &
```

### Access Points
- **Main UI**: http://localhost:8085
- **API Gateway**: http://localhost:8080
- **Product Service**: http://localhost:8081
- **Order Service**: http://localhost:8082

## Docker

```bash
# Start all services
docker-compose up --build

# Run in background
docker-compose up -d

# Stop services
docker-compose down
```

## Features

- ✅ User registration/login system
- ✅ Product catalog and search
- ✅ Cart management (add, remove, clear)
- ✅ Stock management (automatic reduction/increase)
- ✅ Order creation and viewing
- ✅ Microservices architecture
- ✅ API Gateway routing
- ✅ PostgreSQL database support
- ✅ Docker containerization

Health checks: `http://localhost:8083/actuator/health`, `http://localhost:8081/actuator/health`, `http://localhost:8082/actuator/health`, `http://localhost:8080/actuator/health`.

## Sample requests

### Authentication
```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/signup -H 'Content-Type: application/json' -d '{"name":"John Doe","email":"john@example.com","password":"password123","confirmPassword":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/signin -H 'Content-Type: application/json' -d '{"email":"john@example.com","password":"password123"}'
```

### Products
```bash
curl -X POST http://localhost:8081/api/products -H 'Content-Type: application/json' -d '{"sku":"SKU-1","name":"Phone","description":"5G","price":799,"stock":10}'
curl http://localhost:8080/api/products
```

### Cart (requires authentication token)
```bash
# Get token from login response, then use it
curl -X POST http://localhost:8080/api/carts/user-1/items -H 'Content-Type: application/json' -H 'Authorization: Bearer YOUR_JWT_TOKEN' -d '{"productId":1,"quantity":2}'
curl http://localhost:8080/api/carts/user-1 -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

## Tests

Each module includes unit tests samples (add more as needed) and can be run with `mvn test`.

## Deploy preview

For a quick preview on a single host, run all modules as shown above. For containerized deployment, create Dockerfiles per module and route traffic via the gateway service.
