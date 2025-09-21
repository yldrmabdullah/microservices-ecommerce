# Monitoring and Observability

This directory contains monitoring configurations and dashboards for the Valven E-commerce Platform.

## Components

### 1. Prometheus
- **Configuration**: `prometheus-alerts.yml`
- **Purpose**: Metrics collection and alerting
- **Features**:
  - Service health monitoring
  - Performance metrics
  - Business metrics
  - Custom alerts

### 2. Grafana
- **Configuration**: `grafana-dashboard.json`
- **Purpose**: Visualization and dashboards
- **Features**:
  - Real-time monitoring
  - Custom dashboards
  - Alert visualization
  - Historical data analysis

### 3. AlertManager
- **Configuration**: `alertmanager.yml`
- **Purpose**: Alert routing and notification
- **Features**:
  - Multi-channel notifications
  - Alert grouping and deduplication
  - Escalation policies
  - Integration with PagerDuty, Slack, Email

## Metrics Collected

### Application Metrics
- **Request Rate**: HTTP requests per second
- **Response Time**: P50, P95, P99 percentiles
- **Error Rate**: 4xx and 5xx error rates
- **Throughput**: Requests processed per second

### JVM Metrics
- **Memory Usage**: Heap and non-heap memory
- **Garbage Collection**: GC pause times and frequency
- **Thread Count**: Active and daemon threads
- **Class Loading**: Classes loaded and unloaded

### Database Metrics
- **Connection Pool**: Active and idle connections
- **Query Performance**: Query execution times
- **Transaction Metrics**: Commits and rollbacks
- **Connection Errors**: Connection failures

### Redis Metrics
- **Memory Usage**: Used and available memory
- **Hit Rate**: Cache hit and miss ratios
- **Connected Clients**: Active connections
- **Operations**: Commands per second

### Business Metrics
- **Orders**: Created, completed, and failed orders
- **Cart**: Created, abandoned, and completed carts
- **Users**: Registrations and active users
- **Products**: Views and purchases

## Alerts

### Critical Alerts
- **Service Down**: Service is not responding
- **High Error Rate**: Error rate > 10%
- **Database Connection Pool Exhaustion**: > 90% connections used
- **Memory Usage**: > 80% memory used
- **Circuit Breaker Open**: Circuit breaker is open

### Warning Alerts
- **High Response Time**: P95 > 2 seconds
- **High CPU Usage**: > 80% CPU usage
- **JVM GC Issues**: High GC pause times
- **Rate Limiting**: Rate limiting being triggered
- **Authentication Failures**: High failure rate

### Business Alerts
- **Low Order Volume**: < 1 order per hour
- **High Cart Abandonment**: > 80% abandonment rate
- **Product Stock Issues**: Low stock alerts
- **Payment Failures**: High payment failure rate

## Setup Instructions

### 1. Prometheus Setup
```bash
# Create Prometheus config
kubectl create configmap prometheus-config --from-file=prometheus-alerts.yml

# Deploy Prometheus
kubectl apply -f k8s/monitoring.yaml
```

### 2. Grafana Setup
```bash
# Import dashboard
kubectl apply -f k8s/monitoring.yaml

# Access Grafana
kubectl port-forward svc/grafana-service 3000:3000
```

### 3. AlertManager Setup
```bash
# Create AlertManager config
kubectl create configmap alertmanager-config --from-file=alertmanager.yml

# Deploy AlertManager
kubectl apply -f k8s/alertmanager.yaml
```

## Dashboard Features

### Overview Dashboard
- **Service Health**: Overall system health status
- **Request Metrics**: Rate, latency, and error rates
- **Resource Usage**: CPU, memory, and disk usage
- **Business Metrics**: Orders, users, and revenue

### Service-Specific Dashboards
- **Gateway Dashboard**: API gateway specific metrics
- **User Service Dashboard**: Authentication and user metrics
- **Product Service Dashboard**: Product catalog metrics
- **Order Service Dashboard**: Order processing metrics

### Infrastructure Dashboard
- **Kubernetes Metrics**: Pod, node, and cluster metrics
- **Database Metrics**: PostgreSQL performance
- **Redis Metrics**: Cache performance
- **Network Metrics**: Network latency and throughput

## Alerting Rules

### Severity Levels
- **Critical**: Immediate attention required
- **Warning**: Attention needed within hours
- **Info**: Informational alerts

### Notification Channels
- **Email**: Team-specific email notifications
- **Slack**: Real-time Slack notifications
- **PagerDuty**: Critical alert escalation
- **Webhook**: Custom webhook integrations

### Alert Grouping
- **By Service**: Alerts grouped by service
- **By Severity**: Alerts grouped by severity
- **By Time**: Alerts grouped by time window

## Best Practices

### 1. Alert Design
- **Clear Descriptions**: Descriptive alert messages
- **Runbook Links**: Include runbook URLs
- **Proper Thresholds**: Set appropriate alert thresholds
- **Avoid Alert Fatigue**: Use proper grouping and timing

### 2. Dashboard Design
- **Logical Grouping**: Group related metrics
- **Color Coding**: Use consistent color schemes
- **Time Ranges**: Provide multiple time range options
- **Drill-down**: Enable detailed investigation

### 3. Monitoring Strategy
- **SLI/SLO**: Define service level indicators
- **Error Budgets**: Track error budgets
- **Capacity Planning**: Monitor resource usage trends
- **Performance Baselines**: Establish performance baselines

## Troubleshooting

### Common Issues
1. **Missing Metrics**: Check service annotations
2. **Alert Not Firing**: Verify alert rules and thresholds
3. **Dashboard Not Loading**: Check data source configuration
4. **High Alert Volume**: Review alert grouping and thresholds

### Debug Commands
```bash
# Check Prometheus targets
kubectl port-forward svc/prometheus-service 9090:9090
# Open http://localhost:9090/targets

# Check AlertManager status
kubectl port-forward svc/alertmanager-service 9093:9093
# Open http://localhost:9093

# Check Grafana logs
kubectl logs -f deployment/grafana
```

## Integration

### CI/CD Integration
- **Deployment Monitoring**: Monitor deployment success
- **Performance Regression**: Detect performance issues
- **Security Alerts**: Monitor security events
- **Business Impact**: Track business metrics

### External Integrations
- **PagerDuty**: Critical alert escalation
- **Slack**: Team notifications
- **Email**: Detailed alert reports
- **Webhooks**: Custom integrations

## Maintenance

### Regular Tasks
- **Review Alerts**: Monthly alert review
- **Update Dashboards**: Quarterly dashboard updates
- **Capacity Planning**: Monthly capacity reviews
- **Performance Tuning**: Ongoing optimization

### Monitoring Health
- **Alert Coverage**: Ensure all services are monitored
- **Dashboard Usage**: Track dashboard usage
- **Alert Accuracy**: Monitor false positive rates
- **Response Times**: Track alert response times