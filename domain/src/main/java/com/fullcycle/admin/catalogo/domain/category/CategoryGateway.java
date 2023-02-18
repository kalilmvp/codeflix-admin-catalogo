package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.category.pagination.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.category.pagination.Pagination;

import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 2/18/23 11:13
 * @email kalilmvp@gmail.com
 */
public interface CategoryGateway {

    Category create(Category aCategory);
    void deleteById(CategoryID anId);
    Optional<Category> findById(CategoryID anId);
    Category update(Category aCategory);
    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
