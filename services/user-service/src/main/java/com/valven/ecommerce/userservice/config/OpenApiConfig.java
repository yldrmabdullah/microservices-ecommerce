package com.valven.ecommerce.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("User management and authentication service for Valven E-commerce Platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Valven Development Team")
                                .email("dev@valven.com")
                                .url("https://valven.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083")
                                .description("Development Server"),
                        new Server()
                                .url("http://localhost:8080/api/auth")
                                .description("Production Server (via Gateway)")
                ));
    }
}