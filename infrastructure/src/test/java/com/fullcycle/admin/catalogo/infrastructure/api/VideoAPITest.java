package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import com.fullcycle.admin.catalogo.infrastructure.api.controllers.VideoController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoController.class)
public class VideoAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

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
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mockMvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/".concat(expectedId.getValue())))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
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
}
