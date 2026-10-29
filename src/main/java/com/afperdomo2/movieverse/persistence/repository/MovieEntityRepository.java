package com.afperdomo2.movieverse.persistence.repository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.common.uuid.UuidV7Generator;
import com.afperdomo2.movieverse.domain.dto.movie.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.movie.MovieDto;
import com.afperdomo2.movieverse.domain.dto.movie.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.movie.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.exception.AuthorNotFoundException;
import com.afperdomo2.movieverse.domain.exception.MovieAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;
import com.afperdomo2.movieverse.domain.repository.MovieRepository;
import com.afperdomo2.movieverse.persistence.crud.CrudAuthorEntity;
import com.afperdomo2.movieverse.persistence.crud.CrudMovieEntity;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;
import com.afperdomo2.movieverse.persistence.mapper.MovieMapper;

@Repository
public class MovieEntityRepository implements MovieRepository {
    private final CrudMovieEntity crudMovieEntity;
    private final CrudAuthorEntity crudAuthorEntity;
    private final MovieMapper movieMapper;

    public MovieEntityRepository(CrudMovieEntity crudMovieEntity, CrudAuthorEntity crudAuthorEntity,
            MovieMapper movieMapper) {
        this.crudMovieEntity = crudMovieEntity;
        this.crudAuthorEntity = crudAuthorEntity;
        this.movieMapper = movieMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findAll() {
        return this.movieMapper.toDto(this.crudMovieEntity.findAll());
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public MovieDto findById(UUID id) {
        return this.movieMapper.toDto(findEntity(id));
    }

    @Override
    @Transactional
    public MovieDto create(CreateMovieDto movieDto) {
        if (this.crudMovieEntity.findByTitle(movieDto.title()) != null) {
            throw new MovieAlreadyExistsException(movieDto.title());
        }
        MovieEntity newMovie = new MovieEntity();
        this.movieMapper.createEntityFromDto(movieDto, newMovie);
        newMovie.setId(UuidV7Generator.generate());
        newMovie.setAuthors(resolveAuthors(movieDto.authorIds()));
        this.crudMovieEntity.save(newMovie);
        return this.movieMapper.toDto(newMovie);
    }

    @Override
    @Transactional
    public MovieDto update(UUID id, UpdateMovieDto changes) {
        MovieEntity movie = findEntity(id);
        this.movieMapper.updateEntityFromDto(changes, movie);
        movie.setAuthors(resolveAuthors(changes.authorIds()));
        this.crudMovieEntity.save(movie);
        return this.movieMapper.toDto(movie);
    }

    @Override
    @Transactional
    public MovieDto patch(UUID id, PatchMovieDto changes) {
        MovieEntity movie = findEntity(id);

        if (changes.title().isEmpty()
                && changes.releaseDate().isEmpty()
                && changes.rating().isEmpty()
                && changes.genre().isEmpty()
                && changes.active().isEmpty()
                && changes.authorIds().isEmpty()) {
            return this.movieMapper.toDto(movie);
        }

        changes.title().ifPresent(title -> updateTitle(id, movie, title));
        changes.releaseDate().ifPresent(movie::setReleaseDate);
        changes.rating().ifPresent(rating -> movie.setRating(BigDecimal.valueOf(rating)));
        changes.genre().ifPresent(movie::setGenre);
        changes.active().ifPresent(movie::setActive);
        changes.authorIds().ifPresent(authorIds -> movie.setAuthors(resolveAuthors(authorIds)));

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
    @Transactional
    public boolean delete(UUID id) {
        if (!this.crudMovieEntity.existsById(id)) {
            throw new MovieNotFoundException(id);
        }
        this.crudMovieEntity.deleteRelationsByMovieId(id);
        this.crudMovieEntity.deleteById(id);
        return true;
    }

    private MovieEntity findEntity(UUID id) {
        return this.crudMovieEntity.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    private Set<AuthorEntity> resolveAuthors(Set<UUID> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return new HashSet<>();
        }

        List<AuthorEntity> found = this.crudAuthorEntity.findAllById(authorIds);
        if (found.size() != authorIds.size()) {
            Set<UUID> foundIds = found.stream()
                    .map(AuthorEntity::getId)
                    .collect(Collectors.toSet());

            UUID missingId = authorIds.stream()
                    .filter(authorId -> !foundIds.contains(authorId))
                    .findFirst()
                    .orElseThrow();

            throw new AuthorNotFoundException(missingId);
        }

        return new HashSet<>(found);
    }
}
