FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY services/product-service/pom.xml ./services/product-service/

# Download dependencies
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -f services/product-service/pom.xml

# Copy source code
COPY services/product-service/src ./services/product-service/src

# Build the application
RUN mvn clean package -DskipTests -f services/product-service/pom.xml

# Run the application
EXPOSE 8081
CMD ["java", "-jar", "services/product-service/target/product-service-1.0.0.jar"]