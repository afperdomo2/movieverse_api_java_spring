package com.afperdomo2.movieverse.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.exception.MovieAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;
import com.afperdomo2.movieverse.persistence.crud.CrudMovieEntity;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;
import com.afperdomo2.movieverse.persistence.mapper.MovieMapper;
import com.afperdomo2.movieverse.common.uuid.UuidV7Generator;

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
    public PageResponse<MovieDto> findPage(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id").ascending());
        Page<MovieEntity> result = this.crudMovieEntity.findAll(pageable);
        List<MovieDto> items = this.movieMapper.toDto(result.getContent());

        return new PageResponse<>(
                items,
                page,
                limit,
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious());
    }

    @Override
    public MovieDto findById(UUID id) {
        MovieEntity movie = this.crudMovieEntity.findById(id).orElse(null);
        if (movie == null) {
            throw new MovieNotFoundException(id);
        }
        return this.movieMapper.toDto(movie);
    }

    @Override
    public MovieDto create(CreateMovieDto movieDto) {
        if (this.crudMovieEntity.findByTitle(movieDto.title()) != null) {
            throw new MovieAlreadyExistsException(movieDto.title());
        }
        MovieEntity newMovie = new MovieEntity();
        this.movieMapper.createEntityFromDto(movieDto, newMovie);
        newMovie.setId(UuidV7Generator.generate());
        this.crudMovieEntity.save(newMovie);
        return this.movieMapper.toDto(newMovie);
    }

    @Override
    public MovieDto update(UUID id, UpdateMovieDto changes) {
        MovieEntity movie = this.crudMovieEntity.findById(id).orElse(null);
        if (movie == null) {
            throw new MovieNotFoundException(id);
        }
        // movie.setTitle(changes.title());
        // movie.setReleaseDate(changes.releaseDate());
        // movie.setRating(BigDecimal.valueOf(changes.rating()));
        this.movieMapper.updateEntityFromDto(changes, movie);
        this.crudMovieEntity.save(movie);
        return this.movieMapper.toDto(movie);
    }

    @Override
    @Transactional
    public MovieDto patch(UUID id, PatchMovieDto changes) {
        MovieEntity movie = this.crudMovieEntity.findById(id).orElse(null);
        if (movie == null) {
            throw new MovieNotFoundException(id);
        }

        if (changes.title().isEmpty()
                && changes.releaseDate().isEmpty()
                && changes.rating().isEmpty()
                && changes.genre().isEmpty()
                && changes.active().isEmpty()) {
            return this.movieMapper.toDto(movie);
        }

        changes.title().ifPresent(title -> updateTitle(id, movie, title));
        changes.releaseDate().ifPresent(movie::setReleaseDate);
        changes.rating().ifPresent(rating -> movie.setRating(BigDecimal.valueOf(rating)));
        changes.genre().ifPresent(movie::setGenre);
        changes.active().ifPresent(movie::setActive);

        this.crudMovieEntity.save(movie);
        return this.movieMapper.toDto(movie);
    }

    private void updateTitle(UUID id, MovieEntity movie, String title) {
        if (title.equals(movie.getTitle())) {
            return;
        }
        MovieEntity existing = this.crudMovieEntity.findByTitle(title);
        if (existing != null && !existing.getId().equals(id)) {
            throw new MovieAlreadyExistsException(title);
        }
        movie.setTitle(title);
    }

    @Override
    public boolean delete(UUID id) {
        if (!this.crudMovieEntity.existsById(id)) {
            throw new MovieNotFoundException(id);
        }
        this.crudMovieEntity.deleteById(id);
        return true;
    }
}
