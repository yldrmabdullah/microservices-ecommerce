FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY services/order-service/pom.xml ./services/order-service/

# Download dependencies
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -f services/order-service/pom.xml

# Copy source code
COPY services/order-service/src ./services/order-service/src

# Build the application
RUN mvn clean package -DskipTests -f services/order-service/pom.xml

# Run the application
EXPOSE 8082
CMD ["java", "-jar", "services/order-service/target/order-service-1.0.0.jar"]