package com.valven.ecommerce.orderservice.config;

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
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("Shopping cart and order processing service for Valven E-commerce Platform")
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
                                .url("http://localhost:8082")
                                .description("Development Server"),
                        new Server()
                                .url("http://localhost:8080/api/carts")
                                .description("Production Server (via Gateway)"),
                        new Server()
                                .url("http://localhost:8080/api/orders")
                                .description("Production Server (via Gateway)")
                ));
    }
}