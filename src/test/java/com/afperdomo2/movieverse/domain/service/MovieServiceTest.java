package com.afperdomo2.movieverse.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("findAll delega en el repositorio y devuelve su resultado")
    void findAll_returnsRepositoryResult() {
        List<MovieDto> movies = List.of(movieDto(1L, "Interstellar"), movieDto(2L, "Inception"));
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
    @DisplayName("findById delega en el repositorio")
    void findById_returnsRepositoryResult() {
        MovieDto movie = movieDto(1L, "Interstellar");
        when(movieRepository.findById(1L)).thenReturn(movie);

        assertThat(movieService.findById(1L)).isEqualTo(movie);
        verify(movieRepository).findById(1L);
    }

    @Test
    @DisplayName("findById propaga MovieNotFoundException")
    void findById_propagatesMovieNotFound() {
        when(movieRepository.findById(99L)).thenThrow(new MovieNotFoundException(99L));

        assertThatThrownBy(() -> movieService.findById(99L))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining("99");
        verify(movieRepository).findById(99L);
    }

    @Test
    @DisplayName("create delega en el repositorio")
    void create_delegatesToRepository() {
        CreateMovieDto request = createRequest("Interstellar");
        MovieDto created = movieDto(1L, "Interstellar");
        when(movieRepository.create(request)).thenReturn(created);

        assertThat(movieService.create(request)).isEqualTo(created);
        verify(movieRepository).create(request);
    }

    @Test
    @DisplayName("update delega en el repositorio")
    void update_delegatesToRepository() {
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar", LocalDate.of(2014, 11, 7), 8.6);
        MovieDto updated = movieDto(1L, "Interstellar");
        when(movieRepository.update(1L, changes)).thenReturn(updated);

        assertThat(movieService.update(1L, changes)).isEqualTo(updated);
        verify(movieRepository).update(1L, changes);
    }

    @Test
    @DisplayName("delete devuelve true cuando el repositorio borra la película")
    void delete_returnsTrue_whenRepositoryDeletes() {
        when(movieRepository.delete(1L)).thenReturn(true);

        assertThat(movieService.delete(1L)).isTrue();
        verify(movieRepository).delete(1L);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("delete devuelve false cuando el repositorio no borra la película")
    void delete_returnsFalse_whenRepositoryDoesNotDelete() {
        when(movieRepository.delete(1L)).thenReturn(false);

        assertThat(movieService.delete(1L)).isFalse();
        verify(movieRepository).delete(1L);
    }

    @Test
    @DisplayName("delete propaga MovieNotFoundException")
    void delete_propagatesMovieNotFound() {
        when(movieRepository.delete(99L)).thenThrow(new MovieNotFoundException(99L));

        assertThatThrownBy(() -> movieService.delete(99L))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining("99");
        verify(movieRepository).delete(99L);
    }

    private static MovieDto movieDto(Long id, String title) {
        return new MovieDto(id, title, 169, "Sci-Fi", LocalDate.of(2014, 11, 7), "D", 8.6, "VERY_GOOD");
    }

    private static CreateMovieDto createRequest(String title) {
        return new CreateMovieDto(null, title, 169, "Sci-Fi", LocalDate.of(2014, 11, 7), 8.6, null);
    }
}
