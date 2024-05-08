package com.fullcycle.admin.catalogo.application.video.retrieve.list;

import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;
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
public class ListVideosUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase listVideoUseCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.videoGateway);
    }

    @Test
    public void givenAValidQuery_whenCallListVideos_shouldReturnVideos() {
        final var videos = List.of(
                Fixture.video(),
                Fixture.video()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);

        when(this.videoGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(VideosListOutput::from);

        final var actualResult = this.listVideoUseCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(videos.size(), actualResult.total());

        verify(this.videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAnInvalidId_whenHasNoResults_shouldReturnEmptyVideos() {
        final var videos = List.<Video>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);

        when(this.videoGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination;

        final var actualResult = this.listVideoUseCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(videos.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_wheGatewayThrowsError_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(this.videoGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(IllegalStateException.class, () -> this.listVideoUseCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
