package com.afperdomo2.movieverse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Añade las cabeceras que exigen algunas pasarelas compatibles con OpenAI.
 * OpenCode Go rechaza las peticiones sin {@code x-opencode-session} (HTTP 400),
 * así que solo se envían cuando el base-url apunta a opencode.ai.
 */
@Configuration
public class AiHttpConfig {

    @Bean
    RestClientCustomizer aiGatewayHeaders(
            @Value("${spring.ai.openai.base-url:https://api.openai.com}") String baseUrl,
            @Value("${OPENCODE_SESSION:movieverse-api}") String sessionId) {
        return builder -> {
            if (baseUrl.contains("opencode.ai")) {
                builder.defaultHeader("x-opencode-session", sessionId);
                builder.defaultHeader("User-Agent", "movieverse-api/1.0");
            }
        };
    }
}
