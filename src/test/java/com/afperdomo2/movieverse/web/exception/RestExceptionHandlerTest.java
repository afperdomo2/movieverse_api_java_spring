package com.afperdomo2.movieverse.web.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.exception.MovieAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    @DisplayName("MovieNotFoundException se traduce a 404")
    void handleMovieNotFound_returnsNotFound() {
        ResponseEntity<Error> response = handler.handleMovieNotFound(new MovieNotFoundException(99L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(
                new Error("MovieNotFound", "La película con el ID '99' no fue encontrada."));
    }

    @Test
    @DisplayName("MovieAlreadyExistsException se traduce a 409")
    void handleMovieAlreadyExists_returnsConflict() {
        ResponseEntity<Error> response = handler.handleMovieAlreadyExists(
                new MovieAlreadyExistsException("Interstellar"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(
                new Error("MovieAlreadyExists", "La película con el título 'Interstellar' ya existe."));
    }

    @Test
    @DisplayName("Una excepción genérica se traduce a 500")
    void handleGenericException_returnsInternalServerError() {
        ResponseEntity<Error> response = handler.handleGenericException(new IllegalStateException("fallo inesperado"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(new Error("InternalServerError", "fallo inesperado"));
    }

    @Test
    @DisplayName("Un error de validación se traduce a 400 con un Error por campo")
    void handleMethodArgumentNotValid_returnsBadRequestWithFieldErrors() throws Exception {
        BindingResult bindingResult = bindingResultWith(
                new String[] { "title", "duration" },
                new String[] { "El título es obligatorio", "La duración es obligatoria" });
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(String.class.getDeclaredMethod("length"), -1), bindingResult);

        ResponseEntity<List<Error>> response = handler.handleMethodArgumentNotValid(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsExactly(
                new Error("title", "El título es obligatorio"),
                new Error("duration", "La duración es obligatoria"));
    }

    @Test
    @DisplayName("Un único error de validación produce una lista con un elemento")
    void handleMethodArgumentNotValid_returnsSingleError() throws Exception {
        BindingResult bindingResult = bindingResultWith(
                new String[] { "title" },
                new String[] { "El título es obligatorio" });
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                mock(MethodParameter.class), bindingResult);

        ResponseEntity<List<Error>> response = handler.handleMethodArgumentNotValid(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsExactly(new Error("title", "El título es obligatorio"));
    }

    private static BindingResult bindingResultWith(String[] fields, String[] messages) {
        CreateMovieDto target = new CreateMovieDto(null, "Interstellar", 169, "Sci-Fi",
                LocalDate.of(2014, 11, 7), 8.6, null);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "createMovieDto");
        for (int i = 0; i < fields.length; i++) {
            bindingResult.rejectValue(fields[i], "invalid", messages[i]);
        }
        return bindingResult;
    }
}
