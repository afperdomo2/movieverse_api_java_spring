package com.afperdomo2.movieverse.domain.repository;

import java.util.List;
import java.util.UUID;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;

public interface MovieRepository {
    List<MovieDto> findAll();

    PageResponse<MovieDto> findPage(int page, int limit);

    MovieDto findById(UUID id);

    MovieDto create(CreateMovieDto movieDto);

    MovieDto update(UUID id, UpdateMovieDto changes);

    MovieDto patch(UUID id, PatchMovieDto changes);

    boolean delete(UUID id);
}
