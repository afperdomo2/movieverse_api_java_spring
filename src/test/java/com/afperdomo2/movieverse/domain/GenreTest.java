package com.afperdomo2.movieverse.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenreTest {

    @Test
    @DisplayName("Convierte la etiqueta al enum correspondiente")
    void fromLabel_returnsGenre() {
        assertThat(Genre.fromLabel("Drama")).isEqualTo(Genre.DRAMA);
        assertThat(Genre.fromLabel("sci-fi")).isEqualTo(Genre.SCI_FI);
        assertThat(Genre.fromLabel("  Animation  ")).isEqualTo(Genre.ANIMATION);
    }

    @Test
    @DisplayName("Devuelve null cuando la etiqueta es null")
    void fromLabel_returnsNull_whenValueIsNull() {
        assertThat(Genre.fromLabel(null)).isNull();
    }

    @Test
    @DisplayName("Rechaza una etiqueta desconocida")
    void fromLabel_throwsException_whenValueIsUnknown() {
        assertThatThrownBy(() -> Genre.fromLabel("Acción"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Acción");
    }

    @Test
    @DisplayName("Expone la etiqueta legible")
    void label_returnsReadableValue() {
        assertThat(Genre.SCI_FI.label()).isEqualTo("Sci-Fi");
        assertThat(Genre.DOCUMENTARY.label()).isEqualTo("Documentary");
    }
}
