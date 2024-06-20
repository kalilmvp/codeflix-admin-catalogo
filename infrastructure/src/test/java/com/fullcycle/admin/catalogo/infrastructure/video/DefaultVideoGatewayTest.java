package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;
    @Autowired
    private CastMemberGateway castMemberGateway;
    @Autowired
    private CategoryGateway categoryGateway;
    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void testInjections() {
        assertNotNull(this.videoGateway);
        assertNotNull(this.castMemberGateway);
        assertNotNull(this.categoryGateway);
        assertNotNull(this.genreGateway);
        assertNotNull(this.videoRepository);
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        final var aulas = this.categoryGateway.create(Fixture.Categories.aulas());
        final var tech = this.genreGateway.create(Fixture.Genres.tech());
        final var kalil = this.castMemberGateway.create(Fixture.CastMembers.kalil());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedCastMembers = Set.of(kalil.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video1.mov", "/videos/video1");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("1234", "video2.mov", "/videos/video2");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner.jpg", "/image/banner");
        final ImageMedia expectedThumbnail = ImageMedia.with("1234", "thumb.jpg", "/image/thumb");
        final ImageMedia expectedThumbnailHalf = ImageMedia.with("12345", "thumHalf.jpg", "/image/thumHalf");

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

        // when
        final var actualResult = this.videoGateway.create(aVideo);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.getId());

        assertEquals(expectedTitle, actualResult.getTitle());
        assertEquals(expectedDescription, actualResult.getDescription());
        assertEquals(expectedLaunchYear, actualResult.getLaunchedAt());
        assertEquals(expectedDuration, actualResult.getDuration());
        assertEquals(expectedRating, actualResult.getRating());
        assertEquals(expectedOpened, actualResult.getOpened());
        assertEquals(expectedPublished, actualResult.getPublished());
        assertEquals(expectedCategories, actualResult.getCategories());
        assertEquals(expectedGenres, actualResult.getGenres());
        assertEquals(expectedCastMembers, actualResult.getCastMembers());
        assertEquals(expectedVideo.name(), actualResult.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualResult.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualResult.getBanner().get().name());
        assertEquals(expectedThumbnailHalf.name(), actualResult.getThumbnailHalf().get().name());
        assertEquals(expectedThumbnailHalf.name(), actualResult.getThumbnailHalf().get().name());

        final var persistedVideo = this.videoRepository.findById(actualResult.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedCategories, persistedVideo.getCategoriesId());
        assertEquals(expectedGenres, persistedVideo.getGenresId());
        assertEquals(expectedCastMembers, persistedVideo.getCastMembersId());
        assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        assertEquals(expectedThumbnailHalf.name(), persistedVideo.getThumbnailHalf().getName());
        assertEquals(expectedThumbnailHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    public void givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMembers = Set.<CastMemberID>of();

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
                );

        // when
        final var actualResult = this.videoGateway.create(aVideo);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.getId());

        assertEquals(expectedTitle, actualResult.getTitle());
        assertEquals(expectedDescription, actualResult.getDescription());
        assertEquals(expectedLaunchYear, actualResult.getLaunchedAt());
        assertEquals(expectedDuration, actualResult.getDuration());
        assertEquals(expectedRating, actualResult.getRating());
        assertEquals(expectedOpened, actualResult.getOpened());
        assertEquals(expectedPublished, actualResult.getPublished());
        assertEquals(expectedCategories, actualResult.getCategories());
        assertEquals(expectedGenres, actualResult.getGenres());
        assertEquals(expectedCastMembers, actualResult.getCastMembers());
        assertTrue(actualResult.getVideo().isEmpty());
        assertTrue(actualResult.getTrailer().isEmpty());
        assertTrue(actualResult.getBanner().isEmpty());
        assertTrue(actualResult.getThumbnail().isEmpty());
        assertTrue(actualResult.getThumbnailHalf().isEmpty());

        final var persistedVideo = this.videoRepository.findById(actualResult.getId().getValue()).get();

        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedCategories, persistedVideo.getCategoriesId());
        assertEquals(expectedGenres, persistedVideo.getGenresId());
        assertEquals(expectedCastMembers, persistedVideo.getCastMembersId());
        assertNull(persistedVideo.getVideo());
        assertNull(persistedVideo.getTrailer());
        assertNull(persistedVideo.getBanner());
        assertNull(persistedVideo.getThumbnail());
        assertNull(persistedVideo.getThumbnailHalf());
    }
}
