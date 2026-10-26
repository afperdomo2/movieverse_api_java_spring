package com.afperdomo2.movieverse.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.afperdomo2.movieverse.domain.service.MovieverseAiService;

@ExtendWith(MockitoExtension.class)
class HelloControllerTest {

    private static final String PLATFORM = "movieverse";

    @Mock
    private MovieverseAiService movieverseAiService;

    @Test
    @DisplayName("sayHello devuelve el saludo generado por el servicio de IA")
    void sayHello_returnsGreeting() {
        when(movieverseAiService.generateGreeting(PLATFORM)).thenReturn("¡Bienvenido a movieverse!");
        HelloController controller = new HelloController(PLATFORM, movieverseAiService);

        assertThat(controller.sayHello()).isEqualTo("¡Bienvenido a movieverse!");
        verify(movieverseAiService).generateGreeting(PLATFORM);
    }

    @Test
    @DisplayName("sayHello propaga la excepción del servicio de IA")
    void sayHello_propagatesException() {
        when(movieverseAiService.generateGreeting(PLATFORM)).thenThrow(new IllegalStateException("IA no disponible"));
        HelloController controller = new HelloController(PLATFORM, movieverseAiService);

        assertThatThrownBy(controller::sayHello)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("IA no disponible");
    }
}
