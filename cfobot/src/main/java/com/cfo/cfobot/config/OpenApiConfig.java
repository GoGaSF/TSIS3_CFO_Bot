package com.cfo.cfobot.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cfobotOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("CFO Bot API")
                        .description("Cloud Economics Chatbot for estimating monthly cloud infrastructure costs")
                        .version("1.0")
                        .contact(new Contact()
                                .name("CFO Bot Project")
                                .email("team@cfobot.com")
                        )
                        .license(new License()
                                .name("Educational Project")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                );
    }
}