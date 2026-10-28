package com.afperdomo2.movieverse.web.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.domain.dto.PageQuery;
import com.afperdomo2.movieverse.domain.dto.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;
import com.afperdomo2.movieverse.domain.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("authors")
@Tag(name = "Authors", description = "Operaciones relacionadas con autores")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping()
    @Operation(summary = "Listar autores paginados", description = "Devuelve una página de autores ordenada por apellido y nombre. Por defecto page=1 y limit=10", responses = {
            @ApiResponse(responseCode = "200", description = "Página de autores encontrada"),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos", content = @Content())
    })
    public ResponseEntity<PageResponse<AuthorDto>> findAll(
            @ParameterObject @Valid @ModelAttribute PageQuery query) {
        return ResponseEntity.ok(this.authorService.findPage(query.getPage(), query.getLimit()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por ID", description = "Devuelve un autor específico", responses = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content())
    })
    public ResponseEntity<AuthorDto> findById(
            @Parameter(description = "Identificador del autor", example = "019b7c01-0bd0-7003-a001-0000030235ad") @PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.authorService.findById(id));
    }

    @PostMapping()
    @Operation(summary = "Crear nuevo autor", description = "Crea un nuevo autor", responses = {
            @ApiResponse(responseCode = "201", description = "Autor creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content()),
            @ApiResponse(responseCode = "409", description = "El autor ya existe", content = @Content())
    })
    public ResponseEntity<AuthorDto> create(@RequestBody @Valid CreateAuthorDto authorDto) {
        AuthorDto authorCreated = this.authorService.create(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorCreated);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor", description = "Actualiza un autor existente", responses = {
            @ApiResponse(responseCode = "200", description = "Autor actualizado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content()),
            @ApiResponse(responseCode = "409", description = "El autor ya existe", content = @Content())
    })
    public ResponseEntity<AuthorDto> update(
            @Parameter(description = "Identificador del autor", example = "019b7c01-0bd0-7003-a001-0000030235ad") @PathVariable("id") UUID id,
            @RequestBody @Valid UpdateAuthorDto changes) {
        return ResponseEntity.ok(this.authorService.update(id, changes));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un autor", description = "Actualiza solo los campos enviados", responses = {
            @ApiResponse(responseCode = "200", description = "Autor actualizado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content()),
            @ApiResponse(responseCode = "409", description = "El autor ya existe", content = @Content())
    })
    public ResponseEntity<AuthorDto> patch(
            @Parameter(description = "Identificador del autor", example = "019b7c01-0bd0-7003-a001-0000030235ad") @PathVariable("id") UUID id,
            @RequestBody @Valid PatchAuthorDto changes) {
        return ResponseEntity.ok(this.authorService.patch(id, changes));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor", description = "Elimina un autor existente", responses = {
            @ApiResponse(responseCode = "204", description = "Autor eliminado"),
            @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content())
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador del autor", example = "019b7c01-0bd0-7003-a001-0000030235ad") @PathVariable("id") UUID id) {
        this.authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
