package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record UpdateMovieDto(
        @Schema(description = "Título de la película", example = "Interstellar", maxLength = 255)
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 255, message = "El título no puede superar los 255 caracteres")
        String title,

        @Schema(description = "Fecha de lanzamiento", example = "2014-11-07")
        @NotNull(message = "La fecha de lanzamiento es obligatoria")
        @PastOrPresent(message = "La fecha de lanzamiento no puede ser mayor a la fecha actual")
        LocalDate releaseDate,

        @Schema(description = "Calificación entre 0 y 9", example = "8.6", minimum = "0", maximum = "9")
        @Min(value = 0, message = "La calificación debe ser mayor o igual a 0")
        @Max(value = 9, message = "La calificación debe ser menor o igual a 9")
        Double rating,

        @Schema(description = "IDs de los autores. Enviar una lista vacía para dejar la película sin autores.", example = "[\"019b7c01-0bd0-7003-a001-0000030235ad\"]")
        @NotNull(message = "La lista de autores es obligatoria")
        Set<@NotNull(message = "El ID del autor no puede ser nulo") UUID> authorIds) {

    public UpdateMovieDto(String title, LocalDate releaseDate, Double rating) {
        this(title, releaseDate, rating, Set.of());
    }
}
