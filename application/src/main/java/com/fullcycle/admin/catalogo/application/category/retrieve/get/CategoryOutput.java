package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 3/11/23 21:18
 * @email kalilmvp@gmail.com
 */
public record CategoryOutput(CategoryID id, String name, String description, boolean isActive, Instant createdAt,
                             Instant updatedAt, Instant deletedAt) {

    public static CategoryOutput from(final Category aCategory) {
        return new CategoryOutput(aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt());
    }
}
