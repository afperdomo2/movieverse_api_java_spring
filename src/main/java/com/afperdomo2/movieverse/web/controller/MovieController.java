package com.afperdomo2.movieverse.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.SuggestionRequestDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.service.MovieService;
import com.afperdomo2.movieverse.domain.service.MovieverseAiService;

@RestController
@RequestMapping("movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieverseAiService movieverseAiService;

    public MovieController(MovieService movieService, MovieverseAiService movieverseAiService) {
        this.movieService = movieService;
        this.movieverseAiService = movieverseAiService;
    }

    @GetMapping()
    public ResponseEntity<List<MovieDto>> findAll() {
        List<MovieDto> movies = this.movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> findById(@PathVariable("id") long id) {
        MovieDto movie = this.movieService.findById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    @PostMapping()
    public ResponseEntity<MovieDto> create(@RequestBody MovieDto movieDto) {
        MovieDto movieCreated = this.movieService.create(movieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> update(@PathVariable("id") long id, @RequestBody UpdateMovieDto changes) {
        MovieDto updatedMovie = this.movieService.update(id, changes);
        if (updatedMovie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        boolean deleted = this.movieService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/suggestion")
    public ResponseEntity<String> generateMoviesSuggestion(@RequestBody SuggestionRequestDto suggestionRequest) {
        String response = this.movieverseAiService.generateMoviesSuggestion(suggestionRequest.userPreferences());
        return ResponseEntity.ok(response);
    }
}