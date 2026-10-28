package com.afperdomo2.movieverse.common.uuid;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UuidV7GeneratorTest {

    @Test
    @DisplayName("generate devuelve un UUID v7 con variante RFC 9562")
    void generate_returnsUuidV7() {
        UUID uuid = UuidV7Generator.generate();

        assertThat(uuid).isNotNull();
        assertThat(uuid.version()).isEqualTo(7);
        assertThat(uuid.variant()).isEqualTo(2);
    }

    @Test
    @DisplayName("generate devuelve valores únicos")
    void generate_returnsUniqueValues() {
        Set<UUID> generated = new HashSet<>(IntStream.range(0, 1000)
                .mapToObj(ignored -> UuidV7Generator.generate())
                .toList());

        assertThat(generated).hasSize(1000);
    }

    @Test
    @DisplayName("generate mantiene el orden cronológico")
    void generate_isTimeOrdered() {
        List<UUID> generated = IntStream.range(0, 100)
                .mapToObj(ignored -> UuidV7Generator.generate())
                .toList();

        for (int i = 1; i < generated.size(); i++) {
            assertThat(generated.get(i).toString())
                    .isGreaterThan(generated.get(i - 1).toString());
        }
    }
}
