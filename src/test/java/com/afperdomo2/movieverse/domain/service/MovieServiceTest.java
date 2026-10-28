package com.afperdomo2.movieverse.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private static final UUID MOVIE_ID = UUID.fromString("019b76da-d2f8-700c-8004-00000c08d6b4");
    private static final UUID OTHER_MOVIE_ID = UUID.fromString("019b76da-bb88-7006-8002-000006046b5a");
    private static final UUID UNKNOWN_MOVIE_ID = UUID.fromString("019b76da-ee50-7013-8ab1-0000130dfe9d");

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("findAll delega en el repositorio y devuelve su resultado")
    void findAll_returnsRepositoryResult() {
        List<MovieDto> movies = List.of(movieDto(MOVIE_ID, "Interstellar"), movieDto(OTHER_MOVIE_ID, "Inception"));
        when(movieRepository.findAll()).thenReturn(movies);

        List<MovieDto> result = movieService.findAll();

        assertThat(result).isEqualTo(movies);
        verify(movieRepository).findAll();
    }

    @Test
    @DisplayName("findAll devuelve una lista vacía cuando no hay películas")
    void findAll_returnsEmptyList_whenRepositoryIsEmpty() {
        when(movieRepository.findAll()).thenReturn(List.of());

        assertThat(movieService.findAll()).isEmpty();
        verify(movieRepository).findAll();
    }

    @Test
    @DisplayName("findPage delega en el repositorio")
    void findPage_delegatesToRepository() {
        PageResponse<MovieDto> page = new PageResponse<>(
                List.of(movieDto(MOVIE_ID, "Interstellar")), 1, 10, 19, 2, true, false);
        when(movieRepository.findPage(1, 10)).thenReturn(page);

        assertThat(movieService.findPage(1, 10)).isEqualTo(page);
        verify(movieRepository).findPage(1, 10);
    }

    @Test
    @DisplayName("findById delega en el repositorio")
    void findById_returnsRepositoryResult() {
        MovieDto movie = movieDto(MOVIE_ID, "Interstellar");
        when(movieRepository.findById(MOVIE_ID)).thenReturn(movie);

        assertThat(movieService.findById(MOVIE_ID)).isEqualTo(movie);
        verify(movieRepository).findById(MOVIE_ID);
    }

    @Test
    @DisplayName("findById propaga MovieNotFoundException")
    void findById_propagatesMovieNotFound() {
        when(movieRepository.findById(UNKNOWN_MOVIE_ID)).thenThrow(new MovieNotFoundException(UNKNOWN_MOVIE_ID));

        assertThatThrownBy(() -> movieService.findById(UNKNOWN_MOVIE_ID))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining(UNKNOWN_MOVIE_ID.toString());
        verify(movieRepository).findById(UNKNOWN_MOVIE_ID);
    }

    @Test
    @DisplayName("create delega en el repositorio")
    void create_delegatesToRepository() {
        CreateMovieDto request = createRequest("Interstellar");
        MovieDto created = movieDto(MOVIE_ID, "Interstellar");
        when(movieRepository.create(request)).thenReturn(created);

        assertThat(movieService.create(request)).isEqualTo(created);
        verify(movieRepository).create(request);
    }

    @Test
    @DisplayName("update delega en el repositorio")
    void update_delegatesToRepository() {
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar", LocalDate.of(2014, 11, 7), 8.6);
        MovieDto updated = movieDto(MOVIE_ID, "Interstellar");
        when(movieRepository.update(MOVIE_ID, changes)).thenReturn(updated);

        assertThat(movieService.update(MOVIE_ID, changes)).isEqualTo(updated);
        verify(movieRepository).update(MOVIE_ID, changes);
    }

    @Test
    @DisplayName("patch delega en el repositorio")
    void patch_delegatesToRepository() {
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar"), Optional.empty(), Optional.of(9.0));
        MovieDto updated = movieDto(MOVIE_ID, "Interstellar");
        when(movieRepository.patch(MOVIE_ID, changes)).thenReturn(updated);

        assertThat(movieService.patch(MOVIE_ID, changes)).isEqualTo(updated);
        verify(movieRepository).patch(MOVIE_ID, changes);
    }

    @Test
    @DisplayName("delete devuelve true cuando el repositorio borra la película")
    void delete_returnsTrue_whenRepositoryDeletes() {
        when(movieRepository.delete(MOVIE_ID)).thenReturn(true);

        assertThat(movieService.delete(MOVIE_ID)).isTrue();
        verify(movieRepository).delete(MOVIE_ID);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("delete devuelve false cuando el repositorio no borra la película")
    void delete_returnsFalse_whenRepositoryDoesNotDelete() {
        when(movieRepository.delete(MOVIE_ID)).thenReturn(false);

        assertThat(movieService.delete(MOVIE_ID)).isFalse();
        verify(movieRepository).delete(MOVIE_ID);
    }

    @Test
    @DisplayName("delete propaga MovieNotFoundException")
    void delete_propagatesMovieNotFound() {
        when(movieRepository.delete(UNKNOWN_MOVIE_ID)).thenThrow(new MovieNotFoundException(UNKNOWN_MOVIE_ID));

        assertThatThrownBy(() -> movieService.delete(UNKNOWN_MOVIE_ID))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining(UNKNOWN_MOVIE_ID.toString());
        verify(movieRepository).delete(UNKNOWN_MOVIE_ID);
    }

    private static MovieDto movieDto(UUID id, String title) {
        return new MovieDto(id, title, 169, Genre.SCI_FI, LocalDate.of(2014, 11, 7), true, 8.6, "VERY_GOOD",
                List.of());
    }

    private static CreateMovieDto createRequest(String title) {
        return new CreateMovieDto(title, 169, Genre.SCI_FI, LocalDate.of(2014, 11, 7), 8.6);
    }
}
