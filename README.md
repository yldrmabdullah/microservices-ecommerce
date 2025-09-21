# Valven E-commerce Microservices Platform

A comprehensive microservices-based e-commerce platform built with modern Java technologies, featuring service discovery, centralized configuration, distributed caching, monitoring, and security.

## Architecture Overview

This platform implements a robust microservices architecture following industry best practices:

- **Service Discovery**: Eureka Server for dynamic service registration and discovery
- **API Gateway**: Spring Cloud Gateway with rate limiting, authentication, and circuit breaker
- **Centralized Configuration**: Spring Cloud Config Server for configuration management
- **Distributed Caching**: Redis for high-performance caching across services
- **Monitoring & Observability**: Micrometer, Prometheus, Zipkin, and Grafana integration
- **Security**: JWT authentication with rate limiting and comprehensive input validation
- **Resilience**: Circuit breaker pattern, retry mechanisms, and fallback strategies

## Technology Stack

### Backend Technologies
- **Java 21** with Spring Boot 3.3.5
- **Spring Cloud Gateway** for API gateway functionality
- **Spring Cloud Netflix Eureka** for service discovery
- **Spring Cloud Config** for centralized configuration
- **Spring Data JPA** with H2 (development) and PostgreSQL (production)
- **Spring Security** with JWT authentication
- **Spring Cache** with Redis integration
- **Spring WebFlux** for reactive programming

### Database & Caching
- **PostgreSQL 15** for production data persistence
- **H2 Database** for development and testing
- **Redis 7** for distributed caching and rate limiting
- **JPA/Hibernate** for ORM mapping

### Monitoring & Observability
- **Micrometer** for application metrics collection
- **Prometheus** for metrics storage and querying
- **Zipkin** for distributed tracing
- **Grafana** for visualization and dashboards
- **Spring Boot Actuator** for health checks and monitoring

### Security & Validation
- **JWT (JSON Web Tokens)** for stateless authentication
- **BCrypt** for password hashing
- **Bean Validation** for input validation
- **Rate Limiting** with Redis-based token bucket algorithm
- **CORS** configuration for cross-origin requests

### Development & Testing
- **Maven** for dependency management and build automation
- **JUnit 5** for unit testing
- **MockMvc** for integration testing
- **Testcontainers** for integration test environments
- **Lombok** for reducing boilerplate code

### Documentation & API
- **OpenAPI 3.0** (Swagger) for API documentation
- **SpringDoc** for automatic API documentation generation
- **RESTful API** design principles

### Containerization & Orchestration
- **Docker** for containerization
- **Docker Compose** for local development orchestration
- **Kubernetes** manifests for production deployment
- **Multi-stage Docker builds** for optimized images

## Services Architecture

### Core Business Services
- **User Service** (Port 8083): User authentication, registration, and profile management
- **Product Service** (Port 8081): Product catalog, search, inventory management, and stock tracking
- **Order Service** (Port 8082): Shopping cart management and order processing
- **AI Insights Service** (Port 8084): AI-powered analytics, recommendations, and insights with web interface

### Infrastructure Services
- **Eureka Server** (Port 8761): Service discovery and registration
- **Config Server** (Port 8888): Centralized configuration management
- **API Gateway** (Port 8080): Edge service with routing, security, and rate limiting
- **UI Application** (Port 8085): Thymeleaf-based web interface

### Supporting Services
- **Redis** (Port 6379): Distributed caching and rate limiting
- **PostgreSQL** (Port 5432): Primary database for production
- **Zipkin** (Port 9411): Distributed tracing

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
├── database-migrations/        # Database schema migrations
├── monitoring/                 # Monitoring configurations
├── security-audit/            # Security audit tools
├── performance-tests/         # Load testing and performance tests
├── k8s/                       # Kubernetes deployment manifests
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
- PostgreSQL 15+ (for production)

### Security Configuration
Before running the application, set the following environment variables:

```bash
# Required for production
export POSTGRES_PASSWORD="your-secure-postgres-password"
export JWT_SECRET="your-very-strong-secret-key-here-must-be-at-least-256-bits-long-for-hs512-algorithm"
```

**Important Security Notes:**
- Never use default passwords in production
- Generate a strong JWT secret (at least 256 bits)
- Use environment variables for all sensitive configuration
- Ensure proper CORS configuration for your domain

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
- **AI Insights Dashboard**: http://localhost:8084/web/insights
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888

### API Documentation
- **User Service API**: http://localhost:8083/swagger-ui.html
- **Product Service API**: http://localhost:8081/swagger-ui.html
- **Order Service API**: http://localhost:8082/swagger-ui.html
- **AI Insights Service API**: http://localhost:8084/swagger-ui.html
- **Gateway API**: http://localhost:8080/swagger-ui.html

### Service URLs
- **User Service**: http://localhost:8083
- **Product Service**: http://localhost:8081
- **Order Service**: http://localhost:8082
- **AI Insights Service**: http://localhost:8084

### Monitoring URLs
- **Prometheus Metrics**: http://localhost:8080/actuator/prometheus
- **Health Checks**: http://localhost:8080/actuator/health
- **Zipkin Tracing**: http://localhost:9411

## Features

### Core E-commerce Features
- ✓ User registration and authentication with JWT
- ✓ Product catalog with search and filtering
- ✓ Shopping cart management
- ✓ Order processing and history
- ✓ Inventory management with stock tracking
- ✓ Real-time stock updates
- ✓ AI-powered product recommendations
- ✓ User behavior analysis and insights
- ✓ Sales trend analysis
- ✓ Inventory optimization suggestions

### Microservices Features
- ✓ Service discovery with Eureka
- ✓ Centralized configuration management
- ✓ API Gateway with intelligent routing
- ✓ Distributed caching with Redis
- ✓ Circuit breaker pattern for resilience
- ✓ Rate limiting for API protection
- ✓ Distributed tracing with Zipkin

### Monitoring & Observability
- ✓ Health checks and metrics collection
- ✓ Distributed tracing with Zipkin
- ✓ Prometheus metrics integration
- ✓ Request/response logging
- ✓ Performance monitoring
- ✓ Custom business metrics

### Security Features
- ✓ JWT-based authentication
- ✓ Rate limiting per user and IP
- ✓ CORS configuration
- ✓ Input validation and sanitization
- ✓ Secure password encoding
- ✓ Security headers implementation

### Development Features
- ✓ Comprehensive API documentation
- ✓ Structured logging with JSON format
- ✓ Environment-specific configurations
- ✓ Unit and integration testing
- ✓ Database migration support
- ✓ Docker containerization

### AI Insights Features
- ✓ **Product Recommendations**: Personalized product suggestions based on user behavior
- ✓ **User Behavior Analysis**: Deep insights into customer interaction patterns
- ✓ **Sales Trend Analysis**: Market trend identification and forecasting
- ✓ **Inventory Optimization**: Smart inventory management recommendations
- ✓ **Customer Segmentation**: Advanced customer grouping and targeting
- ✓ **Price Optimization**: Dynamic pricing strategies and recommendations
- ✓ **Web Dashboard**: Interactive web interface for insight management
- ✓ **Multiple AI Providers**: Support for OpenAI and Anthropic APIs
- ✓ **Real-time Processing**: Fast insight generation and updates

## Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Set to `prod` for production mode
- `SPRING_REDIS_HOST`: Redis server hostname (default: localhost)
- `EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE`: Eureka server URL
- `JWT_SECRET`: JWT signing secret key
- `POSTGRES_PASSWORD`: PostgreSQL password
- `OPENAI_API_KEY`: OpenAI API key for AI insights
- `OPENAI_BASE_URL`: OpenAI API base URL (default: https://api.openai.com/v1)
- `ANTHROPIC_API_KEY`: Anthropic API key for alternative AI provider
- `ANTHROPIC_BASE_URL`: Anthropic API base URL (default: https://api.anthropic.com)

### Rate Limiting Configuration
- **Auth endpoints**: 10 requests/minute per user
- **Product endpoints**: 20 requests/minute per IP
- **Order endpoints**: 5 requests/minute per user

### Cache Configuration
- **User data**: 10 minutes TTL
- **Product data**: 5 minutes TTL
- **Order data**: 10 minutes TTL

## Database Schema

The platform uses a microservices database pattern with separate databases for each service:

- **userdb**: User authentication and profile data
- **productdb**: Product catalog and inventory data
- **orderdb**: Shopping cart and order data
- **aiinsightsdb**: AI insights, recommendations, and analytics data

Each database includes proper indexing, constraints, and migration scripts for schema management.

## AI Insights Service

The AI Insights Service provides intelligent analytics and recommendations for the e-commerce platform:

### Web Interface
- **Dashboard**: http://localhost:8084/web/insights
- **Insight Types**: http://localhost:8084/web/insights/types
- **API Documentation**: http://localhost:8084/swagger-ui.html

### Available Insight Types
1. **Product Recommendations** - Personalized product suggestions
2. **User Behavior Analysis** - Customer interaction insights
3. **Sales Trend Analysis** - Market trend identification
4. **Inventory Optimization** - Smart inventory management
5. **Customer Segmentation** - Advanced customer grouping
6. **Price Optimization** - Dynamic pricing strategies

### Usage Examples
```bash
# Generate product recommendations
curl -X POST http://localhost:8084/api/ai-insights/generate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "insightType": "product_recommendation",
    "data": {"purchaseHistory": [], "preferences": []},
    "context": "shopping_session"
  }'

# Get available insight types
curl http://localhost:8084/api/ai-insights/insight-types
```

## Development Guidelines

### Code Quality
- Follow Clean Code principles
- Use meaningful variable and method names
- Implement proper error handling
- Add comprehensive logging
- Write unit tests for critical functionality
- Use structured logging with JSON format

### Microservices Best Practices
- Each service has a single responsibility
- Services communicate via well-defined APIs
- Database per service pattern
- Stateless service design
- Proper error handling and fallback mechanisms
- Circuit breaker implementation

### Testing Strategy
- Unit tests for business logic
- Integration tests for API endpoints
- End-to-end tests for critical user flows
- Performance tests for scalability validation
- Security tests for vulnerability assessment

## Troubleshooting

### Common Issues
1. **Port conflicts**: Ensure all required ports are available
2. **Redis connection**: Verify Redis is running and accessible
3. **Service discovery**: Check Eureka server is running first
4. **Database issues**: Verify database connections and schemas
5. **JWT token issues**: Check JWT secret configuration

### Logs
- Check individual service logs for detailed error information
- Use Zipkin for tracing request flows
- Monitor Prometheus metrics for performance issues
- Review structured logs in JSON format

## Contributing

1. Follow the established code style and patterns
2. Add appropriate tests for new features
3. Update documentation for any API changes
4. Ensure all services start successfully
5. Verify integration tests pass
6. Update API documentation

## License

This project is licensed under the MIT License - see the LICENSE file for details.