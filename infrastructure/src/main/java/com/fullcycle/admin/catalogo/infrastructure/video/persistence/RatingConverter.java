package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.Rating;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author kalil.peixoto
 * @date 6/20/24 17:45
 * @email kalilmvp@gmail.com
 */
@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        if (rating == null) return null;
        return rating.getName();
    }

    @Override
    public Rating convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Rating.of(dbData).orElse(null);
    }
}
