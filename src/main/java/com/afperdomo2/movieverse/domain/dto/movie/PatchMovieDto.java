package com.afperdomo2.movieverse.domain.dto.movie;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.afperdomo2.movieverse.domain.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record PatchMovieDto(
        @Schema(description = "Nuevo título. Si se omite o se envía null, no se modifica.", example = "Interstellar", maxLength = 255)
        Optional<@Size(max = 255, message = "El título no puede superar los 255 caracteres") String> title,

        @Schema(description = "Nueva fecha de lanzamiento. Si se omite o se envía null, no se modifica.", example = "2014-11-07")
        Optional<@PastOrPresent(message = "La fecha de lanzamiento no puede ser mayor a la fecha actual") LocalDate> releaseDate,

        @Schema(description = "Nueva calificación entre 0 y 9. Si se omite o se envía null, no se modifica.", minimum = "0", maximum = "9", example = "8.6")
        Optional<@Min(value = 0, message = "La calificación debe ser mayor o igual a 0") @Max(value = 9, message = "La calificación debe ser menor o igual a 9") Double> rating,

        @Schema(description = "Nuevo género. Si se omite o se envía null, no se modifica.", example = "Comedy")
        Optional<Genre> genre,

        @Schema(description = "Activa o desactiva la película. Si se omite o se envía null, no se modifica.", example = "false")
        @JsonProperty("isActive")
        Optional<Boolean> active,

        @Schema(description = "Nuevos autores. Si se omite o se envía null, no se modifica. Una lista vacía elimina todos los autores.", example = "[\"019b7c01-0bd0-7003-a001-0000030235ad\"]")
        Optional<Set<@NotNull(message = "El ID del autor no puede ser nulo") UUID>> authorIds) {

    public PatchMovieDto {
        title = title == null ? Optional.empty() : title;
        releaseDate = releaseDate == null ? Optional.empty() : releaseDate;
        rating = rating == null ? Optional.empty() : rating;
        genre = genre == null ? Optional.empty() : genre;
        active = active == null ? Optional.empty() : active;
        authorIds = authorIds == null ? Optional.empty() : authorIds.map(Set::copyOf);
    }

    public PatchMovieDto(Optional<String> title, Optional<LocalDate> releaseDate, Optional<Double> rating) {
        this(title, releaseDate, rating, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public PatchMovieDto(Optional<String> title, Optional<LocalDate> releaseDate, Optional<Double> rating,
            Optional<Genre> genre, Optional<Boolean> active) {
        this(title, releaseDate, rating, genre, active, Optional.empty());
    }

    @JsonIgnore
    @AssertTrue(message = "El título es obligatorio")
    public boolean isTitleValid() {
        return title.map(value -> !value.isBlank()).orElse(true);
    }

    @JsonIgnore
    @AssertTrue(message = "Debes enviar al menos un campo para actualizar")
    public boolean isAnyFieldPresent() {
        return title.isPresent()
                || releaseDate.isPresent()
                || rating.isPresent()
                || genre.isPresent()
                || active.isPresent()
                || authorIds.isPresent();
    }
}
