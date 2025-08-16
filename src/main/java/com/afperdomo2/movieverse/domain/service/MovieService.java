package com.afperdomo2.movieverse.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;

import dev.langchain4j.agent.tool.Tool;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Tool("Busca todas las películas que están dentro de la plataforma")
    public List<MovieDto> findAll() {
        return this.movieRepository.findAll();
    }

    public MovieDto findById(long id) {
        return this.movieRepository.findById(id);
    }

    public MovieDto create(CreateMovieDto movieDto) {
        return this.movieRepository.create(movieDto);
    }

    public MovieDto update(long id, UpdateMovieDto changes) {
        return this.movieRepository.update(id, changes);
    }

    public boolean delete(long id) {
        return this.movieRepository.delete(id);
    }
}
