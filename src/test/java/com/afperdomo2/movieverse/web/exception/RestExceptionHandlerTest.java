package com.afperdomo2.movieverse.web.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.domain.dto.movie.CreateMovieDto;
import com.afperdomo2.movieverse.domain.exception.AuthorAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.AuthorNotFoundException;
import com.afperdomo2.movieverse.domain.exception.MovieAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

class RestExceptionHandlerTest {

    private static final UUID UNKNOWN_MOVIE_ID = UUID.fromString("019b76da-ee50-7013-8ab1-0000130dfe9d");
    private static final UUID UNKNOWN_AUTHOR_ID = UUID.fromString("019b7c01-4a50-7013-8ab1-0000130dfe9d");

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    @DisplayName("MovieNotFoundException se traduce a 404")
    void handleMovieNotFound_returnsNotFound() {
        ResponseEntity<Error> response = handler.handleMovieNotFound(new MovieNotFoundException(UNKNOWN_MOVIE_ID));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(
                new Error("MovieNotFound", "La película con el ID '" + UNKNOWN_MOVIE_ID + "' no fue encontrada."));
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
    @DisplayName("AuthorNotFoundException se traduce a 404")
    void handleAuthorNotFound_returnsNotFound() {
        ResponseEntity<Error> response = handler.handleAuthorNotFound(new AuthorNotFoundException(UNKNOWN_AUTHOR_ID));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(
                new Error("AuthorNotFound", "El autor con el ID '" + UNKNOWN_AUTHOR_ID + "' no fue encontrado."));
    }

    @Test
    @DisplayName("AuthorAlreadyExistsException se traduce a 409")
    void handleAuthorAlreadyExists_returnsConflict() {
        ResponseEntity<Error> response = handler.handleAuthorAlreadyExists(
                new AuthorAlreadyExistsException("Christopher", "Nolan"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(
                new Error("AuthorAlreadyExists", "El autor 'Christopher Nolan' ya existe."));
    }

    @Test
    @DisplayName("Un UUID inválido en la ruta se traduce a 400")
    void handleTypeMismatch_returnsBadRequest() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "no-es-un-uuid", UUID.class, "id", null, null);

        ResponseEntity<Error> response = handler.handleTypeMismatch(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(
                new Error("InvalidRequest", "El valor 'no-es-un-uuid' no es válido para el parámetro 'id'."));
    }

    @Test
    @DisplayName("Un conflicto de bloqueo optimista se traduce a 409")
    void handleOptimisticLock_returnsConflict() {
        ResponseEntity<Error> response = handler.handleOptimisticLock(
                new ObjectOptimisticLockingFailureException(MovieEntity.class, 1L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(new Error("OptimisticLockingFailure",
                "El registro fue modificado por otra operación. Vuelve a intentarlo."));
    }

    @Test
    @DisplayName("Un body ilegible se traduce a 400")
    void handleUnreadableMessage_returnsBadRequest() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "Género inválido: Acción", new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Error> response = handler.handleUnreadableMessage(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(new Error("InvalidRequest", "Género inválido: Acción"));
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
        CreateMovieDto target = new CreateMovieDto("Interstellar", 169, Genre.SCI_FI,
                LocalDate.of(2014, 11, 7), 8.6);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "createMovieDto");
        for (int i = 0; i < fields.length; i++) {
            bindingResult.rejectValue(fields[i], "invalid", messages[i]);
        }
        return bindingResult;
    }
}
