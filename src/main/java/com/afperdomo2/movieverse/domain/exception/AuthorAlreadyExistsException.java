package com.afperdomo2.movieverse.domain.exception;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String name, String lastName) {
        super("El autor '" + name + " " + lastName + "' ya existe.");
    }
}
