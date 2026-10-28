package com.afperdomo2.movieverse.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.afperdomo2.movieverse.domain.dto.AuthorDetailDto;
import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.domain.dto.MovieSummaryDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(AuthorEntity entity);

    List<AuthorDto> toDto(Iterable<AuthorEntity> entities);

    default AuthorDetailDto toDetailDto(AuthorEntity entity, List<MovieSummaryDto> movies) {
        if (entity == null) {
            return null;
        }
        return new AuthorDetailDto(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getNationality(),
                entity.getBirthDate(),
                entity.getBiography(),
                entity.isActive(),
                movies == null ? List.of() : movies);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void createEntityFromDto(CreateAuthorDto createAuthorDto, @MappingTarget AuthorEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateAuthorDto updateAuthorDto, @MappingTarget AuthorEntity entity);
}
