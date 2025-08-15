package com.afperdomo2.movieverse.domain.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface MovieverseAiService {
    @UserMessage("""
            Genera un saludo de bienvenida a la plataforma de gestión de películas {{platform}}.
            Usa menos de 120 caracteres y hazlo con un estilo amigable.
            """)
    String generateGreeting(@V("platform") String platform);

    @SystemMessage("""
            Eres un experto en cine que recomienda películas personalizadas según los gustos del usuario.
            Debes recomendar máximo 3 películas.
            Solo incluye películas que estén dentro de la plataforma.
            """)
    String generateMoviesSuggestion(@UserMessage String userMessage);
}
