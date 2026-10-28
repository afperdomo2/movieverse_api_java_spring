package com.afperdomo2.movieverse.domain.exception;

import java.util.UUID;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(UUID id) {
        super("El autor con el ID '" + id + "' no fue encontrado.");
    }
}
