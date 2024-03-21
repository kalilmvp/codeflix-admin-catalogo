package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import java.net.URI;

import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.api.GenreAPI;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.presenters.GenreApiPresenter;

/**
 * @author kalil.peixoto
 * @date 16/3/24 21:16
 * @email kalilmvp@gmail.com
 */
@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenreUseCase listGenreUseCase;

    public GenreController(CreateGenreUseCase createGenreUseCase, GetGenreByIdUseCase getGenreByIdUseCase, UpdateGenreUseCase updateGenreUseCase, DeleteGenreUseCase deleteGenreUseCase, ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(input.name(), input.isActive(), input.categories());

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/".concat(output.id()))).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(final String search,
                                              final int page,
                                              final int perPage,
                                              final String sort,
                                              final String direction) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(id, input.name(), input.isActive(), input.categories());

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}
