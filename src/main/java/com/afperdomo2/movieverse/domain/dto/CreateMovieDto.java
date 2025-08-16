package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record CreateMovieDto(
        Long id,

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser mayor a 0 minutos")
        Integer duration,

        @NotNull(message = "El género es obligatorio")
        String genre,

        @NotNull(message = "La fecha de lanzamiento es obligatoria")
        @PastOrPresent(message = "La fecha de lanzamiento no puede ser mayor a la fecha actual")
        LocalDate releaseDate,

        @NotNull(message = "La calificación es obligatoria")
        @Min(value = 0, message = "La calificación debe ser mayor o igual a 0")
        @Max(value = 9, message = "La calificación debe ser menor o igual a 9")
        Double rating,

        String status) {
}
