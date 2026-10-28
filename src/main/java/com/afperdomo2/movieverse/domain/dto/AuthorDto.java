package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorDto(
        UUID id,
        String name,
        String lastName,
        String nationality,
        LocalDate birthDate,
        String biography,
        @JsonProperty("isActive") boolean active) {
}
