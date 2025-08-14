package com.afperdomo2.movieverse.persistence.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.afperdomo2.movieverse.domain.RatingString;

@Mapper(componentModel = "spring")
public class RatingMapper {
    @Named("bigDecimalToRatingString")
    public static RatingString bigDecimalToRatingString(BigDecimal rating) {
        if (rating == null) {
            return null;
        }
        double value = rating.doubleValue();
        int category = (int) Math.floor(value / 2);

        return switch (category) {
            case 4, 5 -> RatingString.VERY_GOOD; // 8.0 - 10.0
            case 3 -> RatingString.GOOD; // 6.0 - 7.99
            case 2 -> RatingString.AVERAGE; // 4.0 - 5.99
            case 1 -> RatingString.BAD; // 2.0 - 3.99
            default -> RatingString.VERY_BAD; // < 2.0
        };
    }
}
