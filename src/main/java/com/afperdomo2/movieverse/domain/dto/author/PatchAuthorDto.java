package com.afperdomo2.movieverse.domain.dto.author;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record PatchAuthorDto(
        @Schema(description = "Nuevo nombre. Si se omite o se envía null, no se modifica.", example = "Christopher", maxLength = 100)
        Optional<@Size(max = 100, message = "El nombre no puede superar los 100 caracteres") String> name,

        @Schema(description = "Nuevo apellido. Si se omite o se envía null, no se modifica.", example = "Nolan", maxLength = 100)
        Optional<@Size(max = 100, message = "El apellido no puede superar los 100 caracteres") String> lastName,

        @Schema(description = "Nueva nacionalidad. Si se omite o se envía null, no se modifica.", example = "British-American", maxLength = 100)
        Optional<@Size(max = 100, message = "La nacionalidad no puede superar los 100 caracteres") String> nationality,

        @Schema(description = "Nueva fecha de nacimiento. Si se omite o se envía null, no se modifica.", example = "1970-07-30")
        Optional<@PastOrPresent(message = "La fecha de nacimiento no puede ser mayor a la fecha actual") LocalDate> birthDate,

        @Schema(description = "Nueva biografía. Si se omite o se envía null, no se modifica.", example = "Director, guionista y productor.", maxLength = 1000)
        Optional<@Size(max = 1000, message = "La biografía no puede superar los 1000 caracteres") String> biography,

        @Schema(description = "Activa o desactiva el autor. Si se omite o se envía null, no se modifica.", example = "false")
        @JsonProperty("isActive")
        Optional<Boolean> active) {

    public PatchAuthorDto {
        name = name == null ? Optional.empty() : name;
        lastName = lastName == null ? Optional.empty() : lastName;
        nationality = nationality == null ? Optional.empty() : nationality;
        birthDate = birthDate == null ? Optional.empty() : birthDate;
        biography = biography == null ? Optional.empty() : biography;
        active = active == null ? Optional.empty() : active;
    }

    public PatchAuthorDto(Optional<String> name, Optional<String> lastName, Optional<String> nationality,
            Optional<LocalDate> birthDate, Optional<String> biography) {
        this(name, lastName, nationality, birthDate, biography, Optional.empty());
    }

    @JsonIgnore
    @AssertTrue(message = "El nombre es obligatorio")
    public boolean isNameValid() {
        return name.map(value -> !value.isBlank()).orElse(true);
    }

    @JsonIgnore
    @AssertTrue(message = "El apellido es obligatorio")
    public boolean isLastNameValid() {
        return lastName.map(value -> !value.isBlank()).orElse(true);
    }

    @JsonIgnore
    @AssertTrue(message = "Debes enviar al menos un campo para actualizar")
    public boolean isAnyFieldPresent() {
        return name.isPresent()
                || lastName.isPresent()
                || nationality.isPresent()
                || birthDate.isPresent()
                || biography.isPresent()
                || active.isPresent();
    }
}
