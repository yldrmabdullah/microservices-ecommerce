# Performance Testing Suite

This directory contains performance and load testing tools for the Valven E-commerce Platform.

## Test Types

### 1. JMeter Load Tests
- **File**: `src/test/jmeter/ecommerce-load-test.jmx`
- **Purpose**: Comprehensive load testing with realistic user scenarios
- **Features**:
  - User registration and authentication
  - Product browsing and search
  - Shopping cart operations
  - Order processing
  - Configurable user count and ramp-up time

### 2. Gatling Performance Tests
- **File**: `src/test/scala/com/valven/ecommerce/EcommerceLoadTest.scala`
- **Purpose**: High-performance load testing with detailed metrics
- **Features**:
  - Multiple concurrent scenarios
  - Response time assertions
  - Success rate monitoring
  - Configurable load parameters

### 3. Unit Performance Tests
- **File**: `src/test/java/com/valven/ecommerce/PerformanceTestSuite.java`
- **Purpose**: Component-level performance validation
- **Features**:
  - Database connection performance
  - Redis connection performance
  - Memory usage monitoring
  - Concurrent request handling

## Running Tests

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker (for TestContainers)
- Running E-commerce Platform

### JMeter Tests
```bash
# Run JMeter tests
mvn jmeter:jmeter

# Run with custom parameters
mvn jmeter:jmeter -DuserCount=100 -DrampUp=120
```

### Gatling Tests
```bash
# Run Gatling tests
mvn gatling:test

# Run with custom parameters
mvn gatling:test -DuserCount=200 -DrampUpTime=180 -DtestDuration=600
```

### Unit Performance Tests
```bash
# Run unit performance tests
mvn test -Dtest=PerformanceTestSuite
```

## Test Configuration

### Environment Variables
- `BASE_URL`: Base URL of the application (default: http://localhost:8080)
- `USER_COUNT`: Number of concurrent users (default: 50)
- `RAMP_UP`: Ramp-up time in seconds (default: 60)
- `TEST_DURATION`: Test duration in seconds (default: 300)

### Performance Thresholds
- **Response Time**: Mean < 1000ms, 95th percentile < 2000ms
- **Success Rate**: > 95%
- **Database Response**: < 1000ms
- **Redis Response**: < 500ms
- **Memory Usage**: < 512MB

## Test Scenarios

### 1. User Registration and Login
- Tests user registration flow
- Tests user authentication
- Measures response times for auth operations

### 2. Product Browsing
- Tests product listing
- Tests product search functionality
- Tests category filtering

### 3. Shopping Cart Operations
- Tests adding items to cart
- Tests removing items from cart
- Tests cart clearing

### 4. Order Processing
- Tests order creation
- Tests order history retrieval
- Tests order validation

### 5. High Load Search
- Tests product search under high load
- Tests system stability
- Tests response time degradation

## Monitoring

### Metrics Collected
- Response times (mean, min, max, percentiles)
- Throughput (requests per second)
- Error rates
- Memory usage
- CPU usage
- Database connection pool metrics

### Reports
- JMeter: HTML reports in `target/jmeter/results/`
- Gatling: HTML reports in `target/gatling-results/`
- Unit tests: Console output and JUnit reports

## Best Practices

1. **Start Small**: Begin with low user counts and gradually increase
2. **Monitor Resources**: Watch CPU, memory, and database connections
3. **Test Realistic Scenarios**: Use realistic data and user behaviors
4. **Baseline Performance**: Establish performance baselines before changes
5. **Regular Testing**: Run performance tests regularly in CI/CD pipeline

## Troubleshooting

### Common Issues
1. **Out of Memory**: Increase JVM heap size
2. **Connection Timeouts**: Check network configuration
3. **Database Locks**: Monitor database performance
4. **Redis Connection Issues**: Check Redis configuration

### Debug Mode
```bash
# Enable debug logging
mvn test -Dlogging.level.com.valven.ecommerce=DEBUG
```

## Continuous Integration

### GitHub Actions Example
```yaml
- name: Run Performance Tests
  run: |
    mvn clean test
    mvn jmeter:jmeter
    mvn gatling:test
```

### Performance Gates
- All tests must pass
- Response times must be within thresholds
- Success rates must be above 95%
- No memory leaks detected