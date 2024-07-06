package com.osayijoy.eventbooking.config.properties;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Joy Osayi
 * @createdOn Jun-29(Sat)-2024
 */

@Configuration
@RequiredArgsConstructor
public class EventBookingSwaggerConfig {

    @Value("${springdoc.swagger-ui.url}")
    private String swaggerUiUrl;

    @Bean
    public OpenAPI eventBookingOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Event Booking Service APIs")
                                .description(
                                        "This documentation contains all the APIs exposed for the Event Booking Management. Aside from the authentication and reset password APIs, all other APIs require a valid authenticated user JWT access token before they can be invoked")
                                .version("v1.0.0")
                                .license(
                                        new License()
                                                .name("Proprietary License")
                                                .url("http://localhost:2760/download-license")))
                .addServersItem(new Server().description("Local Server").url("http://localhost:8080"))
                .addServersItem(
                        new Server()
                                .description("Event Booking Server")
                                .url(swaggerUiUrl))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi eventBookingApi() {
        return GroupedOpenApi.builder().group("eventBookingService").pathsToMatch("/api/v1/**").build();
    }


}

