package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;

public record UpdateMovieDto(
        String title,
        LocalDate releaseDate,
        Double rating) {
}
