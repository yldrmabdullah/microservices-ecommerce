FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY gateway/pom.xml ./gateway/

# Download dependencies
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -f gateway/pom.xml

# Copy source code
COPY gateway/src ./gateway/src

# Build the application
RUN mvn clean package -DskipTests -f gateway/pom.xml

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "gateway/target/gateway-1.0.0.jar"]