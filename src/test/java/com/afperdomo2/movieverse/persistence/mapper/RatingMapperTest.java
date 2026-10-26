package com.afperdomo2.movieverse.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.afperdomo2.movieverse.domain.RatingString;

class RatingMapperTest {

    @Test
    @DisplayName("Devuelve null cuando la calificación es null")
    void bigDecimalToRatingString_returnsNull_whenRatingIsNull() {
        assertThat(RatingMapper.bigDecimalToRatingString(null)).isNull();
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "0.0,   VERY_BAD",
            "1.0,   VERY_BAD",
            "1.99,  VERY_BAD",
            "2.0,   BAD",
            "3.0,   BAD",
            "3.99,  BAD",
            "4.0,   AVERAGE",
            "5.0,   AVERAGE",
            "5.99,  AVERAGE",
            "6.0,   GOOD",
            "7.0,   GOOD",
            "7.99,  GOOD",
            "8.0,   VERY_GOOD",
            "9.0,   VERY_GOOD",
            "9.99,  VERY_GOOD",
            "10.0,  VERY_GOOD"
    })
    @DisplayName("Clasifica la calificación en la categoría correcta")
    void bigDecimalToRatingString_mapsValueToCategory(String rating, RatingString expected) {
        assertThat(RatingMapper.bigDecimalToRatingString(new BigDecimal(rating))).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} cae en {1}")
    @CsvSource({
            "1.99,  VERY_BAD",
            "2.0,   BAD",
            "3.99,  BAD",
            "4.0,   AVERAGE",
            "5.99,  AVERAGE",
            "6.0,   GOOD",
            "7.99,  GOOD",
            "8.0,   VERY_GOOD"
    })
    @DisplayName("Respeta los límites entre categorías")
    void bigDecimalToRatingString_respectsCategoryBoundaries(String rating, RatingString expected) {
        assertThat(RatingMapper.bigDecimalToRatingString(new BigDecimal(rating))).isEqualTo(expected);
    }

    @Test
    @DisplayName("Una calificación negativa se clasifica como VERY_BAD")
    void bigDecimalToRatingString_returnsVeryBad_whenRatingIsNegative() {
        assertThat(RatingMapper.bigDecimalToRatingString(new BigDecimal("-1.0"))).isEqualTo(RatingString.VERY_BAD);
    }

    @Test
    @DisplayName("Un valor fuera del rango esperado cae en VERY_BAD por la rama default")
    void bigDecimalToRatingString_returnsVeryBad_whenRatingIsOutOfExpectedRange() {
        assertThat(RatingMapper.bigDecimalToRatingString(new BigDecimal("15.0"))).isEqualTo(RatingString.VERY_BAD);
    }
}
