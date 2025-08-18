package com.onedev.customer_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springAppOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Customer Service API")
                        .description("RESTful API for Customer Service")
                        .version("v0.0.1-SNAPSHOT")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc OpenAPI Documentation")
                        .url("https://springdoc.org/"));
    }
}