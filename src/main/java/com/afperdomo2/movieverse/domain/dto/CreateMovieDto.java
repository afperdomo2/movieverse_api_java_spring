package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.afperdomo2.movieverse.domain.Genre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateMovieDto(
        @Schema(description = "Título de la película", example = "Shrek 2", maxLength = 255)
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 255, message = "El título no puede superar los 255 caracteres")
        String title,

        @Schema(description = "Duración en minutos", example = "93")
        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser mayor a 0 minutos")
        @Max(value = 600, message = "La duración no puede ser mayor a 600 minutos")
        Integer duration,

        @Schema(description = "Género de la película", example = "Animation")
        @NotNull(message = "El género es obligatorio")
        Genre genre,

        @Schema(description = "Fecha de lanzamiento", example = "2004-05-19")
        @NotNull(message = "La fecha de lanzamiento es obligatoria")
        @PastOrPresent(message = "La fecha de lanzamiento no puede ser mayor a la fecha actual")
        LocalDate releaseDate,

        @Schema(description = "Calificación entre 0 y 9", example = "7.3", minimum = "0", maximum = "9")
        @NotNull(message = "La calificación es obligatoria")
        @Min(value = 0, message = "La calificación debe ser mayor o igual a 0")
        @Max(value = 9, message = "La calificación debe ser menor o igual a 9")
        Double rating,

        @Schema(description = "IDs de los autores. Si se omite, la película se crea sin autores.", example = "[\"019b7c01-0bd0-7003-a001-0000030235ad\"]")
        Set<@NotNull(message = "El ID del autor no puede ser nulo") UUID> authorIds) {

    public CreateMovieDto {
        authorIds = authorIds == null ? Set.of() : Set.copyOf(authorIds);
    }

    public CreateMovieDto(String title, Integer duration, Genre genre, LocalDate releaseDate, Double rating) {
        this(title, duration, genre, releaseDate, rating, Set.of());
    }
}
