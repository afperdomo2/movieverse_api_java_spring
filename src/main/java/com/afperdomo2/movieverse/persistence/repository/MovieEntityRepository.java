package com.afperdomo2.movieverse.persistence.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;
import com.afperdomo2.movieverse.persistence.crud.CrudMovieEntity;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;
import com.afperdomo2.movieverse.persistence.mapper.MovieMapper;

@Repository
public class MovieEntityRepository implements MovieRepository {
    private final CrudMovieEntity crudMovieEntity;
    private final MovieMapper movieMapper;

    public MovieEntityRepository(CrudMovieEntity crudMovieEntity, MovieMapper movieMapper) {
        this.crudMovieEntity = crudMovieEntity;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDto> findAll() {
        return this.movieMapper.toDto(this.crudMovieEntity.findAll());
    }

    @Override
    public MovieDto findById(long id) {
        MovieEntity movie = this.crudMovieEntity.findById(id).orElse(null);
        return this.movieMapper.toDto(movie);
    }

    @Override
    public MovieDto create(MovieDto movieDto) {
        MovieEntity newMovie = this.movieMapper.toEntity(movieDto);
        newMovie.setStatus("D");
        this.crudMovieEntity.save(newMovie);
        return this.movieMapper.toDto(newMovie);
    }

    @Override
    public MovieDto update(long id, UpdateMovieDto changes) {
        MovieEntity movie = this.crudMovieEntity.findById(id).orElse(null);
        if (movie == null) {
            return null;
        }
        // movie.setTitle(changes.title());
        // movie.setReleaseDate(changes.releaseDate());
        // movie.setRating(BigDecimal.valueOf(changes.rating()));
        this.movieMapper.updateEntityFromDto(changes, movie);
        this.crudMovieEntity.save(movie);
        return this.movieMapper.toDto(movie);
    }

    @Override
    public boolean delete(long id) {
        if (!this.crudMovieEntity.existsById(id)) {
            return false;
        }
        this.crudMovieEntity.deleteById(id);
        return true;
    }
}
