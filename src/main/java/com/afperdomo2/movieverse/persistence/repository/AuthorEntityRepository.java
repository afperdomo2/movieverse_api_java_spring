package com.afperdomo2.movieverse.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;
import com.afperdomo2.movieverse.domain.exception.AuthorAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.AuthorNotFoundException;
import com.afperdomo2.movieverse.domain.repository.AuthorRepository;
import com.afperdomo2.movieverse.persistence.crud.CrudAuthorEntity;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;
import com.afperdomo2.movieverse.persistence.mapper.AuthorMapper;
import com.afperdomo2.movieverse.common.uuid.UuidV7Generator;

@Repository
public class AuthorEntityRepository implements AuthorRepository {
    private final CrudAuthorEntity crudAuthorEntity;
    private final AuthorMapper authorMapper;

    public AuthorEntityRepository(CrudAuthorEntity crudAuthorEntity, AuthorMapper authorMapper) {
        this.crudAuthorEntity = crudAuthorEntity;
        this.authorMapper = authorMapper;
    }

    @Override
    public PageResponse<AuthorDto> findPage(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("lastName", "name"));
        Page<AuthorEntity> result = this.crudAuthorEntity.findAll(pageable);
        List<AuthorDto> items = this.authorMapper.toDto(result.getContent());

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
    public AuthorDto findById(UUID id) {
        return this.authorMapper.toDto(findEntity(id));
    }

    @Override
    public AuthorDto create(CreateAuthorDto authorDto) {
        if (this.crudAuthorEntity.findByNameAndLastName(authorDto.name(), authorDto.lastName()) != null) {
            throw new AuthorAlreadyExistsException(authorDto.name(), authorDto.lastName());
        }
        AuthorEntity newAuthor = new AuthorEntity();
        this.authorMapper.createEntityFromDto(authorDto, newAuthor);
        newAuthor.setId(UuidV7Generator.generate());
        this.crudAuthorEntity.save(newAuthor);
        return this.authorMapper.toDto(newAuthor);
    }

    @Override
    public AuthorDto update(UUID id, UpdateAuthorDto changes) {
        AuthorEntity author = findEntity(id);
        validateUniqueName(id, changes.name(), changes.lastName());
        this.authorMapper.updateEntityFromDto(changes, author);
        this.crudAuthorEntity.save(author);
        return this.authorMapper.toDto(author);
    }

    @Override
    @Transactional
    public AuthorDto patch(UUID id, PatchAuthorDto changes) {
        AuthorEntity author = findEntity(id);

        if (changes.name().isEmpty()
                && changes.lastName().isEmpty()
                && changes.nationality().isEmpty()
                && changes.birthDate().isEmpty()
                && changes.biography().isEmpty()
                && changes.active().isEmpty()) {
            return this.authorMapper.toDto(author);
        }

        validateUniqueName(
                id,
                changes.name().orElse(author.getName()),
                changes.lastName().orElse(author.getLastName()));

        changes.name().ifPresent(author::setName);
        changes.lastName().ifPresent(author::setLastName);
        changes.nationality().ifPresent(author::setNationality);
        changes.birthDate().ifPresent(author::setBirthDate);
        changes.biography().ifPresent(author::setBiography);
        changes.active().ifPresent(author::setActive);

        this.crudAuthorEntity.save(author);
        return this.authorMapper.toDto(author);
    }

    @Override
    public boolean delete(UUID id) {
        if (!this.crudAuthorEntity.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        this.crudAuthorEntity.deleteById(id);
        return true;
    }

    private AuthorEntity findEntity(UUID id) {
        return this.crudAuthorEntity.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    private void validateUniqueName(UUID id, String name, String lastName) {
        AuthorEntity existing = this.crudAuthorEntity.findByNameAndLastName(name, lastName);
        if (existing != null && !id.equals(existing.getId())) {
            throw new AuthorAlreadyExistsException(name, lastName);
        }
    }
}
