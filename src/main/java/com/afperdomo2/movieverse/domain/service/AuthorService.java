package com.afperdomo2.movieverse.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.afperdomo2.movieverse.domain.dto.author.AuthorDetailDto;
import com.afperdomo2.movieverse.domain.dto.author.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.author.CreateAuthorDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.author.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.author.UpdateAuthorDto;
import com.afperdomo2.movieverse.domain.repository.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public PageResponse<AuthorDto> findPage(int page, int limit) {
        return this.authorRepository.findPage(page, limit);
    }

    public AuthorDetailDto findById(UUID id) {
        return this.authorRepository.findById(id);
    }

    public AuthorDto create(CreateAuthorDto authorDto) {
        return this.authorRepository.create(authorDto);
    }

    public AuthorDto update(UUID id, UpdateAuthorDto changes) {
        return this.authorRepository.update(id, changes);
    }

    public AuthorDto patch(UUID id, PatchAuthorDto changes) {
        return this.authorRepository.patch(id, changes);
    }

    public boolean delete(UUID id) {
        return this.authorRepository.delete(id);
    }
}
