package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.domain.category.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import com.fullcycle.admin.catalogo.infrastructure.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author kalil.peixoto
 * @date 10/9/23 23:02
 * @email kalilmvp@gmail.com
 */
@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    }

    @Override
    public ResponseEntity<?> createCategoru(final CreateCategoryAPIInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = notification -> ResponseEntity.unprocessableEntity().body(notification);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity.created(URI.create("/categories/".concat(output.id()))).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryAPIOutput getById(String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }
}
