package com.afperdomo2.movieverse.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.common.pagination.PageQuery;
import com.afperdomo2.movieverse.common.pagination.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;
import com.afperdomo2.movieverse.domain.service.AuthorService;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    private static final UUID AUTHOR_ID = UUID.fromString("019b7c01-0bd0-7003-a001-0000030235ad");

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Test
    @DisplayName("findAll devuelve 200 con la página de autores")
    void findAll_returnsOkWithPage() {
        PageQuery query = new PageQuery();
        PageResponse<AuthorDto> page = new PageResponse<>(
                List.of(authorDto()), 1, 10, 19, 2, true, false);
        when(authorService.findPage(1, 10)).thenReturn(page);

        ResponseEntity<PageResponse<AuthorDto>> response = authorController.findAll(query);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(authorService).findPage(1, 10);
    }

    @Test
    @DisplayName("findAll usa los valores de page y limit de la query")
    void findAll_usesQueryParameters() {
        PageQuery query = new PageQuery();
        query.setPage(2);
        query.setLimit(5);
        PageResponse<AuthorDto> page = new PageResponse<>(
                List.of(authorDto()), 2, 5, 19, 4, true, true);
        when(authorService.findPage(2, 5)).thenReturn(page);

        ResponseEntity<PageResponse<AuthorDto>> response = authorController.findAll(query);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(page);
        verify(authorService).findPage(2, 5);
    }

    @Test
    @DisplayName("findById devuelve 200 con el autor")
    void findById_returnsOk() {
        AuthorDto author = authorDto();
        when(authorService.findById(AUTHOR_ID)).thenReturn(author);

        ResponseEntity<AuthorDto> response = authorController.findById(AUTHOR_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(author);
        verify(authorService).findById(AUTHOR_ID);
    }

    @Test
    @DisplayName("create devuelve 201 con el autor creado")
    void create_returnsCreated() {
        CreateAuthorDto request = createRequest();
        AuthorDto created = authorDto();
        when(authorService.create(request)).thenReturn(created);

        ResponseEntity<AuthorDto> response = authorController.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(created);
        verify(authorService).create(request);
    }

    @Test
    @DisplayName("update devuelve 200 con el autor actualizado")
    void update_returnsOk() {
        UpdateAuthorDto changes = new UpdateAuthorDto("Christopher", "Nolan", "British",
                LocalDate.of(1970, 7, 30), "Director.");
        AuthorDto updated = authorDto();
        when(authorService.update(AUTHOR_ID, changes)).thenReturn(updated);

        ResponseEntity<AuthorDto> response = authorController.update(AUTHOR_ID, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(authorService).update(AUTHOR_ID, changes);
    }

    @Test
    @DisplayName("patch devuelve 200 con el autor actualizado")
    void patch_returnsOk() {
        PatchAuthorDto changes = new PatchAuthorDto(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(false));
        AuthorDto updated = authorDto();
        when(authorService.patch(AUTHOR_ID, changes)).thenReturn(updated);

        ResponseEntity<AuthorDto> response = authorController.patch(AUTHOR_ID, changes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(authorService).patch(AUTHOR_ID, changes);
    }

    @Test
    @DisplayName("delete devuelve 204")
    void delete_returnsNoContent() {
        when(authorService.delete(AUTHOR_ID)).thenReturn(true);

        ResponseEntity<Void> response = authorController.delete(AUTHOR_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(authorService).delete(AUTHOR_ID);
    }

    private static AuthorDto authorDto() {
        return new AuthorDto(AUTHOR_ID, "Christopher", "Nolan", "British-American",
                LocalDate.of(1970, 7, 30), "Director.", true);
    }

    private static CreateAuthorDto createRequest() {
        return new CreateAuthorDto("Christopher", "Nolan", "British-American",
                LocalDate.of(1970, 7, 30), "Director.");
    }
}
