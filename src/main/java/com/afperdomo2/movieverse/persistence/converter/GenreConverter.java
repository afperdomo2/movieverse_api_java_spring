package com.afperdomo2.movieverse.persistence.converter;

import com.afperdomo2.movieverse.domain.Genre;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenreConverter implements AttributeConverter<Genre, String> {

    @Override
    public String convertToDatabaseColumn(Genre genre) {
        return genre == null ? null : genre.label();
    }

    @Override
    public Genre convertToEntityAttribute(String value) {
        return value == null ? null : Genre.fromLabel(value);
    }
}
