package com.afperdomo2.movieverse.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.afperdomo2.movieverse.common.pagination.PageQuery;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.SuggestionRequestDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.service.MovieService;
import com.afperdomo2.movieverse.domain.service.MovieverseAiService;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private static final LocalDate RELEASE_DATE = LocalDate.of(2014, 11, 7);
    private static final UUID MOVIE_ID = UUID.fromString("019b76da-d2f8-700c-8004-00000c08d6b4");
    private static final UUID OTHER_MOVIE_ID = UUID.fromString("019b76da-bb88-7006-8002-000006046b5a");
    private static final UUID UNKNOWN_MOVIE_ID = UUID.fromString("019b76da-ee50-7013-8ab1-0000130dfe9d");

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
                List.of(movieDto(MOVIE_ID, "Interstellar")), 1, 10, 19, 2, true, false);
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
                List.of(movieDto(OTHER_MOVIE_ID, "Movie 6")), 2, 5, 19, 4, true, true);
        when(movieService.findPage(2, 5)).thenReturn(page);

        ResponseEntity<PageResponse<MovieDto>> response = movieController.findAll(query);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(movieService).findPage(2, 5);
    }

    @Test
    @DisplayName("findById devuelve 200 cuando la película existe")
    void findById_returnsOk_whenMovieExists() {
        MovieDto movie = movieDto(MOVIE_ID, "Interstellar");
        when(movieService.findById(MOVIE_ID)).thenReturn(movie);

        ResponseEntity<MovieDto> response = movieController.findById(MOVIE_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(movie);
        verify(movieService).findById(MOVIE_ID);
    }

    @Test
    @DisplayName("findById devuelve 404 cuando el servicio devuelve null")
    void findById_returnsNotFound_whenServiceReturnsNull() {
        when(movieService.findById(UNKNOWN_MOVIE_ID)).thenReturn(null);

        ResponseEntity<MovieDto> response = movieController.findById(UNKNOWN_MOVIE_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("create devuelve 201 con la película creada")
    void create_returnsCreated() {
        CreateMovieDto request = new CreateMovieDto("Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, 8.6);
        MovieDto created = movieDto(MOVIE_ID, "Interstellar");
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
        MovieDto updated = movieDto(MOVIE_ID, "Interstellar");
        when(movieService.update(MOVIE_ID, changes)).thenReturn(updated);

        ResponseEntity<MovieDto> response = movieController.update(MOVIE_ID, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(movieService).update(MOVIE_ID, changes);
    }

    @Test
    @DisplayName("update devuelve 404 cuando el servicio devuelve null")
    void update_returnsNotFound_whenServiceReturnsNull() {
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar", RELEASE_DATE, 9.0);
        when(movieService.update(UNKNOWN_MOVIE_ID, changes)).thenReturn(null);

        ResponseEntity<MovieDto> response = movieController.update(UNKNOWN_MOVIE_ID, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("patch devuelve 200 con la película actualizada")
    void patch_returnsOkWithUpdatedMovie() {
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar Remastered"), Optional.empty(),
                Optional.of(9.0));
        MovieDto updated = movieDto(MOVIE_ID, "Interstellar Remastered");
        when(movieService.patch(MOVIE_ID, changes)).thenReturn(updated);

        ResponseEntity<MovieDto> response = movieController.patch(MOVIE_ID, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(movieService).patch(MOVIE_ID, changes);
    }

    @Test
    @DisplayName("delete devuelve 204 cuando la película se elimina")
    void delete_returnsNoContent_whenMovieIsDeleted() {
        when(movieService.delete(MOVIE_ID)).thenReturn(true);

        ResponseEntity<Void> response = movieController.delete(MOVIE_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(movieService).delete(MOVIE_ID);
    }

    @Test
    @DisplayName("delete devuelve 404 cuando el servicio devuelve false")
    void delete_returnsNotFound_whenServiceReturnsFalse() {
        when(movieService.delete(UNKNOWN_MOVIE_ID)).thenReturn(false);

        ResponseEntity<Void> response = movieController.delete(UNKNOWN_MOVIE_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(movieService).delete(UNKNOWN_MOVIE_ID);
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

    private static MovieDto movieDto(UUID id, String title) {
        return new MovieDto(id, title, 169, Genre.SCI_FI, RELEASE_DATE, true, 8.6, "VERY_GOOD");
    }
}
