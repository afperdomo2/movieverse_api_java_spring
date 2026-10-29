package com.afperdomo2.movieverse.domain.dto.author;

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

class CreateAuthorDtoValidationTest {

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

    @Test
    @DisplayName("Los campos opcionales pueden ser null")
    void optionalFieldsCanBeNull() {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", "Nolan", null, null, null);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   " })
    @DisplayName("El nombre nulo, vacío o en blanco es inválido")
    void blankName_isRejected(String name) {
        CreateAuthorDto dto = new CreateAuthorDto(name, "Nolan", null, null, null);

        assertThat(messages(validator.validate(dto))).containsExactly("El nombre es obligatorio");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   " })
    @DisplayName("El apellido nulo, vacío o en blanco es inválido")
    void blankLastName_isRejected(String lastName) {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", lastName, null, null, null);

        assertThat(messages(validator.validate(dto))).containsExactly("El apellido es obligatorio");
    }

    @Test
    @DisplayName("El nombre no puede superar los 100 caracteres")
    void longName_isRejected() {
        CreateAuthorDto dto = new CreateAuthorDto("a".repeat(101), "Nolan", null, null, null);

        assertThat(messages(validator.validate(dto))).containsExactly("El nombre no puede superar los 100 caracteres");
    }

    @Test
    @DisplayName("El apellido no puede superar los 100 caracteres")
    void longLastName_isRejected() {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", "a".repeat(101), null, null, null);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("El apellido no puede superar los 100 caracteres");
    }

    @Test
    @DisplayName("La nacionalidad no puede superar los 100 caracteres")
    void longNationality_isRejected() {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", "Nolan", "a".repeat(101), null, null);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La nacionalidad no puede superar los 100 caracteres");
    }

    @Test
    @DisplayName("La biografía no puede superar los 1000 caracteres")
    void longBiography_isRejected() {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", "Nolan", null, null, "a".repeat(1001));

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La biografía no puede superar los 1000 caracteres");
    }

    @Test
    @DisplayName("La fecha de nacimiento futura es inválida")
    void futureBirthDate_isRejected() {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", "Nolan", null,
                LocalDate.now().plusDays(1), null);

        assertThat(messages(validator.validate(dto)))
                .containsExactly("La fecha de nacimiento no puede ser mayor a la fecha actual");
    }

    private static CreateAuthorDto validDto() {
        return new CreateAuthorDto("Christopher", "Nolan", "British-American",
                LocalDate.of(1970, 7, 30), "Director.");
    }

    private static Set<String> messages(Set<ConstraintViolation<CreateAuthorDto>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
}
