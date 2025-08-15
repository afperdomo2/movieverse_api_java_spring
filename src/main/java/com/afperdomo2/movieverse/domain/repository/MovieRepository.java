package com.afperdomo2.movieverse.domain.repository;

import java.util.List;

import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;

public interface MovieRepository {
    List<MovieDto> findAll();

    MovieDto findById(long id);

    MovieDto create(MovieDto movieDto);

    MovieDto update(long id, UpdateMovieDto changes);
}
