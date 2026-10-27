package com.afperdomo2.movieverse.persistence.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

public interface CrudMovieEntity extends JpaRepository<MovieEntity, Long> {
    MovieEntity findByTitle(String title);
}
