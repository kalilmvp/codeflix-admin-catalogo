package com.fullcycle.admin.catalogo.e2e;

import com.fullcycle.admin.catalogo.ApiTest;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberResponse;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.UpdateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author kalil.peixoto
 * @date 3/21/24 14:27
 * @email kalilmvp@gmail.com
 */
public interface MockDsl {
    String MYSQL_VERSION = "mysql:8.2.0";

    MockMvc mock();

    /**
     * Cast Member
     */
    default CastMemberID givenACastMember(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        return CastMemberID.from(this.given("/cast_members", aRequestBody));
    }

    default ResultActions givenACastMemberResult(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        return this.givenResult("/cast_members", aRequestBody);
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return this.listCastMembers(page, perPage, search, "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return this.listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/cast_members", page, perPage, search, sort, direction);
    }

    default CastMemberResponse retrieveACastMember(final Identifier anId) throws Exception {
        return this.retrieve("/cast_members/", anId, CastMemberResponse.class);
    }

    default ResultActions retrieveACastMemberResult(final Identifier anId) throws Exception {
        return this.retrieveResult("/cast_members/", anId);
    }

    default ResultActions updateACastMember(final Identifier anId, final String aName, final CastMemberType aType) throws Exception {
        return this.update("/cast_members/", anId, new UpdateCastMemberRequest(aName, aType));
    }

    default ResultActions deleteACastMember(final Identifier anId) throws Exception {
        return this.delete("/cast_members/", anId);
    }

    /**
     * Genre
     */
    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        return GenreID.from(this.given("/genres", aRequestBody));
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return this.listGenres(page, perPage, search, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return this.listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(
            final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }

    default GenreResponse retrieveAGenre(final Identifier anId) throws Exception {
        return this.retrieve("/genres/", anId, GenreResponse.class);
    }

    default ResultActions updateAGenre(final Identifier anId, final UpdateGenreRequest updateGenreRequest) throws Exception {
        return this.update("/genres/", anId, updateGenreRequest);
    }

    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres/", anId);
    }

    /**
     * Category
     */

    default CategoryID givenACategory(final String aName, String aDescription, final boolean aIsActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, aIsActive);
        return CategoryID.from(this.given("/categories", aRequestBody));
    }

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories/", anId);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return this.listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return this.listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(
            final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return this.retrieve("/categories/", anId, CategoryResponse.class);
    }

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryRequest updateCategoryRequest) throws Exception {
        return this.update("/categories/", anId, updateCategoryRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(ApiTest.ADMIN_JWT);

        final var json = this.mock().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions retrieveResult(final String url, final Identifier anId) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(ApiTest.ADMIN_JWT);

        return this.mock().perform(aRequest);
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String dir) throws Exception {
        final var aRequest = get(url)
                .with(ApiTest.ADMIN_JWT)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", dir)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mock().perform(aRequest);
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = this.mock().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mock().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object aBody) throws Exception {
        final var aRequest = put(url.concat(anId.getValue()))
                .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aBody));

        return this.mock().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        return this.mock().perform(MockMvcRequestBuilders.delete(url.concat(anId.getValue()))
                        .with(ApiTest.ADMIN_JWT)
                .contentType(MediaType.APPLICATION_JSON));
    }

    default <A, D> List<D> mapTo(final List<A> actual, Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }
}
