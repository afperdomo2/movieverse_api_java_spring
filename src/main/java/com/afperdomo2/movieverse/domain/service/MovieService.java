package com.afperdomo2.movieverse.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> findAll() {
        return this.movieRepository.findAll();
    }

    public MovieDto findById(long id) {
        return this.movieRepository.findById(id);
    }
}
