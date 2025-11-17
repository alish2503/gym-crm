package com.gymcrm.presentation.config;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alish
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public GlobalOpenApiCustomizer jsonOnlyResponses() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    if (responses != null) {
                        responses.forEach((key, apiResponse) -> {
                            if (apiResponse.getContent() != null) {
                                apiResponse.setContent(
                                        new Content().addMediaType("application/json", new MediaType())
                                );
                            }
                        });
                    }
                })
        );
    }
}
