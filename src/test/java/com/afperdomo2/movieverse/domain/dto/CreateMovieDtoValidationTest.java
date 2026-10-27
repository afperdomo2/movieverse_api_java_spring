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

class CreateMovieDtoValidationTest {

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
        CreateMovieDto dto = copyWithTitle(validDto(), title);

        assertThat(messages(validator.validate(dto))).containsExactly("El título es obligatorio");
    }

    @Test
    @DisplayName("El título no puede superar los 255 caracteres")
    void longTitle_isRejected() {
        CreateMovieDto dto = copyWithTitle(validDto(), "a".repeat(256));

        assertThat(messages(validator.validate(dto)))
                .containsExactly("El título no puede superar los 255 caracteres");
    }

    @Test
    @DisplayName("Un título de 255 caracteres es válido")
    void titleAtMaximumLength_isAccepted() {
        CreateMovieDto dto = copyWithTitle(validDto(), "a".repeat(255));

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("La duración nula es inválida")
    void nullDuration_isRejected() {
        CreateMovieDto dto = copyWithDuration(validDto(), null);

        assertThat(messages(validator.validate(dto))).containsExactly("La duración es obligatoria");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -5 })
    @DisplayName("La duración debe ser mayor a cero")
    void nonPositiveDuration_isRejected(int duration) {
        CreateMovieDto dto = copyWithDuration(validDto(), duration);

        assertThat(messages(validator.validate(dto))).containsExactly("La duración debe ser mayor a 0 minutos");
    }

    @Test
    @DisplayName("La duración no puede superar los 600 minutos")
    void longDuration_isRejected() {
        CreateMovieDto dto = copyWithDuration(validDto(), 601);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La duración no puede ser mayor a 600 minutos");
    }

    @Test
    @DisplayName("Una duración de 600 minutos es válida")
    void durationAtMaximum_isAccepted() {
        CreateMovieDto dto = copyWithDuration(validDto(), 600);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("El género nulo es inválido")
    void nullGenre_isRejected() {
        CreateMovieDto dto = new CreateMovieDto("Interstellar", 169, null,
                LocalDate.of(2014, 11, 7), 8.6);

        assertThat(messages(validator.validate(dto))).containsExactly("El género es obligatorio");
    }

    @Test
    @DisplayName("El género en blanco es inválido")
    void blankGenre_isRejected() {
        CreateMovieDto dto = copyWithGenre(validDto(), "   ");

        assertThat(messages(validator.validate(dto))).containsExactly("El género es obligatorio");
    }

    @Test
    @DisplayName("El género no puede superar los 50 caracteres")
    void longGenre_isRejected() {
        CreateMovieDto dto = copyWithGenre(validDto(), "a".repeat(51));

        assertThat(messages(validator.validate(dto)))
                .containsExactly("El género no puede superar los 50 caracteres");
    }

    @Test
    @DisplayName("Un género de 50 caracteres es válido")
    void genreAtMaximumLength_isAccepted() {
        CreateMovieDto dto = copyWithGenre(validDto(), "a".repeat(50));

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("La fecha de lanzamiento nula es inválida")
    void nullReleaseDate_isRejected() {
        CreateMovieDto dto = copyWithReleaseDate(validDto(), null);

        assertThat(messages(validator.validate(dto))).containsExactly("La fecha de lanzamiento es obligatoria");
    }

    @Test
    @DisplayName("La fecha de lanzamiento futura es inválida")
    void futureReleaseDate_isRejected() {
        CreateMovieDto dto = copyWithReleaseDate(validDto(), LocalDate.now().plusDays(1));

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La fecha de lanzamiento no puede ser mayor a la fecha actual");
    }

    @Test
    @DisplayName("La calificación nula es inválida")
    void nullRating_isRejected() {
        CreateMovieDto dto = copyWithRating(validDto(), null);

        assertThat(messages(validator.validate(dto))).containsExactly("La calificación es obligatoria");
    }

    @ParameterizedTest
    @ValueSource(doubles = { -0.1, 9.5 })
    @DisplayName("La calificación debe estar entre 0 y 9")
    void outOfRangeRating_isRejected(double rating) {
        CreateMovieDto dto = copyWithRating(validDto(), rating);

        assertThat(messages(validator.validate(dto))).hasSize(1);
        assertThat(messages(validator.validate(dto)).iterator().next())
                .contains("La calificación debe ser");
    }

    @ParameterizedTest
    @ValueSource(doubles = { 0.0, 9.0 })
    @DisplayName("Los límites 0 y 9 son válidos")
    void boundaryRating_isAccepted(double rating) {
        assertThat(validator.validate(copyWithRating(validDto(), rating))).isEmpty();
    }

    private static CreateMovieDto validDto() {
        return new CreateMovieDto("Interstellar", 169, "Sci-Fi", LocalDate.of(2014, 11, 7), 8.6);
    }

    private static CreateMovieDto copyWithTitle(CreateMovieDto dto, String title) {
        return new CreateMovieDto(title, dto.duration(), dto.genre(), dto.releaseDate(), dto.rating());
    }

    private static CreateMovieDto copyWithDuration(CreateMovieDto dto, Integer duration) {
        return new CreateMovieDto(dto.title(), duration, dto.genre(), dto.releaseDate(), dto.rating());
    }

    private static CreateMovieDto copyWithGenre(CreateMovieDto dto, String genre) {
        return new CreateMovieDto(dto.title(), dto.duration(), genre, dto.releaseDate(), dto.rating());
    }

    private static CreateMovieDto copyWithReleaseDate(CreateMovieDto dto, LocalDate releaseDate) {
        return new CreateMovieDto(dto.title(), dto.duration(), dto.genre(), releaseDate, dto.rating());
    }

    private static CreateMovieDto copyWithRating(CreateMovieDto dto, Double rating) {
        return new CreateMovieDto(dto.title(), dto.duration(), dto.genre(), dto.releaseDate(), rating);
    }

    private static Set<String> messages(Set<ConstraintViolation<CreateMovieDto>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
}
