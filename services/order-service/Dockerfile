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

# Copy the built JAR to the working directory
RUN cp services/order-service/target/*.jar app.jar

# Run the application
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]