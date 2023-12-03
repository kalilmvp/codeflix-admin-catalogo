package com.fullcycle.admin.catalogo.application.category.retrieve.list;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 3/11/23 19:42
 * @email kalilmvp@gmail.com
 */
public class ListCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCategoryUseCase defaultListCategoryUseCase;

    @Mock
    private CategoryGateway categoryGatewayMock;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.categoryGatewayMock);
    }

    @Test
    public void givenAValidQuery_whenCallListCategories_shouldReturnCategories() {
        final var categories = List.of(
                Category.newCategory("Filmes", null, Boolean.TRUE),
                Category.newCategory("Series", null, Boolean.TRUE),
                Category.newCategory("Other", null, Boolean.TRUE)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        when(this.categoryGatewayMock.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var expectedItemsCount = 3;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        final var actualResult = this.defaultListCategoryUseCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAnInvalidId_whenHasNoResults_shouldReturnEmptyCategories() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        when(this.categoryGatewayMock.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination;

        final var actualResult = this.defaultListCategoryUseCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_wheGatewayThrowsError_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(this.categoryGatewayMock.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));


        final var actualException = assertThrows(IllegalStateException.class, () -> this.defaultListCategoryUseCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
