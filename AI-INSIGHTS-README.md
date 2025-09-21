# 🤖 AI Insights Service

## Overview

The AI Insights Service is a microservice that provides AI-powered analytics, recommendations, and insights for the e-commerce platform. It integrates with external AI services like OpenAI and Anthropic to generate intelligent recommendations and analysis.

## Features

- **AI-Powered Recommendations**: Generate personalized product recommendations
- **User Behavior Analysis**: Analyze user behavior patterns and preferences
- **Sales Trend Analysis**: Provide insights on sales trends and patterns
- **Inventory Optimization**: Suggest inventory optimization strategies
- **Customer Segmentation**: Segment customers based on behavior and preferences
- **Price Optimization**: Recommend optimal pricing strategies

## Configuration

### Environment Variables

The service requires the following environment variables to be set:

```bash
# AI Service Configuration
OPENAI_API_KEY=your-openai-api-key-here
OPENAI_BASE_URL=https://api.openai.com/v1
ANTHROPIC_API_KEY=your-anthropic-api-key-here
ANTHROPIC_BASE_URL=https://api.anthropic.com

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/aiinsightsdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your-password

# Redis Configuration
SPRING_REDIS_HOST=redis
```

### API Endpoints

- **POST** `/api/ai-insights/generate` - Generate AI insights
- **GET** `/api/ai-insights/health` - Health check
- **GET** `/api/ai-insights/insight-types` - Get available insight types
- **GET** `/swagger-ui.html` - API documentation

## Usage

### Generate Insight

```bash
curl -X POST http://localhost:8084/api/ai-insights/generate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "insightType": "product_recommendation",
    "data": {
      "purchaseHistory": [...],
      "preferences": [...]
    },
    "context": "shopping_session"
  }'
```

### Available Insight Types

- `product_recommendation` - Personalized product recommendations
- `user_behavior` - User behavior analysis
- `sales_trend` - Sales trend analysis
- `inventory_optimization` - Inventory optimization suggestions
- `customer_segmentation` - Customer segmentation analysis
- `price_optimization` - Price optimization recommendations

## Response Format

```json
{
  "id": "uuid",
  "userId": "user123",
  "insightType": "product_recommendation",
  "title": "Personalized Product Recommendations",
  "description": "AI-generated insight based on product_recommendation analysis",
  "recommendation": "Consider these products based on your preferences",
  "metadata": {
    "requestId": "uuid",
    "timestamp": "2024-01-01T00:00:00",
    "dataSize": 1024
  },
  "tags": ["recommendation", "personalization", "products"],
  "confidence": 0.85,
  "createdAt": "2024-01-01T00:00:00",
  "expiresAt": "2024-01-08T00:00:00",
  "status": "ACTIVE"
}
```

## Setup Instructions

1. **Set Environment Variables**: Copy `env.example` to `.env` and fill in your API keys
2. **Start the Service**: Use Docker Compose or run directly
3. **Configure AI Services**: Add your OpenAI and/or Anthropic API keys
4. **Test the Service**: Use the health check endpoint to verify it's running

## Security Notes

- Never commit API keys to version control
- Use environment variables for all sensitive configuration
- Rotate API keys regularly
- Monitor API usage and costs
- Implement rate limiting for AI service calls

## Monitoring

- Health checks available at `/actuator/health`
- Prometheus metrics at `/actuator/prometheus`
- Swagger UI documentation at `/swagger-ui.html`
- Distributed tracing with Zipkin

## Development

The service is built with:
- Spring Boot 3.3.5
- Java 21
- Spring WebFlux for reactive programming
- Spring Data JPA for data persistence
- Redis for caching
- OpenAPI 3 for documentation