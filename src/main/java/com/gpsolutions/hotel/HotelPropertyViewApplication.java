package com.gpsolutions.hotel;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot application class for Hotel Property View API.
 * Runs on port 8092.
 */
@SpringBootApplication
public class HotelPropertyViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelPropertyViewApplication.class, args);
    }

    /**
     * Configure OpenAPI documentation.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Property View API")
                        .version("1.0.0")
                        .description("RESTful API for hotel management and property view operations")
                        .contact(new Contact()
                                .name("GP Solutions")
                                .url("https://gpsolutions.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
