package com.afperdomo2.movieverse.domain.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
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

class PageQueryValidationTest {

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
    @DisplayName("Los valores por defecto son page=1 y limit=10")
    void defaults_areValid() {
        PageQuery query = new PageQuery();

        assertThat(query.getPage()).isEqualTo(1);
        assertThat(query.getLimit()).isEqualTo(10);
        assertThat(validator.validate(query)).isEmpty();
    }

    @Test
    @DisplayName("Una página y un límite personalizados válidos no producen violaciones")
    void customValues_areValid() {
        PageQuery query = query(2, 50);

        assertThat(validator.validate(query)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    @DisplayName("page debe ser mayor o igual a 1")
    void invalidPage_isRejected(int page) {
        PageQuery query = query(page, 10);

        assertThat(messages(validator.validate(query))).containsExactly("page debe ser mayor o igual a 1");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    @DisplayName("limit debe ser mayor o igual a 1")
    void nonPositiveLimit_isRejected(int limit) {
        PageQuery query = query(1, limit);

        assertThat(messages(validator.validate(query))).containsExactly("limit debe ser mayor o igual a 1");
    }

    @Test
    @DisplayName("limit no puede ser mayor a 100")
    void limitAboveMaximum_isRejected() {
        PageQuery query = query(1, 101);

        assertThat(messages(validator.validate(query))).containsExactly("limit debe ser menor o igual a 100");
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 100 })
    @DisplayName("Los límites 1 y 100 son válidos")
    void limitBoundaries_areValid(int limit) {
        PageQuery query = query(1, limit);

        assertThat(validator.validate(query)).isEmpty();
    }

    private static PageQuery query(int page, int limit) {
        PageQuery query = new PageQuery();
        query.setPage(page);
        query.setLimit(limit);
        return query;
    }

    private static Set<String> messages(Set<ConstraintViolation<PageQuery>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }
}
