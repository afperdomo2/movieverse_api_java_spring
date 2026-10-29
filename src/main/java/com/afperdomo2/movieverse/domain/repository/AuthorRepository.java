package com.afperdomo2.movieverse.domain.repository;

import java.util.UUID;

import com.afperdomo2.movieverse.domain.dto.author.AuthorDetailDto;
import com.afperdomo2.movieverse.domain.dto.author.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.author.CreateAuthorDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.author.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.author.UpdateAuthorDto;

public interface AuthorRepository {
    PageResponse<AuthorDto> findPage(int page, int limit);

    AuthorDetailDto findById(UUID id);

    AuthorDto create(CreateAuthorDto authorDto);

    AuthorDto update(UUID id, UpdateAuthorDto changes);

    AuthorDto patch(UUID id, PatchAuthorDto changes);

    boolean delete(UUID id);
}
