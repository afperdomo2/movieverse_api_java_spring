package com.afperdomo2.movieverse.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.afperdomo2.movieverse.domain.dto.author.AuthorDetailDto;
import com.afperdomo2.movieverse.domain.dto.author.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.author.CreateAuthorDto;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.author.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.author.UpdateAuthorDto;
import com.afperdomo2.movieverse.domain.exception.AuthorNotFoundException;
import com.afperdomo2.movieverse.domain.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private static final UUID AUTHOR_ID = UUID.fromString("019b7c01-0bd0-7003-a001-0000030235ad");
    private static final UUID UNKNOWN_AUTHOR_ID = UUID.fromString("019b7c01-4a50-7013-8ab1-0000130dfe9d");

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    @DisplayName("findPage delega en el repositorio")
    void findPage_delegatesToRepository() {
        PageResponse<AuthorDto> page = new PageResponse<>(
                List.of(authorDto()), 1, 10, 19, 2, true, false);
        when(authorRepository.findPage(1, 10)).thenReturn(page);

        assertThat(authorService.findPage(1, 10)).isEqualTo(page);
        verify(authorRepository).findPage(1, 10);
    }

    @Test
    @DisplayName("findById delega en el repositorio")
    void findById_delegatesToRepository() {
        AuthorDetailDto author = authorDetailDto();
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(author);

        assertThat(authorService.findById(AUTHOR_ID)).isEqualTo(author);
        verify(authorRepository).findById(AUTHOR_ID);
    }

    @Test
    @DisplayName("findById propaga AuthorNotFoundException")
    void findById_propagatesAuthorNotFound() {
        when(authorRepository.findById(UNKNOWN_AUTHOR_ID))
                .thenThrow(new AuthorNotFoundException(UNKNOWN_AUTHOR_ID));

        assertThatThrownBy(() -> authorService.findById(UNKNOWN_AUTHOR_ID))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining(UNKNOWN_AUTHOR_ID.toString());
    }

    @Test
    @DisplayName("create delega en el repositorio")
    void create_delegatesToRepository() {
        CreateAuthorDto request = createRequest();
        AuthorDto created = authorDto();
        when(authorRepository.create(request)).thenReturn(created);

        assertThat(authorService.create(request)).isEqualTo(created);
        verify(authorRepository).create(request);
    }

    @Test
    @DisplayName("update delega en el repositorio")
    void update_delegatesToRepository() {
        UpdateAuthorDto changes = new UpdateAuthorDto("Christopher", "Nolan", "British",
                LocalDate.of(1970, 7, 30), "Director.");
        AuthorDto updated = authorDto();
        when(authorRepository.update(AUTHOR_ID, changes)).thenReturn(updated);

        assertThat(authorService.update(AUTHOR_ID, changes)).isEqualTo(updated);
        verify(authorRepository).update(AUTHOR_ID, changes);
    }

    @Test
    @DisplayName("patch delega en el repositorio")
    void patch_delegatesToRepository() {
        PatchAuthorDto changes = new PatchAuthorDto(Optional.of("Christopher"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        AuthorDto updated = authorDto();
        when(authorRepository.patch(AUTHOR_ID, changes)).thenReturn(updated);

        assertThat(authorService.patch(AUTHOR_ID, changes)).isEqualTo(updated);
        verify(authorRepository).patch(AUTHOR_ID, changes);
    }

    @Test
    @DisplayName("delete delega en el repositorio")
    void delete_delegatesToRepository() {
        when(authorRepository.delete(AUTHOR_ID)).thenReturn(true);

        assertThat(authorService.delete(AUTHOR_ID)).isTrue();
        verify(authorRepository).delete(AUTHOR_ID);
    }

    private static AuthorDto authorDto() {
        return new AuthorDto(AUTHOR_ID, "Christopher", "Nolan", "British-American",
                LocalDate.of(1970, 7, 30), "Director.", true);
    }

    private static AuthorDetailDto authorDetailDto() {
        return new AuthorDetailDto(AUTHOR_ID, "Christopher", "Nolan", "British-American",
                LocalDate.of(1970, 7, 30), "Director.", true, List.of());
    }

    private static CreateAuthorDto createRequest() {
        return new CreateAuthorDto("Christopher", "Nolan", "British-American",
                LocalDate.of(1970, 7, 30), "Director.");
    }
}
