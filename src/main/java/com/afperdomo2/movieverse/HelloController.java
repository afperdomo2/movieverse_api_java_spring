package com.afperdomo2.movieverse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final MovieverseAiService movieverseAiService;

    public HelloController(MovieverseAiService movieverseAiService) {
        this.movieverseAiService = movieverseAiService;
    }

    @GetMapping("/")
    public String sayHello() {
        return movieverseAiService.generateGreeting();
    }
}
