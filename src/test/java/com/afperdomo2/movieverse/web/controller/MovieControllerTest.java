package com.afperdomo2.movieverse.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.SuggestionRequestDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.service.MovieService;
import com.afperdomo2.movieverse.domain.service.MovieverseAiService;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private static final LocalDate RELEASE_DATE = LocalDate.of(2014, 11, 7);

    @Mock
    private MovieService movieService;

    @Mock
    private MovieverseAiService movieverseAiService;

    @InjectMocks
    private MovieController movieController;

    @Test
    @DisplayName("findAll devuelve 200 con la lista de películas")
    void findAll_returnsOkWithMovies() {
        List<MovieDto> movies = List.of(movieDto(1L, "Interstellar"));
        when(movieService.findAll()).thenReturn(movies);

        ResponseEntity<List<MovieDto>> response = movieController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(movies);
        verify(movieService).findAll();
    }

    @Test
    @DisplayName("findById devuelve 200 cuando la película existe")
    void findById_returnsOk_whenMovieExists() {
        MovieDto movie = movieDto(1L, "Interstellar");
        when(movieService.findById(1L)).thenReturn(movie);

        ResponseEntity<MovieDto> response = movieController.findById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(movie);
        verify(movieService).findById(1L);
    }

    @Test
    @DisplayName("findById devuelve 404 cuando el servicio devuelve null")
    void findById_returnsNotFound_whenServiceReturnsNull() {
        when(movieService.findById(99L)).thenReturn(null);

        ResponseEntity<MovieDto> response = movieController.findById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("create devuelve 201 con la película creada")
    void create_returnsCreated() {
        CreateMovieDto request = new CreateMovieDto(null, "Interstellar", 169, "Sci-Fi", RELEASE_DATE, 8.6, null);
        MovieDto created = movieDto(1L, "Interstellar");
        when(movieService.create(request)).thenReturn(created);

        ResponseEntity<MovieDto> response = movieController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(created);
        verify(movieService).create(request);
    }

    @Test
    @DisplayName("update devuelve 200 cuando la película existe")
    void update_returnsOk_whenMovieExists() {
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar", RELEASE_DATE, 9.0);
        MovieDto updated = movieDto(1L, "Interstellar");
        when(movieService.update(1L, changes)).thenReturn(updated);

        ResponseEntity<MovieDto> response = movieController.update(1L, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(movieService).update(1L, changes);
    }

    @Test
    @DisplayName("update devuelve 404 cuando el servicio devuelve null")
    void update_returnsNotFound_whenServiceReturnsNull() {
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar", RELEASE_DATE, 9.0);
        when(movieService.update(99L, changes)).thenReturn(null);

        ResponseEntity<MovieDto> response = movieController.update(99L, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("delete devuelve 204 cuando la película se elimina")
    void delete_returnsNoContent_whenMovieIsDeleted() {
        when(movieService.delete(1L)).thenReturn(true);

        ResponseEntity<Void> response = movieController.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(movieService).delete(1L);
    }

    @Test
    @DisplayName("delete devuelve 404 cuando el servicio devuelve false")
    void delete_returnsNotFound_whenServiceReturnsFalse() {
        when(movieService.delete(99L)).thenReturn(false);

        ResponseEntity<Void> response = movieController.delete(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(movieService).delete(99L);
    }

    @Test
    @DisplayName("generateMoviesSuggestion devuelve 200 con la sugerencia generada")
    void generateMoviesSuggestion_returnsOkWithSuggestion() {
        SuggestionRequestDto request = new SuggestionRequestDto("Me gustan las películas de ciencia ficción");
        when(movieverseAiService.generateMoviesSuggestion(request.userPreferences())).thenReturn("Te recomiendo Interstellar");

        ResponseEntity<String> response = movieController.generateMoviesSuggestion(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Te recomiendo Interstellar");
        verify(movieverseAiService).generateMoviesSuggestion(request.userPreferences());
    }

    private static MovieDto movieDto(Long id, String title) {
        return new MovieDto(id, title, 169, "Sci-Fi", RELEASE_DATE, "D", 8.6, "VERY_GOOD");
    }
}
