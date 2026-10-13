package com.skincare.ecommerce.config;

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
    public OpenAPI skincareOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Skincare E-Commerce REST API")
                        .description("Comprehensive RESTful API for skincare e-commerce platform. Features dynamic product filtering with JPA specifications, category management, and image upload capabilities.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Backend Development Team")
                                .email("dev@skincare-ecommerce.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ));
    }
}
