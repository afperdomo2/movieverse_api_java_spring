package com.afperdomo2.movieverse.domain.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class MovieverseAiService {
    private static final String MOVIE_EXPERT_SYSTEM = """
            Eres un experto en cine que recomienda películas personalizadas según los gustos del usuario.
            Debes recomendar máximo 3 películas.
            Solo incluye películas que estén dentro de la plataforma.
            """;

    private final ChatClient chatClient;
    private final MovieService movieService;

    public MovieverseAiService(ChatClient.Builder builder, MovieService movieService) {
        this.chatClient = builder.build();
        this.movieService = movieService;
    }

    public String generateGreeting(String platform) {
        return chatClient.prompt()
                .user(u -> u.text("""
                        Genera un saludo de bienvenida a la plataforma de gestión de películas {platform}.
                        Usa menos de 120 caracteres y hazlo con un estilo amigable.
                        """)
                        .param("platform", platform))
                .call()
                .content();
    }

    public String generateMoviesSuggestion(String userMessage) {
        return chatClient.prompt()
                .system(MOVIE_EXPERT_SYSTEM)
                .user(userMessage)
                .tools(movieService)
                .call()
                .content();
    }
}
