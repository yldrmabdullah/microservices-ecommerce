# IDE Setup Guide

This guide helps you configure your IDE (IntelliJ IDEA) to work properly with the Valven e-commerce platform project.

## IntelliJ IDEA Configuration

### 1. Schema Registration Issue

If you see the error "URI is not registered (Settings | Languages & Frameworks | Schemas and DTDs)" for the OWASP suppressions file, follow these steps:

#### Option A: Register the Schema (Recommended)
1. Open **File** → **Settings** (or **IntelliJ IDEA** → **Preferences** on macOS)
2. Navigate to **Languages & Frameworks** → **Schemas and DTDs**
3. Click the **+** button to add a new schema
4. Fill in the following:
   - **URI**: `https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd`
   - **Location**: `https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd`
   - **File patterns**: `*owasp-suppressions*.xml`
5. Click **OK** to save

#### Option B: Use Simple Suppressions File
The project includes a simplified suppressions file (`owasp-suppressions-simple.xml`) that doesn't require schema validation. This is already configured in the Maven build.

### 2. Project Import

1. **Open Project**:
   - File → Open
   - Select the `valven` directory
   - Choose "Open as Project"

2. **Import Maven Project**:
   - IntelliJ should automatically detect the Maven project
   - If not, right-click on `pom.xml` → "Add as Maven Project"

3. **Configure JDK**:
   - File → Project Structure → Project
   - Set Project SDK to Java 21
   - Set Project language level to 21

### 3. Code Style Configuration

1. **Import Code Style**:
   - File → Settings → Editor → Code Style
   - Click the gear icon → Import Scheme
   - Select "IntelliJ IDEA code style XML"
   - Choose the provided code style file (if available)

2. **Configure Spotless**:
   - The project uses Spotless for code formatting
   - Run `mvn spotless:apply` to format code
   - Or configure IntelliJ to use the same formatting rules

### 4. Plugin Recommendations

Install these recommended plugins:

1. **Lombok**:
   - File → Settings → Plugins
   - Search for "Lombok" and install
   - Enable annotation processing: Settings → Build → Compiler → Annotation Processors

2. **SonarLint**:
   - Search for "SonarLint" and install
   - Configure to use project's SonarQube settings

3. **SpotBugs**:
   - Search for "SpotBugs" and install
   - Configure to use project's SpotBugs configuration

4. **Docker**:
   - Search for "Docker" and install
   - Useful for Docker Compose integration

### 5. Run Configurations

#### Maven Run Configurations

1. **Clean Compile**:
   - Run → Edit Configurations
   - Add new Maven configuration
   - Command line: `clean compile`
   - Working directory: Project root

2. **Run Tests**:
   - Command line: `test`
   - Working directory: Project root

3. **Run Integration Tests**:
   - Command line: `verify`
   - Working directory: Project root

4. **Code Quality Checks**:
   - Command line: `clean verify -Pci`
   - Working directory: Project root

#### Spring Boot Run Configurations

1. **Eureka Server**:
   - Run → Edit Configurations
   - Add new Spring Boot configuration
   - Main class: `com.valven.ecommerce.eurekaserver.EurekaServerApplication`
   - Module: `eureka-server`

2. **User Service**:
   - Main class: `com.valven.ecommerce.userservice.UserServiceApplication`
   - Module: `user-service`

3. **Product Service**:
   - Main class: `com.valven.ecommerce.productservice.ProductServiceApplication`
   - Module: `product-service`

4. **Order Service**:
   - Main class: `com.valven.ecommerce.orderservice.OrderServiceApplication`
   - Module: `order-service`

5. **Gateway**:
   - Main class: `com.valven.ecommerce.gateway.GatewayApplication`
   - Module: `gateway`

6. **UI Application**:
   - Main class: `com.valven.ecommerce.ui.UiApplication`
   - Module: `ui`

### 6. Database Configuration

1. **PostgreSQL**:
   - View → Tool Windows → Database
   - Add new data source → PostgreSQL
   - Configure connection details:
     - Host: localhost
     - Port: 5432
     - Database: userdb/productdb/orderdb
     - User: postgres
     - Password: (your password)

2. **Redis**:
   - Install Redis plugin if available
   - Configure connection: localhost:6379

### 7. Docker Integration

1. **Docker Compose**:
   - View → Tool Windows → Services
   - Add Docker Compose configuration
   - Select `docker-compose.yml`
   - Start services from the Services window

2. **Dockerfile Support**:
   - Right-click on Dockerfile → "Run Dockerfile"
   - Configure build context and options

### 8. Git Integration

1. **Configure Git**:
   - File → Settings → Version Control → Git
   - Set path to Git executable

2. **GitHub Integration**:
   - File → Settings → Version Control → GitHub
   - Add your GitHub account
   - Configure authentication

### 9. Troubleshooting

#### Common Issues

1. **Maven Import Issues**:
   - File → Invalidate Caches and Restart
   - Delete `.idea` folder and reimport

2. **Lombok Not Working**:
   - Enable annotation processing
   - Install Lombok plugin
   - Restart IDE

3. **Schema Validation Errors**:
   - Use the simple suppressions file
   - Or register the schema as described above

4. **Build Issues**:
   - Check Java version (should be 21)
   - Check Maven version (should be 3.8+)
   - Clean and rebuild project

#### Performance Optimization

1. **Increase Memory**:
   - Help → Edit Custom VM Options
   - Add: `-Xmx4g -XX:+UseG1GC`

2. **Disable Unused Plugins**:
   - File → Settings → Plugins
   - Disable plugins you don't use

3. **Exclude Directories**:
   - File → Project Structure → Modules
   - Exclude `target/`, `node_modules/`, etc.

### 10. Useful Shortcuts

- **Build Project**: Ctrl+F9 (Cmd+F9 on macOS)
- **Run Configuration**: Shift+F10 (Ctrl+R on macOS)
- **Debug Configuration**: Shift+F9 (Ctrl+D on macOS)
- **Reformat Code**: Ctrl+Alt+L (Cmd+Option+L on macOS)
- **Optimize Imports**: Ctrl+Alt+O (Cmd+Option+O on macOS)
- **Find in Files**: Ctrl+Shift+F (Cmd+Shift+F on macOS)
- **Replace in Files**: Ctrl+Shift+R (Cmd+Shift+R on macOS)

### 11. Code Quality Tools

The project includes several code quality tools that integrate with IntelliJ:

1. **SpotBugs**: Static analysis for bug detection
2. **PMD**: Code quality and style analysis
3. **Spotless**: Code formatting
4. **JaCoCo**: Code coverage
5. **OWASP**: Dependency vulnerability scanning

Configure these tools in the IDE for real-time feedback and analysis.

## VS Code Alternative

If you prefer VS Code:

1. Install the Extension Pack for Java
2. Install Spring Boot Extension Pack
3. Install Docker extension
4. Install GitLens extension
5. Configure Java 21 in settings

## Conclusion

Following this setup guide will ensure your IDE is properly configured for the Valven e-commerce platform project. The configuration supports all the project's features including microservices, Docker, testing, and code quality tools.