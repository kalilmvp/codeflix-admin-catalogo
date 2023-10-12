package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.domain.category.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kalil.peixoto
 * @date 10/9/23 23:02
 * @email kalilmvp@gmail.com
 */
@RestController
public class CategoryController implements CategoryAPI {

    @Override
    public ResponseEntity<?> createCategoru() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
