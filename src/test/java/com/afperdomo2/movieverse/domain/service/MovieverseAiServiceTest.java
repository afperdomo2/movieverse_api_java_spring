package com.afperdomo2.movieverse.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

@ExtendWith(MockitoExtension.class)
class MovieverseAiServiceTest {

    @Mock
    private ChatClient.Builder builder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    @Mock
    private ChatClient.PromptUserSpec promptUserSpec;

    @Mock
    private MovieService movieService;

    private MovieverseAiService service;

    @BeforeEach
    void setUp() {
        when(builder.build()).thenReturn(chatClient);
        service = new MovieverseAiService(builder, movieService);
    }

    @Test
    @DisplayName("El constructor construye el ChatClient a partir del builder")
    void constructor_buildsChatClient() {
        verify(builder).build();
    }

    @Test
    @DisplayName("generateGreeting devuelve el contenido generado y envía el nombre de la plataforma")
    void generateGreeting_returnsContent_andPassesPlatform() {
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(ArgumentMatchers.<Consumer<ChatClient.PromptUserSpec>>any())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("¡Bienvenido a movieverse!");
        when(promptUserSpec.text(anyString())).thenReturn(promptUserSpec);
        when(promptUserSpec.param(anyString(), any())).thenReturn(promptUserSpec);

        String greeting = service.generateGreeting("movieverse");

        assertThat(greeting).isEqualTo("¡Bienvenido a movieverse!");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Consumer<ChatClient.PromptUserSpec>> userSpecCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(requestSpec).user(userSpecCaptor.capture());
        userSpecCaptor.getValue().accept(promptUserSpec);

        verify(promptUserSpec).text(argThat((String text) -> text.contains("{platform}")));
        verify(promptUserSpec).param("platform", "movieverse");
    }

    @Test
    @DisplayName("generateMoviesSuggestion usa el system prompt, el mensaje del usuario y las tools")
    void generateMoviesSuggestion_usesSystemPromptUserMessageAndTools() {
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.tools(any(Object.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn("Te recomiendo Interstellar");

        String suggestion = service.generateMoviesSuggestion("Me gustan las películas de ciencia ficción");

        assertThat(suggestion).isEqualTo("Te recomiendo Interstellar");
        verify(requestSpec).system(argThat((String prompt) -> prompt.contains("experto en cine")
                && prompt.contains("máximo 3 películas")));
        verify(requestSpec).user("Me gustan las películas de ciencia ficción");
        verify(requestSpec).tools(movieService);
    }

    @Test
    @DisplayName("Los métodos propagan el contenido null del modelo")
    void generateMoviesSuggestion_returnsNull_whenContentIsNull() {
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.tools(any(Object.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn(null);

        assertThat(service.generateMoviesSuggestion("cualquier preferencia")).isNull();
    }
}
