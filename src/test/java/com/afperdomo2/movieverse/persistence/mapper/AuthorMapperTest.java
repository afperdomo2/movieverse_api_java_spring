package com.afperdomo2.movieverse.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;

class AuthorMapperTest {

    private static final LocalDate BIRTH_DATE = LocalDate.of(1970, 7, 30);
    private static final UUID AUTHOR_ID = UUID.fromString("019b7c01-0bd0-7003-a001-0000030235ad");

    private final AuthorMapper mapper = Mappers.getMapper(AuthorMapper.class);

    @Test
    @DisplayName("toDto mapea todos los campos")
    void toDto_mapsAllFields() {
        AuthorEntity entity = authorEntity();

        AuthorDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(AUTHOR_ID);
        assertThat(dto.name()).isEqualTo("Christopher");
        assertThat(dto.lastName()).isEqualTo("Nolan");
        assertThat(dto.nationality()).isEqualTo("British-American");
        assertThat(dto.birthDate()).isEqualTo(BIRTH_DATE);
        assertThat(dto.biography()).isEqualTo("Director.");
        assertThat(dto.active()).isTrue();
    }

    @Test
    @DisplayName("toDto devuelve null para una entidad null")
    void toDto_returnsNull_whenEntityIsNull() {
        assertThat(mapper.toDto((AuthorEntity) null)).isNull();
    }

    @Test
    @DisplayName("toDto mapea una colección de entidades")
    void toDto_mapsEntityCollection() {
        List<AuthorDto> dtos = mapper.toDto(List.of(authorEntity(), authorEntity()));

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(AuthorDto::name).containsExactly("Christopher", "Christopher");
    }

    @Test
    @DisplayName("createEntityFromDto mapea los campos y no asigna id, estado ni auditoría")
    void createEntityFromDto_mapsCreateFields() {
        CreateAuthorDto dto = new CreateAuthorDto("Christopher", "Nolan", "British-American",
                BIRTH_DATE, "Director.");
        AuthorEntity entity = new AuthorEntity();

        mapper.createEntityFromDto(dto, entity);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("Christopher");
        assertThat(entity.getLastName()).isEqualTo("Nolan");
        assertThat(entity.getNationality()).isEqualTo("British-American");
        assertThat(entity.getBirthDate()).isEqualTo(BIRTH_DATE);
        assertThat(entity.getBiography()).isEqualTo("Director.");
        assertThat(entity.isActive()).isTrue();
        assertThat(entity.getVersion()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("createEntityFromDto preserva id, estado, auditoría y versión de una entidad existente")
    void createEntityFromDto_preservesExistingEntityFields() {
        AuthorEntity entity = authorEntity();
        Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-02T10:00:00Z");
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setVersion(3L);

        mapper.createEntityFromDto(
                new CreateAuthorDto("Christopher", "Nolan", "British", BIRTH_DATE, "Director."), entity);

        assertThat(entity.getId()).isEqualTo(AUTHOR_ID);
        assertThat(entity.isActive()).isTrue();
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(entity.getVersion()).isEqualTo(3L);
    }

    @Test
    @DisplayName("updateEntityFromDto actualiza los campos editables y conserva id, estado y auditoría")
    void updateEntityFromDto_updatesEditableFields() {
        AuthorEntity entity = authorEntity();
        UpdateAuthorDto changes = new UpdateAuthorDto("Frank", "Darabont", "American",
                LocalDate.of(1959, 1, 28), "Director y guionista.");

        mapper.updateEntityFromDto(changes, entity);

        assertThat(entity.getName()).isEqualTo("Frank");
        assertThat(entity.getLastName()).isEqualTo("Darabont");
        assertThat(entity.getNationality()).isEqualTo("American");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(1959, 1, 28));
        assertThat(entity.getBiography()).isEqualTo("Director y guionista.");
        assertThat(entity.getId()).isEqualTo(AUTHOR_ID);
        assertThat(entity.isActive()).isTrue();
    }

    private static AuthorEntity authorEntity() {
        AuthorEntity entity = new AuthorEntity();
        entity.setId(AUTHOR_ID);
        entity.setName("Christopher");
        entity.setLastName("Nolan");
        entity.setNationality("British-American");
        entity.setBirthDate(BIRTH_DATE);
        entity.setBiography("Director.");
        entity.setActive(true);
        return entity;
    }
}
