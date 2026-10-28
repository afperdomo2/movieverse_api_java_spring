package com.afperdomo2.movieverse.domain.repository;

import java.util.UUID;

import com.afperdomo2.movieverse.domain.dto.AuthorDetailDto;
import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;

public interface AuthorRepository {
    PageResponse<AuthorDto> findPage(int page, int limit);

    AuthorDetailDto findById(UUID id);

    AuthorDto create(CreateAuthorDto authorDto);

    AuthorDto update(UUID id, UpdateAuthorDto changes);

    AuthorDto patch(UUID id, PatchAuthorDto changes);

    boolean delete(UUID id);
}
