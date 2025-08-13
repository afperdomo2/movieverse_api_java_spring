package com.afperdomo2.movieverse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final String platform;
    private final MovieverseAiService movieverseAiService;

    public HelloController(@Value("${spring.application.name}") String platform,
            MovieverseAiService movieverseAiService) {
        this.platform = platform;
        this.movieverseAiService = movieverseAiService;
    }

    @GetMapping("/")
    public String sayHello() {
        return movieverseAiService.generateGreeting(this.platform);
    }
}
