package com.fullcycle.admin.catalogo.application.category.retrieve.list;

import com.fullcycle.admin.catalogo.application.UseCase;
import com.fullcycle.admin.catalogo.domain.category.pagination.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.category.pagination.Pagination;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:55
 * @email kalilmvp@gmail.com
 */
public abstract class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {

}
