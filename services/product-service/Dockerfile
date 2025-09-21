FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy everything
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests -f services/product-service/pom.xml

# Copy the built JAR to the working directory
RUN cp services/product-service/target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Run the application
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]