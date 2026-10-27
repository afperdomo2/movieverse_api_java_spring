package com.afperdomo2.movieverse.persistence.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
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
    MovieDto toDto(MovieEntity entity);

    List<MovieDto> toDto(Iterable<MovieEntity> entities);

    @InheritInverseConfiguration
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
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateMovieDto updateMovieDto, @MappingTarget MovieEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void createEntityFromDto(CreateMovieDto createMovieDto, @MappingTarget MovieEntity entity);
}
