package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 4/29/24 21:34
 * @email kalilmvp@gmail.com
 */
public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.gateway, this.categoryGateway, this.genreGateway, this.castMemberGateway, this.mediaResourceGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
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

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        this.mockImageMedia();
        this.mockAudioVideoMedia();

        when(this.gateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(this.gateway).create(argThat(actualVideo ->
                        Objects.equals(expectedTitle, actualVideo.getTitle())
                && Objects.equals(expectedDescription, actualVideo.getDescription())
                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                && Objects.equals(expectedRating, actualVideo.getRating())
                && Objects.equals(expectedOpened, actualVideo.getOpened())
                && Objects.equals(expectedPublished, actualVideo.getPublished())
                && Objects.equals(expectedCategories, actualVideo.getCategories())
                && Objects.equals(expectedGenres, actualVideo.getGenres())
                && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                && Objects.equals(expectedThumbnail.name(), actualVideo.getThumbnail().get().name())
                && Objects.equals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name())
                && actualVideo.getVideo().isPresent()
                && actualVideo.getBanner().isPresent()
                && actualVideo.getTrailer().isPresent()
                && actualVideo.getThumbnail().isPresent()
                && actualVideo.getThumbnailHalf().isPresent()
                ));
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateVideo_shouldReturnVideoid() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedCastMembers = Set.of(
                Fixture.CastMembers.kalil().getId(),
                Fixture.CastMembers.wesley().getId()
        );

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        this.mockImageMedia();
        this.mockAudioVideoMedia();

        when(this.gateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(this.gateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedThumbnail.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && actualVideo.getVideo().isPresent()
                        && actualVideo.getBanner().isPresent()
                        && actualVideo.getTrailer().isPresent()
                        && actualVideo.getThumbnail().isPresent()
                        && actualVideo.getThumbnailHalf().isPresent()
        ));
    }

    @Test
    public void givenAValidCommandWithoutGenres_whenCallsCreateVideo_shouldReturnVideoid() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.of(
                Fixture.CastMembers.kalil().getId(),
                Fixture.CastMembers.wesley().getId()
        );

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        this.mockImageMedia();
        this.mockAudioVideoMedia();

        when(this.gateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(this.gateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedThumbnail.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && actualVideo.getVideo().isPresent()
                        && actualVideo.getBanner().isPresent()
                        && actualVideo.getTrailer().isPresent()
                        && actualVideo.getThumbnail().isPresent()
                        && actualVideo.getThumbnailHalf().isPresent()
        ));
    }

    @Test
    public void givenAValidCommandWithoutCastMembers_whenCallsCreateVideo_shouldReturnVideoid() {
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
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        this.mockImageMedia();
        this.mockAudioVideoMedia();

        when(this.gateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(this.gateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedThumbnail.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && actualVideo.getVideo().isPresent()
                        && actualVideo.getBanner().isPresent()
                        && actualVideo.getTrailer().isPresent()
                        && actualVideo.getThumbnail().isPresent()
                        && actualVideo.getThumbnailHalf().isPresent()
        ));
    }

    @Test
    public void givenAValidCommandWithoutResources_whenCallsCreateVideo_shouldReturnVideoid() {
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

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        when(this.gateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = this.useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(this.gateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenANullTitle_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;
        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).existsByIds(any());
        verify(this.castMemberGateway, times(0)).existsByIds(any());
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenEmptyTitle_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).existsByIds(any());
        verify(this.castMemberGateway, times(0)).existsByIds(any());
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenANullRating_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'ratings' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final String expectedRating = null;
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).existsByIds(any());
        verify(this.castMemberGateway, times(0)).existsByIds(any());
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidRating_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'ratings' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = "ANYTHING";
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).existsByIds(any());
        verify(this.castMemberGateway, times(0)).existsByIds(any());
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenNullLaunchYear_whenCallsCreateVideo_shouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(0)).existsByIds(any());
        verify(this.genreGateway, times(0)).existsByIds(any());
        verify(this.castMemberGateway, times(0)).existsByIds(any());
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeCategoriesDoesNotExist_shouldReturnDomainException() {
        // given
        final var aulaId = Fixture.Categories.aulas().getId();
        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aulaId.getValue());
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(aulaId);
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedCastMembers = Set.of(Fixture.CastMembers.wesley().getId());

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(this.genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(this.castMemberGateway, times(1)).existsByIds(eq(expectedCastMembers));
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeGenresDoesNotExist_shouldReturnDomainException() {
        // given
        final var techId = Fixture.Genres.tech().getId();
        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(techId.getValue());
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(techId);
        final var expectedCastMembers = Set.of(Fixture.CastMembers.wesley().getId());

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(this.genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(this.castMemberGateway, times(1)).existsByIds(eq(expectedCastMembers));
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeCastMembersDoesNotExist_shouldReturnDomainException() {
        // given
        final var wesleyId = Fixture.CastMembers.wesley().getId();
        final var expectedErrorMessage = "Some castMembers could not be found: %s".formatted(wesleyId.getValue());
        final var expectedErrorCount = 1;
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedCastMembers = Set.of(wesleyId);

        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumbnail = null;
        final Resource expectedThumbnailHalf = null;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(this.categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(this.genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(this.castMemberGateway, times(1)).existsByIds(eq(expectedCastMembers));
        verify(this.mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(this.mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(this.gateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndThrowsAnError_shouldCallClearResources() {
        // given
        final var expectedErrorMessage = "An error on create video ocurred";

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

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        // when
        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(this.genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(this.castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMembers));

        this.mockImageMedia();
        this.mockAudioVideoMedia();

        when(this.gateway.create(any()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        final var actualException = assertThrows(InternalErrorException.class, () -> this.useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertTrue(actualException.getMessage().startsWith(expectedErrorMessage));

        verify(this.mediaResourceGateway, times(1)).clearResources(any());
    }

    private void mockImageMedia() {
        when(this.mediaResourceGateway.storeImage(any(), any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return ImageMedia.with(resource.checksum(), resource.name(), "/raw/".concat(resource.name()));
        });
    }

    private void mockAudioVideoMedia() {
        when(this.mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return AudioVideoMedia.with(resource.checksum(), resource.name(), "/raw/".concat(resource.name()));
        });
    }
}
