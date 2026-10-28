package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.afperdomo2.movieverse.domain.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieSummaryDto(
        UUID id,
        String title,
        Integer duration,
        Genre genre,
        LocalDate releaseDate,
        @JsonProperty("isActive") boolean active,
        Double rating,
        String ratingString) {
}
