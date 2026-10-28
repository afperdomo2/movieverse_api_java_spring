package com.afperdomo2.movieverse.domain.exception;

import java.util.UUID;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(UUID id) {
        super("La película con el ID '" + id + "' no fue encontrada.");
    }
}
