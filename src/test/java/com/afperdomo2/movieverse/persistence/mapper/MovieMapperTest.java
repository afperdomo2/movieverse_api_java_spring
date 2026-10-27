package com.afperdomo2.movieverse.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

class MovieMapperTest {

    private static final LocalDate RELEASE_DATE = LocalDate.of(2014, 11, 7);

    private final MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    @Test
    @DisplayName("toDto mapea todos los campos, incluida la categoría de calificación")
    void toDto_mapsAllFields() {
        MovieEntity entity = movieEntity();

        MovieDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("Interstellar");
        assertThat(dto.duration()).isEqualTo(169);
        assertThat(dto.genre()).isEqualTo("Sci-Fi");
        assertThat(dto.releaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(dto.status()).isEqualTo("D");
        assertThat(dto.rating()).isEqualTo(8.6);
        assertThat(dto.ratingString()).isEqualTo("VERY_GOOD");
    }

    @Test
    @DisplayName("toDto deja ratingString en null cuando no hay calificación")
    void toDto_returnsNullRatingString_whenRatingIsNull() {
        MovieEntity entity = movieEntity();
        entity.setRating(null);

        MovieDto dto = mapper.toDto(entity);

        assertThat(dto.rating()).isNull();
        assertThat(dto.ratingString()).isNull();
    }

    @Test
    @DisplayName("toDto devuelve null para una entidad null")
    void toDto_returnsNull_whenEntityIsNull() {
        assertThat(mapper.toDto((MovieEntity) null)).isNull();
    }

    @Test
    @DisplayName("toDto mapea una colección de entidades")
    void toDto_mapsEntityCollection() {
        List<MovieDto> dtos = mapper.toDto(List.of(movieEntity(), movieEntity(2L, "Inception")));

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(MovieDto::title).containsExactly("Interstellar", "Inception");
    }

    @Test
    @DisplayName("toEntity mapea todos los campos del DTO")
    void toEntity_mapsAllFields() {
        MovieDto dto = new MovieDto(1L, "Interstellar", 169, "Sci-Fi", RELEASE_DATE, "D", 8.6, "VERY_GOOD");

        MovieEntity entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.getDuration()).isEqualTo(169);
        assertThat(entity.getGenre()).isEqualTo("Sci-Fi");
        assertThat(entity.getReleaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(entity.getStatus()).isEqualTo("D");
        assertThat(entity.getRating()).isEqualByComparingTo("8.6");
    }

    @Test
    @DisplayName("toEntity no establece los campos de auditoría ni la versión")
    void toEntity_doesNotSetAuditOrVersionFields() {
        MovieDto dto = new MovieDto(1L, "Interstellar", 169, "Sci-Fi", RELEASE_DATE, "D", 8.6, "VERY_GOOD");

        MovieEntity entity = mapper.toEntity(dto);

        assertThat(entity.getVersion()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("createEntityFromDto mapea los campos de creación y no asigna id ni estado")
    void createEntityFromDto_mapsCreateFields() {
        CreateMovieDto dto = new CreateMovieDto("Interstellar", 169, "Sci-Fi", RELEASE_DATE, 8.6);
        MovieEntity entity = new MovieEntity();

        mapper.createEntityFromDto(dto, entity);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.getDuration()).isEqualTo(169);
        assertThat(entity.getGenre()).isEqualTo("Sci-Fi");
        assertThat(entity.getReleaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(entity.getRating()).isEqualByComparingTo("8.6");
        assertThat(entity.getStatus()).isNull();
    }

    @Test
    @DisplayName("createEntityFromDto deja la calificación en null cuando el DTO la envía null")
    void createEntityFromDto_setsNullRating() {
        CreateMovieDto dto = new CreateMovieDto("Interstellar", 169, "Sci-Fi", RELEASE_DATE, null);
        MovieEntity entity = movieEntity();

        mapper.createEntityFromDto(dto, entity);

        assertThat(entity.getRating()).isNull();
    }

    @Test
    @DisplayName("createEntityFromDto preserva id, estado, auditoría y versión de una entidad existente")
    void createEntityFromDto_preservesExistingEntityFields() {
        MovieEntity entity = movieEntity();
        Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-02T10:00:00Z");
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setVersion(3L);

        mapper.createEntityFromDto(
                new CreateMovieDto("Interstellar", 169, "Sci-Fi", RELEASE_DATE, 8.6), entity);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getStatus()).isEqualTo("D");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(entity.getVersion()).isEqualTo(3L);
    }

    @Test
    @DisplayName("updateEntityFromDto solo actualiza título, fecha y calificación")
    void updateEntityFromDto_updatesOnlyMappedFields() {
        MovieEntity entity = movieEntity();
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar Remastered", LocalDate.of(2014, 11, 5), 9.0);

        mapper.updateEntityFromDto(changes, entity);

        assertThat(entity.getTitle()).isEqualTo("Interstellar Remastered");
        assertThat(entity.getReleaseDate()).isEqualTo(LocalDate.of(2014, 11, 5));
        assertThat(entity.getRating()).isEqualByComparingTo("9.0");
        assertThat(entity.getDuration()).isEqualTo(169);
        assertThat(entity.getGenre()).isEqualTo("Sci-Fi");
        assertThat(entity.getStatus()).isEqualTo("D");
    }

    @Test
    @DisplayName("updateEntityFromDto borra la calificación cuando el DTO la envía null")
    void updateEntityFromDto_clearsRating_whenRatingIsNull() {
        MovieEntity entity = movieEntity();

        mapper.updateEntityFromDto(new UpdateMovieDto("Interstellar", RELEASE_DATE, null), entity);

        assertThat(entity.getRating()).isNull();
    }

    @Test
    @DisplayName("updateEntityFromDto preserva la auditoría y la versión de una entidad existente")
    void updateEntityFromDto_preservesAuditAndVersionFields() {
        MovieEntity entity = movieEntity();
        Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-02T10:00:00Z");
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setVersion(3L);

        mapper.updateEntityFromDto(new UpdateMovieDto("Interstellar Remastered", RELEASE_DATE, 9.0), entity);

        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(entity.getVersion()).isEqualTo(3L);
    }

    private static MovieEntity movieEntity() {
        return movieEntity(1L, "Interstellar");
    }

    private static MovieEntity movieEntity(Long id, String title) {
        MovieEntity entity = new MovieEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setDuration(169);
        entity.setGenre("Sci-Fi");
        entity.setReleaseDate(RELEASE_DATE);
        entity.setRating(new BigDecimal("8.6"));
        entity.setStatus("D");
        return entity;
    }
}
