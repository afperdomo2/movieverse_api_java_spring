package com.afperdomo2.movieverse.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;

import org.springframework.ai.tool.annotation.Tool;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Tool(description = "Busca todas las películas que están dentro de la plataforma")
    public List<MovieDto> findAll() {
        return this.movieRepository.findAll();
    }

    public PageResponse<MovieDto> findPage(int page, int limit) {
        return this.movieRepository.findPage(page, limit);
    }

    public MovieDto findById(UUID id) {
        return this.movieRepository.findById(id);
    }

    public MovieDto create(CreateMovieDto movieDto) {
        return this.movieRepository.create(movieDto);
    }

    public MovieDto update(UUID id, UpdateMovieDto changes) {
        return this.movieRepository.update(id, changes);
    }

    public MovieDto patch(UUID id, PatchMovieDto changes) {
        return this.movieRepository.patch(id, changes);
    }

    public boolean delete(UUID id) {
        return this.movieRepository.delete(id);
    }
}
