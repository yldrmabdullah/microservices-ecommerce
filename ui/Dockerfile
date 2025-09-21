FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy everything
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests -f ui/pom.xml

# Copy the built JAR to the working directory
RUN cp ui/target/*.jar app.jar

# Run the application
EXPOSE 8085
CMD ["java", "-jar", "app.jar"]