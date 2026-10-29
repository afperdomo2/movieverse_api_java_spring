package com.afperdomo2.movieverse.domain.dto.movie;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.domain.dto.author.AuthorSummaryDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieDto(
        UUID id,
        String title,
        Integer duration,
        Genre genre,
        LocalDate releaseDate,
        @JsonProperty("isActive") boolean active,
        Double rating,
        String ratingString,
        List<AuthorSummaryDto> authors) {
}
