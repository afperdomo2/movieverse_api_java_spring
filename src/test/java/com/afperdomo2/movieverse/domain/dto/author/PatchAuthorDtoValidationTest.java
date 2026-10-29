package com.afperdomo2.movieverse.domain.dto.author;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class PatchAuthorDtoValidationTest {

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
        PatchAuthorDto dto = emptyPatch();

        assertThat(messages(validator.validate(dto)))
                .containsExactly("Debes enviar al menos un campo para actualizar");
    }

    @Test
    @DisplayName("Los campos nulos se normalizan a vacío y el PATCH sigue siendo inválido")
    void nullFields_areNormalizedToEmpty() {
        PatchAuthorDto dto = new PatchAuthorDto(null, null, null, null, null, null);

        assertThat(dto.name()).isEmpty();
        assertThat(dto.lastName()).isEmpty();
        assertThat(dto.nationality()).isEmpty();
        assertThat(dto.birthDate()).isEmpty();
        assertThat(dto.biography()).isEmpty();
        assertThat(dto.active()).isEmpty();
        assertThat(messages(validator.validate(dto)))
                .containsExactly("Debes enviar al menos un campo para actualizar");
    }

    @Test
    @DisplayName("Un PATCH solo con nombre válido es válido")
    void nameOnly_isValid() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.of("Christopher"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("El nombre en blanco es inválido")
    void blankName_isRejected() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.of("   "), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(messages(validator.validate(dto))).contains("El nombre es obligatorio");
    }

    @Test
    @DisplayName("El apellido en blanco es inválido")
    void blankLastName_isRejected() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.empty(), Optional.of("   "),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(messages(validator.validate(dto))).contains("El apellido es obligatorio");
    }

    @Test
    @DisplayName("El nombre no puede superar los 100 caracteres")
    void longName_isRejected() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.of("a".repeat(101)), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(messages(validator.validate(dto))).containsExactly("El nombre no puede superar los 100 caracteres");
    }

    @Test
    @DisplayName("La fecha de nacimiento futura es inválida")
    void futureBirthDate_isRejected() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(LocalDate.now().plusDays(1)), Optional.empty());

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La fecha de nacimiento no puede ser mayor a la fecha actual");
    }

    @Test
    @DisplayName("La biografía no puede superar los 1000 caracteres")
    void longBiography_isRejected() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.of("a".repeat(1001)));

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La biografía no puede superar los 1000 caracteres");
    }

    @Test
    @DisplayName("Un PATCH solo con isActive es válido")
    void activeOnly_isValid() {
        PatchAuthorDto dto = new PatchAuthorDto(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false));

        assertThat(dto.active()).contains(false);
        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("Un PATCH con todos los campos válidos es válido")
    void allFields_isValid() {
        PatchAuthorDto dto = new PatchAuthorDto(
                Optional.of("Christopher"),
                Optional.of("Nolan"),
                Optional.of("British-American"),
                Optional.of(LocalDate.of(1970, 7, 30)),
                Optional.of("Director."),
                Optional.of(true));

        assertThat(validator.validate(dto)).isEmpty();
    }

    private static PatchAuthorDto emptyPatch() {
        return new PatchAuthorDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
    }

    private static Set<String> messages(Set<ConstraintViolation<PatchAuthorDto>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
}
