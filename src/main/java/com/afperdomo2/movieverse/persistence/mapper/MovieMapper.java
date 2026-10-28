package com.afperdomo2.movieverse.persistence.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.afperdomo2.movieverse.domain.dto.AuthorSummaryDto;
import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

@Mapper(componentModel = "spring", uses = { RatingMapper.class })
public interface MovieMapper {
    // NOTE: Si los campos se llaman igual, se pueden omitir
    // @Mapping(source = "id", target = "id")
    // @Mapping(source = "title", target = "title")
    // @Mapping(source = "duration", target = "duration")
    // @Mapping(source = "genre", target = "genre")
    // @Mapping(source = "releaseDate", target = "releaseDate")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "rating", target = "ratingString", qualifiedByName = "bigDecimalToRatingString")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "toAuthorSummaries")
    MovieDto toDto(MovieEntity entity);

    List<MovieDto> toDto(Iterable<MovieEntity> entities);

    @InheritInverseConfiguration
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MovieEntity toEntity(MovieDto dto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateMovieDto updateMovieDto, @MappingTarget MovieEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void createEntityFromDto(CreateMovieDto createMovieDto, @MappingTarget MovieEntity entity);

    @Named("toAuthorSummaries")
    default List<AuthorSummaryDto> toAuthorSummaries(Set<AuthorEntity> authors) {
        if (authors == null) {
            return List.of();
        }
        return authors.stream()
                .sorted(Comparator.comparing(AuthorEntity::getLastName)
                        .thenComparing(AuthorEntity::getName))
                .map(author -> new AuthorSummaryDto(
                        author.getId(),
                        author.getName(),
                        author.getLastName()))
                .toList();
    }
}
