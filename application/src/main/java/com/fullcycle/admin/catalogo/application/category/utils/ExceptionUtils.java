package com.fullcycle.admin.catalogo.application.category.utils;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;

import java.util.function.Supplier;

/**
 * @author kalil.peixoto
 * @date 3/11/23 21:23
 * @email kalilmvp@gmail.com
 */
public class ExceptionUtils {

    private ExceptionUtils() {}

    public static Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }
}
