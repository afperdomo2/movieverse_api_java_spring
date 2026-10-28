package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record CreateAuthorDto(
        @Schema(description = "Nombre del autor", example = "Christopher", maxLength = 100)
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @Schema(description = "Apellido del autor", example = "Nolan", maxLength = 100)
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String lastName,

        @Schema(description = "Nacionalidad del autor", example = "British-American", maxLength = 100)
        @Size(max = 100, message = "La nacionalidad no puede superar los 100 caracteres")
        String nationality,

        @Schema(description = "Fecha de nacimiento", example = "1970-07-30")
        @PastOrPresent(message = "La fecha de nacimiento no puede ser mayor a la fecha actual")
        LocalDate birthDate,

        @Schema(description = "Biografía del autor", example = "Director, guionista y productor.", maxLength = 1000)
        @Size(max = 1000, message = "La biografía no puede superar los 1000 caracteres")
        String biography) {
}
