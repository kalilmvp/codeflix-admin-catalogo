package com.fullcycle.admin.catalogo.infrastructure.presenters;

import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;

/**
 * @author kalil.peixoto
 * @date 11/13/23 23:58
 * @email kalilmvp@gmail.com
 */
public interface CategoryApiPresenter {

    static CategoryAPIOutput present(final CategoryOutput output) {
        return new CategoryAPIOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt());
    }
}
