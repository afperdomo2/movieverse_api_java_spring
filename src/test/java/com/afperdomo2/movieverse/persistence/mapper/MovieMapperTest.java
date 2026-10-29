package com.afperdomo2.movieverse.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.domain.dto.author.AuthorSummaryDto;
import com.afperdomo2.movieverse.domain.dto.movie.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.movie.MovieDto;
import com.afperdomo2.movieverse.domain.dto.movie.MovieSummaryDto;
import com.afperdomo2.movieverse.domain.dto.movie.UpdateMovieDto;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

class MovieMapperTest {

    private static final LocalDate RELEASE_DATE = LocalDate.of(2014, 11, 7);
    private static final UUID MOVIE_ID = UUID.fromString("019b76da-d2f8-700c-8004-00000c08d6b4");
    private static final UUID OTHER_MOVIE_ID = UUID.fromString("019b76da-bb88-7006-8002-000006046b5a");

    private final MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    @Test
    @DisplayName("toDto mapea todos los campos, incluida la categoría de calificación")
    void toDto_mapsAllFields() {
        MovieEntity entity = movieEntity();

        MovieDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(MOVIE_ID);
        assertThat(dto.title()).isEqualTo("Interstellar");
        assertThat(dto.duration()).isEqualTo(169);
        assertThat(dto.genre()).isEqualTo(Genre.SCI_FI);
        assertThat(dto.releaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(dto.active()).isTrue();
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
        List<MovieDto> dtos = mapper.toDto(List.of(movieEntity(), movieEntity(OTHER_MOVIE_ID, "Inception")));

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(MovieDto::title).containsExactly("Interstellar", "Inception");
    }

    @Test
    @DisplayName("toEntity mapea todos los campos del DTO")
    void toEntity_mapsAllFields() {
        MovieDto dto = new MovieDto(MOVIE_ID, "Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, true, 8.6, "VERY_GOOD",
                List.of());

        MovieEntity entity = mapper.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(MOVIE_ID);
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.getDuration()).isEqualTo(169);
        assertThat(entity.getGenre()).isEqualTo(Genre.SCI_FI);
        assertThat(entity.getReleaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(entity.isActive()).isTrue();
        assertThat(entity.getRating()).isEqualByComparingTo("8.6");
    }

    @Test
    @DisplayName("toEntity no establece los campos de auditoría ni la versión")
    void toEntity_doesNotSetAuditOrVersionFields() {
        MovieDto dto = new MovieDto(MOVIE_ID, "Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, true, 8.6, "VERY_GOOD",
                List.of());

        MovieEntity entity = mapper.toEntity(dto);

        assertThat(entity.getVersion()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("createEntityFromDto mapea los campos de creación y no asigna id ni estado")
    void createEntityFromDto_mapsCreateFields() {
        CreateMovieDto dto = new CreateMovieDto("Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, 8.6);
        MovieEntity entity = new MovieEntity();

        mapper.createEntityFromDto(dto, entity);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.getDuration()).isEqualTo(169);
        assertThat(entity.getGenre()).isEqualTo(Genre.SCI_FI);
        assertThat(entity.getReleaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(entity.getRating()).isEqualByComparingTo("8.6");
        assertThat(entity.isActive()).isTrue();
    }

    @Test
    @DisplayName("createEntityFromDto deja la calificación en null cuando el DTO la envía null")
    void createEntityFromDto_setsNullRating() {
        CreateMovieDto dto = new CreateMovieDto("Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, null);
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
                new CreateMovieDto("Interstellar", 169, Genre.SCI_FI, RELEASE_DATE, 8.6), entity);

        assertThat(entity.getId()).isEqualTo(MOVIE_ID);
        assertThat(entity.isActive()).isTrue();
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
        assertThat(entity.getGenre()).isEqualTo(Genre.SCI_FI);
        assertThat(entity.isActive()).isTrue();
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

    @Test
    @DisplayName("toDto mapea los autores ordenados por apellido y nombre")
    void toDto_mapsAuthorsSorted() {
        MovieEntity entity = movieEntity();
        entity.setAuthors(Set.of(
                authorEntity("019b7c01-0bd0-7003-a001-0000030235ad", "Christopher", "Nolan"),
                authorEntity("019b7c01-0400-7001-8aab-00000100bc8f", "Frank", "Darabont")));

        MovieDto dto = mapper.toDto(entity);

        assertThat(dto.authors()).extracting(AuthorSummaryDto::lastName)
                .containsExactly("Darabont", "Nolan");
    }

    @Test
    @DisplayName("toDto devuelve una lista vacía cuando la película no tiene autores")
    void toDto_returnsEmptyAuthors_whenNoAuthors() {
        MovieDto dto = mapper.toDto(movieEntity());

        assertThat(dto.authors()).isEmpty();
    }

    @Test
    @DisplayName("toSummaryDto mapea los campos básicos y ratingString")
    void toSummaryDto_mapsFields() {
        MovieSummaryDto summary = mapper.toSummaryDto(movieEntity());

        assertThat(summary.id()).isEqualTo(MOVIE_ID);
        assertThat(summary.title()).isEqualTo("Interstellar");
        assertThat(summary.genre()).isEqualTo(Genre.SCI_FI);
        assertThat(summary.releaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(summary.active()).isTrue();
        assertThat(summary.rating()).isEqualTo(8.6);
        assertThat(summary.ratingString()).isEqualTo("VERY_GOOD");
    }

    private static AuthorEntity authorEntity(String id, String name, String lastName) {
        AuthorEntity author = new AuthorEntity();
        author.setId(UUID.fromString(id));
        author.setName(name);
        author.setLastName(lastName);
        return author;
    }

    private static MovieEntity movieEntity() {
        return movieEntity(MOVIE_ID, "Interstellar");
    }

    private static MovieEntity movieEntity(UUID id, String title) {
        MovieEntity entity = new MovieEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setDuration(169);
        entity.setGenre(Genre.SCI_FI);
        entity.setReleaseDate(RELEASE_DATE);
        entity.setRating(new BigDecimal("8.6"));
        entity.setActive(true);
        return entity;
    }
}
