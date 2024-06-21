package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    private Category aulas;
    private Category tutoriais;

    private Genre tech;
    private Genre western;

    private CastMember kalil;
    private CastMember wesley;

    @BeforeEach
    public void setUp() {
        this.aulas = this.categoryGateway.create(Fixture.Categories.aulas());
        this.tutoriais = this.categoryGateway.create(Fixture.Categories.tutoriais());

        this.tech = this.genreGateway.create(Fixture.Genres.tech());
        this.western = this.genreGateway.create(Fixture.Genres.western());

        this.kalil = this.castMemberGateway.create(Fixture.CastMembers.kalil());
        this.wesley = this.castMemberGateway.create(Fixture.CastMembers.wesley());
    }

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
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(this.aulas.getId());
        final var expectedGenres = Set.of(this.tech.getId());
        final var expectedCastMembers = Set.of(this.kalil.getId());

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

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        // given
        final var aVideo = Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        );

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(this.aulas.getId());
        final var expectedGenres = Set.of(this.tech.getId());
        final var expectedCastMembers = Set.of(this.kalil.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video1.mov", "/videos/video1");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("1234", "video2.mov", "/videos/video2");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner.jpg", "/image/banner");
        final ImageMedia expectedThumbnail = ImageMedia.with("1234", "thumb.jpg", "/image/thumb");
        final ImageMedia expectedThumbnailHalf = ImageMedia.with("12345", "thumHalf.jpg", "/image/thumHalf");

        final var updatedVideo = Video.with(aVideo).update(
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
        final var actualVideo = this.videoGateway.create(updatedVideo);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name());
        assertEquals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(actualVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));

        final var persistedVideo = this.videoRepository.findById(actualVideo.getId().getValue()).get();

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
        assertNotNull(persistedVideo.getCreatedAt());
        assertTrue(persistedVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));
    }

    @Test
    public void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var actualVideo = this.videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        ));

        assertEquals(1, this.videoRepository.count());

        // when
        this.videoGateway.deleteById(actualVideo.getId());

        // then
        assertEquals(0, this.videoRepository.count());
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var actualVideo = this.videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        ));

        assertEquals(1, this.videoRepository.count());

        // when
        this.videoGateway.deleteById(VideoID.unique());

        // then
        assertEquals(1, this.videoRepository.count());
    }

    @Test
    public void givenAValidVideo_whenCallsFindById_shouldReturnIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(this.aulas.getId());
        final var expectedGenres = Set.of(this.tech.getId());
        final var expectedCastMembers = Set.of(this.kalil.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video1.mov", "/videos/video1");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("1234", "video2.mov", "/videos/video2");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner.jpg", "/image/banner");
        final ImageMedia expectedThumbnail = ImageMedia.with("1234", "thumb.jpg", "/image/thumb");
        final ImageMedia expectedThumbnailHalf = ImageMedia.with("12345", "thumHalf.jpg", "/image/thumHalf");

        final var aVideo = this.videoGateway.create(Video.newVideo(
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
                .setThumbnailHalf(expectedThumbnailHalf));

        // when
        final var actualVideo = this.videoGateway.findById(aVideo.getId()).get();

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        assertEquals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name());
        assertEquals(expectedThumbnailHalf.name(), actualVideo.getThumbnailHalf().get().name());
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();

        this.videoGateway.create(Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                Set.of(),
                Set.of(),
                Set.of()
        ));

        // when
        final var actualVideo = this.videoGateway.findById(VideoID.unique());

        // then
        assertNotNull(actualVideo);
        assertTrue(actualVideo.isEmpty());
    }

    /**
     * 9. teste buscando videos que nao possuem relacionamento
     */

    @Test
    public void givenEmptyParams_whenCallFindAll_shouldReturnAllList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenAllParameters_whenCallFindAll_shouldReturnFilteredList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "empreendedorismo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(this.aulas.getId()),
                        Set.of(this.western.getId()),
                        Set.of(this.kalil.getId()));

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }

    @Test
    public void givenEmptyVideos_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenValidCategory_whenCallFindAll_shouldReturnFilteredList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(this.aulas.getId()),
                        Set.of(),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
    }

    @Test
    public void givenValidGenre_whenCallFindAllWithTech_shouldReturnFilteredList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(tech.getId()),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        assertEquals("System Design no Mercado Livre na Prática", actualPage.items().get(1).title());
    }

    @Test
    public void givenValidGenre_whenCallFindAllWithWestern_shouldReturnFilteredList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(western.getId()),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }

    @Test
    public void givenValidGenres_whenCallFindAll_shouldReturnFilteredList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 3;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(tech.getId(), western.getId()),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
        assertEquals("System Design no Mercado Livre na Prática", actualPage.items().get(2).title());
    }

    @Test
    public void givenValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
        // given
        this.mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of(wesley.getId()));

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());

        assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        assertEquals("System Design no Mercado Livre na Prática", actualPage.items().get(1).title());
    }

    @ParameterizedTest
    @CsvSource({
            "system, 0, 10, 1, 1, System Design no Mercado Livre na Prática",
            "micro, 0, 10, 1, 1, Não cometa esses erros ao trabalhar com Microserviços",
            "empreend, 0, 10, 1, 1, Aula de empreendedorismo",
            "testes, 0, 10, 1, 1, 21.1 Implementação dos testes integrados do findAll",
    })
    public void givenValidTerms_whenCallFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedVideo
    ) {
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        // given
        mockVideos();

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedVideo, actualPage.items().get(0).title());

    }

    @ParameterizedTest
    @CsvSource({
            "title, asc, 0, 10, 4, 4, 21.1 Implementação dos testes integrados do findAll",
            "title, desc, 0, 10, 4, 4, System Design no Mercado Livre na Prática",
            "createdAt, asc, 0, 10, 4, 4, System Design no Mercado Livre na Prática",
            "createdAt, desc, 0, 10, 4, 4, Aula de empreendedorismo"
    })
    public void givenValidSortAndDirection_whenCallFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedVideo
    ) {
        // given
        this.mockVideos();

        final var expectedTerms = "";

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedVideo, actualPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 2, 2, 4, 21.1 Implementação dos testes integrados do findAll;Aula de empreendedorismo",
            "1, 2, 2, 4, Não cometa esses erros ao trabalhar com Microserviços;System Design no Mercado Livre na Prática",
    })
    public void givenValidPage_whenCallFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedVideo
    ) {
        // given
        this.mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery =
                new VideoSearchQuery(expectedPage,
                        expectedPerPage,
                        expectedTerms,
                        expectedSort,
                        expectedDirection,
                        Set.of(),
                        Set.of(),
                        Set.of());

        // when
        final var actualPage = this.videoGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName: expectedVideo.split(";")) {
            final var actualName = actualPage.items().get(index).title();
            assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockVideos() {
        this.videoGateway.create(Video.newVideo(
                "System Design no Mercado Livre na Prática",
                Fixture.Videos.description(),
                Year.of(2022),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.of(tutoriais.getId()),
                Set.of(tech.getId()),
                Set.of(wesley.getId(), kalil.getId())));

        this.videoGateway.create(Video.newVideo(
                "Não cometa esses erros ao trabalhar com Microserviços",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        ));

        this.videoGateway.create(Video.newVideo(
                "21.1 Implementação dos testes integrados do findAll",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.<CategoryID>of(aulas.getId()),
                Set.<GenreID>of(tech.getId()),
                Set.<CastMemberID>of(wesley.getId())
        ));

        this.videoGateway.create(Video.newVideo(
                "Aula de empreendedorismo",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.<CategoryID>of(aulas.getId()),
                Set.<GenreID>of(western.getId()),
                Set.<CastMemberID>of(kalil.getId())
        ));
    }
}
