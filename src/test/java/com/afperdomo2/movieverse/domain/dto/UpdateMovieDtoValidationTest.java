package com.afperdomo2.movieverse.domain.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UpdateMovieDtoValidationTest {

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
    @DisplayName("Un DTO completo y válido no produce violaciones")
    void validDto_hasNoViolations() {
        assertThat(validator.validate(validDto())).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   " })
    @DisplayName("El título nulo, vacío o en blanco es inválido")
    void blankTitle_isRejected(String title) {
        assertThat(messages(validator.validate(new UpdateMovieDto(title, LocalDate.of(2014, 11, 7), 8.6))))
                .containsExactly("El título es obligatorio");
    }

    @Test
    @DisplayName("El título no puede superar los 255 caracteres")
    void longTitle_isRejected() {
        UpdateMovieDto dto = new UpdateMovieDto("a".repeat(256), LocalDate.of(2014, 11, 7), 8.6);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("El título no puede superar los 255 caracteres");
    }

    @Test
    @DisplayName("Un título de 255 caracteres es válido")
    void titleAtMaximumLength_isAccepted() {
        UpdateMovieDto dto = new UpdateMovieDto("a".repeat(255), LocalDate.of(2014, 11, 7), 8.6);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("La fecha de lanzamiento futura es inválida")
    void futureReleaseDate_isRejected() {
        UpdateMovieDto dto = new UpdateMovieDto("Interstellar", LocalDate.now().plusDays(1), 8.6);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La fecha de lanzamiento no puede ser mayor a la fecha actual");
    }

    @ParameterizedTest
    @ValueSource(doubles = { -1.0, 10.0 })
    @DisplayName("La calificación debe estar entre 0 y 9")
    void outOfRangeRating_isRejected(double rating) {
        UpdateMovieDto dto = new UpdateMovieDto("Interstellar", LocalDate.of(2014, 11, 7), rating);

        assertThat(messages(validator.validate(dto))).hasSize(1);
        assertThat(messages(validator.validate(dto)).iterator().next())
                .contains("La calificación debe ser");
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.0, 9.0 })
    @DisplayName("Los límites 0 y 9 son válidos")
    void boundaryRating_isAccepted(double rating) {
        assertThat(validator.validate(new UpdateMovieDto("Interstellar", LocalDate.of(2014, 11, 7), rating)))
                .isEmpty();
    }

    @Test
    @DisplayName("La fecha de lanzamiento nula es inválida")
    void nullReleaseDate_isRejected() {
        UpdateMovieDto dto = new UpdateMovieDto("Interstellar", null, 8.6);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La fecha de lanzamiento es obligatoria");
    }

    @Test
    @DisplayName("La calificación nula sigue siendo válida")
    void nullRating_isAccepted() {
        assertThat(validator.validate(new UpdateMovieDto("Interstellar", LocalDate.of(2014, 11, 7), null)))
                .isEmpty();
    }

    private static UpdateMovieDto validDto() {
        return new UpdateMovieDto("Interstellar", LocalDate.of(2014, 11, 7), 8.6);
    }

    private static Set<String> messages(Set<ConstraintViolation<UpdateMovieDto>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
}
