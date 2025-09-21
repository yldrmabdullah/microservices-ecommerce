FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY ui/pom.xml ./ui/

# Download dependencies
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -f ui/pom.xml

# Copy source code
COPY ui/src ./ui/src

# Build the application
RUN mvn clean package -DskipTests -f ui/pom.xml

# Run the application
EXPOSE 8085
CMD ["java", "-jar", "ui/target/ui-1.0.0.jar"]