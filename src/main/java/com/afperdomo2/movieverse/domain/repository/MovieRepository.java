package com.afperdomo2.movieverse.domain.repository;

import java.util.List;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;

public interface MovieRepository {
    List<MovieDto> findAll();

    MovieDto findById(long id);

    MovieDto create(CreateMovieDto movieDto);

    MovieDto update(long id, UpdateMovieDto changes);

    MovieDto patch(long id, PatchMovieDto changes);

    boolean delete(long id);
}
