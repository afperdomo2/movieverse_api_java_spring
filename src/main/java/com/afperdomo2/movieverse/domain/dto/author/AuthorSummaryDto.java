package com.afperdomo2.movieverse.domain.dto.author;

import java.util.UUID;

public record AuthorSummaryDto(
        UUID id,
        String name,
        String lastName) {
}
