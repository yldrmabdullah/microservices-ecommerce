# E-commerce Microservices Platform

Professional microservices architecture for e-commerce platform with advanced features including service discovery, centralized configuration, caching, monitoring, and security.

## Architecture Overview

This platform implements a comprehensive microservices architecture following industry best practices:

- **Service Discovery**: Eureka Server for dynamic service registration and discovery
- **API Gateway**: Spring Cloud Gateway with rate limiting, authentication, and circuit breaker
- **Centralized Configuration**: Spring Cloud Config Server for configuration management
- **Caching**: Redis for distributed caching across services
- **Monitoring**: Micrometer, Prometheus, and Zipkin for observability
- **Security**: JWT authentication with rate limiting and request filtering
- **Resilience**: Circuit breaker pattern and retry mechanisms

## Services

### Core Services
- **Eureka Server** (Port 8761): Service discovery and registration
- **Config Server** (Port 8888): Centralized configuration management
- **User Service** (Port 8083): User authentication, registration, and profile management
- **Product Service** (Port 8081): Product catalog, search, and inventory management
- **Order Service** (Port 8082): Shopping cart and order processing
- **API Gateway** (Port 8080): Edge service with routing, security, and rate limiting
- **UI Application** (Port 8085): Thymeleaf-based web interface

### Infrastructure Services
- **Redis** (Port 6379): Distributed caching and rate limiting
- **PostgreSQL** (Port 5432): Primary database for production
- **Zipkin** (Port 9411): Distributed tracing

## Technology Stack

### Backend
- **Java 21** with Spring Boot 3.3.5
- **Spring Cloud Gateway** for API gateway functionality
- **Spring Cloud Netflix Eureka** for service discovery
- **Spring Cloud Config** for centralized configuration
- **Spring Data JPA** with H2 (development) and PostgreSQL (production)
- **Spring Security** with JWT authentication
- **Spring Cache** with Redis integration

### Monitoring & Observability
- **Micrometer** for application metrics
- **Prometheus** for metrics collection and storage
- **Zipkin** for distributed tracing
- **Spring Boot Actuator** for health checks and monitoring

### Caching & Performance
- **Redis** for distributed caching
- **Spring Cache** annotations for transparent caching
- **Rate Limiting** with Redis-based token bucket algorithm

### Security
- **JWT (JSON Web Tokens)** for stateless authentication
- **Rate Limiting** per user and IP address
- **CORS** configuration for cross-origin requests
- **Request filtering** and validation

## Repository Structure

```
valven/
├── services/
│   ├── eureka-server/          # Service discovery server
│   ├── config-server/          # Centralized configuration
│   ├── user-service/           # User management and authentication
│   ├── product-service/        # Product catalog and inventory
│   └── order-service/          # Shopping cart and orders
├── gateway/                    # API Gateway with security and routing
├── ui/                        # Web user interface
├── docker-compose.yml         # Multi-container orchestration
├── pom.xml                    # Parent Maven configuration
└── README.md                  # This file
```

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose (optional)
- Redis (for production features)

### Local Development (Without Docker)

1. **Start Redis** (required for caching and rate limiting):
```bash
# Using Docker
docker run -d -p 6379:6379 redis:7-alpine

# Or install Redis locally
# macOS: brew install redis && brew services start redis
# Ubuntu: sudo apt install redis-server && sudo systemctl start redis
```

2. **Build the project**:
```bash
mvn clean compile
```

3. **Start services in order**:
```bash
# Terminal 1: Eureka Server
cd services/eureka-server && mvn spring-boot:run

# Terminal 2: Config Server
cd services/config-server && mvn spring-boot:run

# Terminal 3: User Service
cd services/user-service && mvn spring-boot:run

# Terminal 4: Product Service
cd services/product-service && mvn spring-boot:run

# Terminal 5: Order Service
cd services/order-service && mvn spring-boot:run

# Terminal 6: API Gateway
cd gateway && mvn spring-boot:run

# Terminal 7: UI Application
cd ui && mvn spring-boot:run
```

### Docker Compose (Recommended)

```bash
# Start all services
docker-compose up --build

# Run in background
docker-compose up -d

# Stop services
docker-compose down
```

## Access Points

### Application URLs
- **Main UI**: http://localhost:8085
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888

### Service URLs
- **User Service**: http://localhost:8083
- **Product Service**: http://localhost:8081
- **Order Service**: http://localhost:8082

### Monitoring URLs
- **Prometheus Metrics**: http://localhost:8080/actuator/prometheus
- **Health Checks**: http://localhost:8080/actuator/health
- **Zipkin Tracing**: http://localhost:9411

## Features

### Core E-commerce Features
- ✅ User registration and authentication with JWT
- ✅ Product catalog with search and filtering
- ✅ Shopping cart management
- ✅ Order processing and history
- ✅ Inventory management with stock tracking

### Microservices Features
- ✅ Service discovery with Eureka
- ✅ Centralized configuration management
- ✅ API Gateway with intelligent routing
- ✅ Distributed caching with Redis
- ✅ Circuit breaker pattern for resilience
- ✅ Rate limiting for API protection

### Monitoring & Observability
- ✅ Health checks and metrics collection
- ✅ Distributed tracing with Zipkin
- ✅ Prometheus metrics integration
- ✅ Request/response logging
- ✅ Performance monitoring

### Security Features
- ✅ JWT-based authentication
- ✅ Rate limiting per user and IP
- ✅ CORS configuration
- ✅ Request validation and filtering
- ✅ Secure password encoding

## Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Set to `prod` for production mode
- `SPRING_REDIS_HOST`: Redis server hostname (default: localhost)
- `EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE`: Eureka server URL

### Rate Limiting Configuration
- **Auth endpoints**: 10 requests/minute per user
- **Product endpoints**: 20 requests/minute per IP
- **Order endpoints**: 5 requests/minute per user

### Cache Configuration
- **User data**: 10 minutes TTL
- **Product data**: 5 minutes TTL
- **Order data**: 10 minutes TTL

## Development Guidelines

### Code Quality
- Follow Clean Code principles
- Use meaningful variable and method names
- Implement proper error handling
- Add comprehensive logging
- Write unit tests for critical functionality

### Microservices Best Practices
- Each service has a single responsibility
- Services communicate via well-defined APIs
- Database per service pattern
- Stateless service design
- Proper error handling and fallback mechanisms

## Troubleshooting

### Common Issues
1. **Port conflicts**: Ensure all required ports are available
2. **Redis connection**: Verify Redis is running and accessible
3. **Service discovery**: Check Eureka server is running first
4. **Database issues**: Verify database connections and schemas

### Logs
- Check individual service logs for detailed error information
- Use Zipkin for tracing request flows
- Monitor Prometheus metrics for performance issues

## Contributing

1. Follow the established code style and patterns
2. Add appropriate tests for new features
3. Update documentation for any API changes
4. Ensure all services start successfully
5. Verify integration tests pass
