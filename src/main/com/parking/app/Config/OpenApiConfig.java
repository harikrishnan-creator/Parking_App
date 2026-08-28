package com.parking.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI parkingOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Parking Bill Generator API")
                        .version("1.0.0")
                        .description(
                                "REST APIs for Parking Management System")
                        .contact(
                                new Contact()
                                        .name("Parking Admin")
                                        .email("admin@parking.com")
                                        .url("https://parking.com"))
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
