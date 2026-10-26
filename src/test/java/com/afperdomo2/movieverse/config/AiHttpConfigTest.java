package com.afperdomo2.movieverse.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.web.client.RestClient;

class AiHttpConfigTest {

    private final AiHttpConfig config = new AiHttpConfig();

    @Test
    @DisplayName("Añade las cabeceras de OpenCode Go cuando el base-url es de opencode.ai")
    void aiGatewayHeaders_addsHeaders_whenBaseUrlIsOpenCode() {
        RestClientCustomizer customizer = config.aiGatewayHeaders(
                "https://opencode.ai/inference/go/openai", "session-123");
        RestClient.Builder builder = mock(RestClient.Builder.class);

        customizer.customize(builder);

        verify(builder).defaultHeader("x-opencode-session", "session-123");
        verify(builder).defaultHeader("User-Agent", "movieverse-api/1.0");
        verifyNoMoreInteractions(builder);
    }

    @Test
    @DisplayName("No añade cabeceras cuando el base-url es de OpenAI")
    void aiGatewayHeaders_addsNoHeaders_whenBaseUrlIsOpenAi() {
        RestClientCustomizer customizer = config.aiGatewayHeaders("https://api.openai.com", "session-123");
        RestClient.Builder builder = mock(RestClient.Builder.class);

        customizer.customize(builder);

        verifyNoInteractions(builder);
    }
}
