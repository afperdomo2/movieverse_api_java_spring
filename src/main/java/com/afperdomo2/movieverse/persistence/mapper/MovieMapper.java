package com.afperdomo2.movieverse.persistence.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    MovieEntity toEntity(MovieDto dto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "rating", source = "rating")
    void updateEntityFromDto(UpdateMovieDto updateMovieDto, @MappingTarget MovieEntity entity);
}
