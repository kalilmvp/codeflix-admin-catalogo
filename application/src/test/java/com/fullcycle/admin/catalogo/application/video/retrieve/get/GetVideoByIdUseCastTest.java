package com.fullcycle.admin.catalogo.application.video.retrieve.get;

import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author kalil.peixoto
 * @date 5/3/24 16:23
 * @email kalilmvp@gmail.com
 */
public class GetVideoByIdUseCastTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetVideo_shouldDeleteIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedCastMembers = Set.of(
                Fixture.CastMembers.kalil().getId(),
                Fixture.CastMembers.wesley().getId()
        );

        final var expectedVideo = audioVideoMedia(Resource.Type.VIDEO);
        final var expectedTrailer = audioVideoMedia(Resource.Type.TRAILER);
        final var expectedBanner = image(Resource.Type.BANNER);
        final var expectedThumbnail = image(Resource.Type.THUMBNAIL);
        final var expectedThumbnailHalf = image(Resource.Type.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumbnail)
                .setThumbnailHalf(expectedThumbnailHalf);

        when(this.videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        // when
        final var expectedId = aVideo.getId();

        final var actualVideo = this.useCase.execute(expectedId.getValue());

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.id());

        assertEquals(expectedId.getValue(), actualVideo.id());
        assertEquals(expectedTitle, actualVideo.title());
        assertEquals(expectedDescription, actualVideo.description());
        assertEquals(expectedLaunchYear.getValue(), actualVideo.launchedAt());
        assertEquals(expectedRating.getName(), actualVideo.rating());
        assertEquals(expectedOpened, actualVideo.opened());
        assertEquals(expectedPublished, actualVideo.published());
        assertEquals(asString(expectedCategories), actualVideo.categories());
        assertEquals(asString(expectedGenres), actualVideo.genres());
        assertEquals(asString(expectedCastMembers), actualVideo.castMembers());
        assertEquals(expectedVideo, actualVideo.video());
        assertEquals(expectedBanner, actualVideo.banner());
        assertEquals(expectedTrailer, actualVideo.trailer());
        assertEquals(expectedThumbnail, actualVideo.thumbnail());
        assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
        assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedId = VideoID.from("123");

        when(this.videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualError = assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedId.getValue()));

        // then
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    private AudioVideoMedia audioVideoMedia(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return AudioVideoMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/videos/".concat(checksum),
                "",
                MediaStatus.PENDING
        );
    }

    private ImageMedia image(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/images/".concat(checksum)
        );
    }
}
