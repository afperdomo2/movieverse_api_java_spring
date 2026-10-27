package com.afperdomo2.movieverse.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.PageQuery;
import com.afperdomo2.movieverse.domain.dto.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
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
    @DisplayName("findAll devuelve 200 con la página de películas")
    void findAll_returnsOkWithPage() {
        PageQuery query = new PageQuery();
        PageResponse<MovieDto> page = new PageResponse<>(
                List.of(movieDto(1L, "Interstellar")), 1, 10, 19, 2, true, false);
        when(movieService.findPage(1, 10)).thenReturn(page);

        ResponseEntity<PageResponse<MovieDto>> response = movieController.findAll(query);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(movieService).findPage(1, 10);
    }

    @Test
    @DisplayName("findAll usa los valores de page y limit de la query")
    void findAll_usesQueryParameters() {
        PageQuery query = new PageQuery();
        query.setPage(2);
        query.setLimit(5);
        PageResponse<MovieDto> page = new PageResponse<>(
                List.of(movieDto(6L, "Movie 6")), 2, 5, 19, 4, true, true);
        when(movieService.findPage(2, 5)).thenReturn(page);

        ResponseEntity<PageResponse<MovieDto>> response = movieController.findAll(query);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(movieService).findPage(2, 5);
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
        CreateMovieDto request = new CreateMovieDto("Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, 8.6);
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
    @DisplayName("patch devuelve 200 con la película actualizada")
    void patch_returnsOkWithUpdatedMovie() {
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar Remastered"), Optional.empty(),
                Optional.of(9.0));
        MovieDto updated = movieDto(1L, "Interstellar Remastered");
        when(movieService.patch(1L, changes)).thenReturn(updated);

        ResponseEntity<MovieDto> response = movieController.patch(1L, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(movieService).patch(1L, changes);
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
        return new MovieDto(id, title, 169, Genre.SCI_FI, RELEASE_DATE, true, 8.6, "VERY_GOOD");
    }
}
