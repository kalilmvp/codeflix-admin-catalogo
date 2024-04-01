package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberListResponse;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberResponse;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.UpdateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.presenters.CastMemberApiPresenter;
import com.fullcycle.admin.catalogo.infrastructure.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author kalil.peixoto
 * @date 16/3/24 21:16
 * @email kalilmvp@gmail.com
 */
@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMemberUseCase;

    public CastMemberController(CreateCastMemberUseCase createCastMemberUseCase, GetCastMemberByIdUseCase getCastMemberByIdUseCase, UpdateCastMemberUseCase updateCastMemberUseCase, DeleteCastMemberUseCase deleteCastMemberUseCase, ListCastMembersUseCase listCastMemberUseCase) {
        this.createCastMemberUseCase = createCastMemberUseCase;
        this.getCastMemberByIdUseCase = getCastMemberByIdUseCase;
        this.updateCastMemberUseCase = updateCastMemberUseCase;
        this.deleteCastMemberUseCase = deleteCastMemberUseCase;
        this.listCastMemberUseCase = listCastMemberUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/".concat(output.id()))).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(final String search,
                                                   final int page,
                                                   final int perPage,
                                                   final String sort,
                                                   final String direction) {
        return this.listCastMemberUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberApiPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberApiPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest input) {
        return ResponseEntity.ok(this.updateCastMemberUseCase.execute(UpdateCastMemberCommand.with(id, input.name(), input.type())));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
