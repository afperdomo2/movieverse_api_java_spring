package com.afperdomo2.movieverse.domain.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        int page,
        int limit,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {
}
