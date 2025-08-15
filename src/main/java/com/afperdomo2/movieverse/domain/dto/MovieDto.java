package com.afperdomo2.movieverse.domain.dto;

import java.time.LocalDate;

public record MovieDto(
        Long id,
        String title, Integer duration, String genre, LocalDate releaseDate, String status, Double rating,
        String ratingString) {
}
