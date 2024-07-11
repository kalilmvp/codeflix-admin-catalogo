package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.media.get.MediaOutput;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideosUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.VideosListOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.utils.CollectionUtils;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.api.controllers.VideoController;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.List;
import java.util.Set;

import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.*;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.TRAILER;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoController.class)
public class VideoAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private ListVideosUseCase listVideosUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private GetMediaUseCase getMediaUseCase;

    @MockBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        // given
        final var categoryAula = Fixture.Categories.aulas();
        final var genreTech = Fixture.Genres.tech();
        final var castMemberKalil = Fixture.CastMembers.kalil();
        final var castMemberWesley = Fixture.CastMembers.wesley();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(categoryAula.getId().getValue());
        final var expectedGenres = Set.of(genreTech.getId().getValue());
        final var expectedCastMembers = Set.of(castMemberKalil.getId().getValue(), castMemberWesley.getId().getValue());

        final var expectedVideo = new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer = new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner = new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumbnail = new MockMultipartFile("thumb_file", "thumb.jpg", "image/jpg", "THUMB".getBytes());
        final var expectedThumbnailHalf = new MockMultipartFile("thumb_half_file", "thumb_half.jpg", "image/jpg", "THUMB_HALF".getBytes());

        when(this.createVideoUseCase.execute(any())).
                thenReturn(new CreateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumbnail)
                .file(expectedThumbnailHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", expectedDuration.toString())
                .param("rating", expectedRating.getName())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("categories_id", categoryAula.getId().getValue())
                .param("genres_id", genreTech.getId().getValue())
                .param("cast_members_id", castMemberKalil.getId().getValue()
                        .concat(",")
                        .concat(castMemberWesley.getId().getValue()))
                .accept(APPLICATION_JSON)
                .contentType(MULTIPART_FORM_DATA);

        this.mockMvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/".concat(expectedId.getValue())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var aCmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);
        verify(this.createVideoUseCase).execute(aCmdCaptor.capture());
        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedCastMembers, actualCmd.castMembers());
        assertEquals(expectedVideo.getOriginalFilename(), actualCmd.getVideo().get().name());
        assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.getTrailer().get().name());
        assertEquals(expectedBanner.getOriginalFilename(), actualCmd.getBanner().get().name());
        assertEquals(expectedThumbnail.getOriginalFilename(), actualCmd.getThumbnail().get().name());
        assertEquals(expectedThumbnailHalf.getOriginalFilename(), actualCmd.getThumbnailHalf().get().name());
    }

    @Test
    public void givenAValidCommand_whenCallsCreatePartial_shouldReturnAnId() throws Exception {
        // given
        final var categoryAula = Fixture.Categories.aulas();
        final var genreTech = Fixture.Genres.tech();
        final var castMemberKalil = Fixture.CastMembers.kalil();
        final var castMemberWesley = Fixture.CastMembers.wesley();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(categoryAula.getId().getValue());
        final var expectedGenres = Set.of(genreTech.getId().getValue());
        final var expectedCastMembers = Set.of(castMemberKalil.getId().getValue(), castMemberWesley.getId().getValue());

        final var aCmd = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(this.createVideoUseCase.execute(any())).
                thenReturn(new CreateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = post("/videos")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(aCmd));

        this.mockMvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/".concat(expectedId.getValue())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var aCmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);
        verify(this.createVideoUseCase).execute(aCmdCaptor.capture());
        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedCastMembers, actualCmd.castMembers());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbnail().isEmpty());
        assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        // given
        final var categoryAula = Fixture.Categories.aulas();
        final var genreTech = Fixture.Genres.tech();
        final var castMemberKalil = Fixture.CastMembers.kalil();
        final var castMemberWesley = Fixture.CastMembers.wesley();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(categoryAula.getId().getValue());
        final var expectedGenres = Set.of(genreTech.getId().getValue());
        final var expectedCastMembers = Set.of(castMemberKalil.getId().getValue(), castMemberWesley.getId().getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(TRAILER);
        final var expectedBanner = Fixture.Videos.image(BANNER);
        final var expectedThumb = Fixture.Videos.image(THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        CollectionUtils.mapTo(expectedCategories, CategoryID::from),
                        CollectionUtils.mapTo(expectedGenres, GenreID::from),
                        CollectionUtils.mapTo(expectedCastMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(this.getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));

        // when
        final var aRequest = get("/videos/{id}", expectedId)
                .accept(APPLICATION_JSON);

        final var response = this.mockMvc.perform(aRequest);

        // then
        response
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))

                .andExpect(jsonPath("$.categories_id", containsInAnyOrder(expectedCategories.toArray())))
                .andExpect(jsonPath("$.genres_id", containsInAnyOrder(expectedGenres.toArray())))
                .andExpect(jsonPath("$.cast_members_id", containsInAnyOrder(expectedCastMembers.toArray())))

                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.raw_location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.raw_location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))

                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        // given
        final var categoryAula = Fixture.Categories.aulas();
        final var genreTech = Fixture.Genres.tech();
        final var castMemberKalil = Fixture.CastMembers.kalil();
        final var castMemberWesley = Fixture.CastMembers.wesley();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(categoryAula.getId().getValue());
        final var expectedGenres = Set.of(genreTech.getId().getValue());
        final var expectedCastMembers = Set.of(castMemberKalil.getId().getValue(), castMemberWesley.getId().getValue());

        final var aCmd = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(this.updateVideoUseCase.execute(any())).
                thenReturn(new UpdateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = put("/videos/{id}", expectedId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(aCmd));

        this.mockMvc.perform(aRequest)
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/videos/".concat(expectedId.getValue())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var aCmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);
        verify(this.updateVideoUseCase).execute(aCmdCaptor.capture());
        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedCastMembers, actualCmd.castMembers());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbnail().isEmpty());
        assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        // given
        final var categoryAula = Fixture.Categories.aulas();
        final var genreTech = Fixture.Genres.tech();
        final var castMemberKalil = Fixture.CastMembers.kalil();
        final var castMemberWesley = Fixture.CastMembers.wesley();

        final var expectedId = VideoID.unique();
        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedCategories = Set.of(categoryAula.getId().getValue());
        final var expectedGenres = Set.of(genreTech.getId().getValue());
        final var expectedCastMembers = Set.of(castMemberKalil.getId().getValue(), castMemberWesley.getId().getValue());

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorSize = 1;

        final var aCmd = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(this.updateVideoUseCase.execute(any())).
                thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var aRequest = put("/videos/{id}", expectedId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(aCmd));

        final var response = this.mockMvc.perform(aRequest);

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorSize)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(this.updateVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        // given
        final var expectedId = VideoID.unique();

        doNothing().when(this.deleteVideoUseCase).execute(any());

        // when
        final var aRequest = delete("/videos/".concat(expectedId.getValue()));
        final var aResponse = this.mockMvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isNoContent());

        verify(this.deleteVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidParams_whenCallListVideos_shouldReturnListPaginated() throws Exception {
        // given
        final var aVideo = new VideoPreview(Fixture.video());
        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCategories = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCastMembers = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(VideosListOutput.from(aVideo));

        when(this.listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos/")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .queryParam("cast_members_ids", expectedCastMembers)
                .accept(APPLICATION_JSON);

        final var response = this.mockMvc.perform(aRequest);

        // then
        response
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var aCmdCaptor = ArgumentCaptor.forClass(VideoSearchQuery.class);
        verify(this.listVideosUseCase).execute(aCmdCaptor.capture());

        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedPage, actualCmd.page());
        assertEquals(expectedPerPage, actualCmd.perPage());
        assertEquals(expectedSort, actualCmd.sort());
        assertEquals(expectedDirection, actualCmd.direction());
        assertEquals(expectedTerms, actualCmd.terms());
        assertEquals(Set.of(CategoryID.from(expectedCategories)), actualCmd.categories());
        assertEquals(Set.of(GenreID.from(expectedGenres)), actualCmd.genres());
        assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), actualCmd.castMembers());
    }

    @Test
    public void givenEmptyParams_whenCallListVideosWithDefaultValues_shouldReturnListPaginated() throws Exception {
        // given
        final var aVideo = new VideoPreview(Fixture.video());
        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(VideosListOutput.from(aVideo));

        when(this.listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos/")
                .accept(APPLICATION_JSON);

        final var response = this.mockMvc.perform(aRequest);

        // then
        response
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var aCmdCaptor = ArgumentCaptor.forClass(VideoSearchQuery.class);
        verify(this.listVideosUseCase).execute(aCmdCaptor.capture());

        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedPage, actualCmd.page());
        assertEquals(expectedPerPage, actualCmd.perPage());
        assertEquals(expectedSort, actualCmd.sort());
        assertEquals(expectedDirection, actualCmd.direction());
        assertEquals(expectedTerms, actualCmd.terms());
        assertTrue(actualCmd.categories().isEmpty());
        assertTrue(actualCmd.genres().isEmpty());
        assertTrue(actualCmd.castMembers().isEmpty());
    }

    @Test
    public void givenValidVideoIdAndFileType_whenCallMediaById_shouldReturnContent() throws Exception {
        // given
        final var expectedId = VideoID.unique();

        final var expectedMediaType = VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);
        final var expectedMedia = new MediaOutput(expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(this.getMediaUseCase.execute(any())).thenReturn(expectedMedia);

        // when
        final var aRequest = get("/videos/{id}/media/{type}", expectedId.getValue(), expectedMedia.name());
        final var aResponse = this.mockMvc.perform(aRequest);

        // then
        aResponse.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(header().string(CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(header().string(CONTENT_DISPOSITION, "attachment; filename-%s".formatted(expectedMedia.name())))
                .andExpect(content().bytes(expectedMedia.content()));

        final var aCmdCaptor = ArgumentCaptor.forClass(GetMediaCommand.class);
        verify(this.getMediaUseCase).execute(aCmdCaptor.capture());

        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedId.getValue(), actualCmd.videoId());
        assertEquals(expectedMedia.name(), actualCmd.mediaType());
    }

    @Test
    public void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideo = new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(this.uploadMediaUseCase.execute(any())).thenReturn(new UploadMediaOutput(expectedId.getValue(), expectedType));

        // when
        final var aRequest = multipart("/videos/{id}/media/{type}", expectedId.getValue(), expectedType.name())
                .file(expectedVideo)
                .accept(APPLICATION_JSON)
                .contentType(MULTIPART_FORM_DATA);

        final var aResponse = this.mockMvc.perform(aRequest);

        // then
        aResponse.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/videos/%s/media/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.video_id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));


        final var aCmdCaptor = ArgumentCaptor.forClass(UploadMediaCommand.class);
        verify(this.uploadMediaUseCase).execute(aCmdCaptor.capture());

        final var actualCmd = aCmdCaptor.getValue();

        assertEquals(expectedId.getValue(), actualCmd.videoId());
        assertEquals(expectedResource.name(), actualCmd.videoResource().resource().name());
        assertEquals(expectedResource.contentType(), actualCmd.videoResource().resource().contentType());
        assertEquals(expectedType, actualCmd.videoResource().type());
    }

    @Test
    public void givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {
        // given
        final var expectedId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VIDEO);
        final var expectedVideo = new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        // when
        final var aRequest = multipart("/videos/{id}/media/INVALID", expectedId.getValue())
                .file(expectedVideo)
                .accept(APPLICATION_JSON)
                .contentType(MULTIPART_FORM_DATA);

        final var aResponse = this.mockMvc.perform(aRequest);

        // then
        aResponse.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo("Invalid INVALID for VideoMediaType")));
    }
}

