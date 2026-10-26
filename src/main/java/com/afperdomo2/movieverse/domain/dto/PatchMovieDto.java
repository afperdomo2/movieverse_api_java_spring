package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;

public record PatchMovieDto(
        @Schema(description = "Nuevo título. Si se omite o se envía null, no se modifica.", example = "Interstellar")
        Optional<String> title,

        @Schema(description = "Nueva fecha de lanzamiento. Si se omite o se envía null, no se modifica.", example = "2014-11-07")
        Optional<@PastOrPresent(message = "La fecha de lanzamiento no puede ser mayor a la fecha actual") LocalDate> releaseDate,

        @Schema(description = "Nueva calificación entre 0 y 9. Si se omite o se envía null, no se modifica.", minimum = "0", maximum = "9", example = "8.6")
        Optional<@Min(value = 0, message = "La calificación debe ser mayor o igual a 0") @Max(value = 9, message = "La calificación debe ser menor o igual a 9") Double> rating) {

    public PatchMovieDto {
        title = title == null ? Optional.empty() : title;
        releaseDate = releaseDate == null ? Optional.empty() : releaseDate;
        rating = rating == null ? Optional.empty() : rating;
    }

    @JsonIgnore
    @AssertTrue(message = "El título es obligatorio")
    public boolean isTitleValid() {
        return title.map(value -> !value.isBlank()).orElse(true);
    }

    @JsonIgnore
    @AssertTrue(message = "Debes enviar al menos un campo para actualizar")
    public boolean isAnyFieldPresent() {
        return title.isPresent() || releaseDate.isPresent() || rating.isPresent();
    }
}
