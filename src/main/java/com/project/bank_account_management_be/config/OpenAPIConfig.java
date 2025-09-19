package com.project.bank_account_management_be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Account Management API")
                        .description("API REST per la gestione di conti bancari e carte di pagamento")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@bankproject.com")
                                .url("https://www.bankproject.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/bankaccountmanagement")
                                .description("Server di sviluppo locale")));
    }
}