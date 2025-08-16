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

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.SuggestionRequestDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.service.MovieService;
import com.afperdomo2.movieverse.domain.service.MovieverseAiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("movies")
@Tag(name = "Movies", description = "Operaciones relacionadas con películas")
public class MovieController {
    private final MovieService movieService;
    private final MovieverseAiService movieverseAiService;

    public MovieController(MovieService movieService, MovieverseAiService movieverseAiService) {
        this.movieService = movieService;
        this.movieverseAiService = movieverseAiService;
    }

    @GetMapping()
    @Operation(summary = "Obtener todas las películas", description = "Devuelve una lista de todas las películas")
    @ApiResponse(responseCode = "200", description = "Lista de películas encontrada")
    public ResponseEntity<List<MovieDto>> findAll() {
        List<MovieDto> movies = this.movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener película por ID", description = "Devuelve una película específica", responses = {
            @ApiResponse(responseCode = "200", description = "Película encontrada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content())
    })
    public ResponseEntity<MovieDto> findById(
            @Parameter(description = "Identificador de la película", example = "1") @PathVariable("id") long id) {
        MovieDto movie = this.movieService.findById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    @PostMapping()
    @Operation(summary = "Crear nueva película", description = "Crea una nueva película", responses = {
            @ApiResponse(responseCode = "201", description = "Película creada"),
            @ApiResponse(responseCode = "409", description = "Conflicto en la solicitud", content = @Content())
    })
    public ResponseEntity<MovieDto> create(@RequestBody @Valid CreateMovieDto movieDto) {
        MovieDto movieCreated = this.movieService.create(movieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieCreated);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar película", description = "Actualiza una película existente", responses = {
            @ApiResponse(responseCode = "200", description = "Película actualizada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Conflicto en la solicitud", content = @Content())
    })
    public ResponseEntity<MovieDto> update(
            @Parameter(description = "Identificador de la película", example = "1") @PathVariable("id") long id,
            @RequestBody @Valid UpdateMovieDto changes) {
        MovieDto updatedMovie = this.movieService.update(id, changes);
        if (updatedMovie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar película", description = "Elimina una película existente", responses = {
            @ApiResponse(responseCode = "204", description = "Película eliminada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content())
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador de la película", example = "9") @PathVariable("id") long id) {
        boolean deleted = this.movieService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/suggestion")
    @Operation(summary = "Generar sugerencias de películas", description = "Genera sugerencias de películas basadas en las preferencias del usuario")
    public ResponseEntity<String> generateMoviesSuggestion(@RequestBody SuggestionRequestDto suggestionRequest) {
        String response = this.movieverseAiService.generateMoviesSuggestion(suggestionRequest.userPreferences());
        return ResponseEntity.ok(response);
    }
}