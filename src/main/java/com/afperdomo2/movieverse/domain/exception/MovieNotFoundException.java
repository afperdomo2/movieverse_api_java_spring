package com.afperdomo2.movieverse.domain.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(long id) {
        super("La película con el ID '" + id + "' no fue encontrada.");
    }
}
