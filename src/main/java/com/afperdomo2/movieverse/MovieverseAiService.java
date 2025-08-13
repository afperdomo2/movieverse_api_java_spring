package com.afperdomo2.movieverse;

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
}
