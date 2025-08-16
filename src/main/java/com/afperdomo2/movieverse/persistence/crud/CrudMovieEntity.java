package com.afperdomo2.movieverse.persistence.crud;

import org.springframework.data.repository.CrudRepository;

import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

public interface CrudMovieEntity extends CrudRepository<MovieEntity, Long> {
    MovieEntity findByTitle(String title);
}
