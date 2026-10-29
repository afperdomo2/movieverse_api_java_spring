package com.afperdomo2.movieverse.domain.dto.movie;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import com.afperdomo2.movieverse.domain.Genre;

class PatchMovieDtoValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("Un PATCH sin campos es inválido")
    void emptyPatch_isRejected() {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(messages(validator.validate(dto)))
                .containsExactly("Debes enviar al menos un campo para actualizar");
    }

    @Test
    @DisplayName("Los campos nulos se normalizan a vacío y el PATCH sigue siendo inválido")
    void nullFields_areNormalizedToEmpty() {
        PatchMovieDto dto = new PatchMovieDto(null, null, null);

        assertThat(dto.title()).isEmpty();
        assertThat(dto.releaseDate()).isEmpty();
        assertThat(dto.rating()).isEmpty();
        assertThat(messages(validator.validate(dto)))
                .containsExactly("Debes enviar al menos un campo para actualizar");
    }

    @Test
    @DisplayName("Un PATCH solo con título válido es válido")
    void titleOnly_isValid() {
        PatchMovieDto dto = new PatchMovieDto(Optional.of("Interstellar"), Optional.empty(), Optional.empty());

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("El título en blanco es inválido")
    void blankTitle_isRejected() {
        PatchMovieDto dto = new PatchMovieDto(Optional.of("   "), Optional.empty(), Optional.empty());

        assertThat(messages(validator.validate(dto))).containsExactly("El título es obligatorio");
    }

    @Test
    @DisplayName("El título no puede superar los 255 caracteres")
    void longTitle_isRejected() {
        PatchMovieDto dto = new PatchMovieDto(Optional.of("a".repeat(256)), Optional.empty(), Optional.empty());

        assertThat(messages(validator.validate(dto)))
                .containsExactly("El título no puede superar los 255 caracteres");
    }

    @Test
    @DisplayName("Un título de 255 caracteres es válido")
    void titleAtMaximumLength_isAccepted() {
        PatchMovieDto dto = new PatchMovieDto(Optional.of("a".repeat(255)), Optional.empty(), Optional.empty());

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("La fecha de lanzamiento futura es inválida")
    void futureReleaseDate_isRejected() {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.of(LocalDate.now().plusDays(1)),
                Optional.empty());

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La fecha de lanzamiento no puede ser mayor a la fecha actual");
    }

    @ParameterizedTest
    @ValueSource(doubles = { -0.1, 9.5 })
    @DisplayName("La calificación debe estar entre 0 y 9")
    void outOfRangeRating_isRejected(double rating) {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.of(rating));

        assertThat(messages(validator.validate(dto))).hasSize(1);
        assertThat(messages(validator.validate(dto)).iterator().next()).contains("La calificación debe ser");
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.0, 9.0 })
    @DisplayName("Los límites 0 y 9 son válidos")
    void boundaryRating_isAccepted(double rating) {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.of(rating));

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("Un PATCH solo con género es válido")
    void genreOnly_isValid() {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(Genre.COMEDY), Optional.empty());

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("Un PATCH solo con isActive en false es válido")
    void activeOnly_isValid() {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(false));

        assertThat(dto.active()).contains(false);
        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("Un PATCH con todos los campos válidos es válido")
    void allFields_isValid() {
        PatchMovieDto dto = new PatchMovieDto(
                Optional.of("Interstellar"),
                Optional.of(LocalDate.of(2014, 11, 7)),
                Optional.of(8.6),
                Optional.of(Genre.SCI_FI),
                Optional.of(true));

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("Un PATCH solo con authorIds es válido")
    void authorIdsOnly_isValid() {
        PatchMovieDto dto = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(),
                Optional.of(Set.of(UUID.fromString("019b7c01-0bd0-7003-a001-0000030235ad"))));

        assertThat(validator.validate(dto)).isEmpty();
    }

    private static Set<String> messages(Set<ConstraintViolation<PatchMovieDto>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
}
