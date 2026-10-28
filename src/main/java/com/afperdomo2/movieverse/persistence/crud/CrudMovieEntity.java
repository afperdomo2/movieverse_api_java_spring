package com.afperdomo2.movieverse.persistence.crud;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.afperdomo2.movieverse.persistence.entity.MovieEntity;

public interface CrudMovieEntity extends JpaRepository<MovieEntity, UUID> {
    MovieEntity findByTitle(String title);

    @EntityGraph(attributePaths = "authors")
    Optional<MovieEntity> findById(UUID id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM movie_authors WHERE movie_id = :movieId", nativeQuery = true)
    int deleteRelationsByMovieId(@Param("movieId") UUID movieId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM movie_authors WHERE author_id = :authorId", nativeQuery = true)
    int deleteRelationsByAuthorId(@Param("authorId") UUID authorId);
}
