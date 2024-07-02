package com.fullcycle.admin.catalogo.application.video.media.update;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 7/2/24 16:42
 * @email kalilmvp@gmail.com
 */
public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.videoGateway);
    }

    @Test
    public void givenCommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign()
                .setVideo(expectedMedia);
        final var expectedId = aVideo.getId();

        when(this.videoGateway.findById(any()))
                .thenReturn(of(aVideo));
        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename);

        // when
        this.useCase.execute(aCmd);

        // then
        verify(this.videoGateway, times(1)).findById(eq(expectedId));

        final var videoCaptor = ArgumentCaptor.forClass(Video.class);

        verify(this.videoGateway, times(1)).update(videoCaptor.capture());

        final var actualVideo = videoCaptor.getValue();

        assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenCommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign()
                .setTrailer(expectedMedia);
        final var expectedId = aVideo.getId();

        when(this.videoGateway.findById(any()))
                .thenReturn(of(aVideo));
        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename);

        // when
        this.useCase.execute(aCmd);

        // then
        verify(this.videoGateway, times(1)).findById(eq(expectedId));

        final var videoCaptor = ArgumentCaptor.forClass(Video.class);

        verify(this.videoGateway, times(1)).update(videoCaptor.capture());

        final var actualVideo = videoCaptor.getValue();

        assertTrue(actualVideo.getVideo().isEmpty());

        final var actualTrailerMedia = actualVideo.getTrailer().get();

        assertEquals(expectedMedia.id(), actualTrailerMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualTrailerMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualTrailerMedia.checksum());
        assertEquals(expectedStatus, actualTrailerMedia.status());
        assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualTrailerMedia.encodedLocation());
    }

    @Test
    public void givenCommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign()
                .setVideo(expectedMedia);
        final var expectedId = aVideo.getId();

        when(this.videoGateway.findById(any()))
                .thenReturn(of(aVideo));
        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename);

        // when
        this.useCase.execute(aCmd);

        // then
        verify(this.videoGateway, times(1)).findById(eq(expectedId));

        final var videoCaptor = ArgumentCaptor.forClass(Video.class);

        verify(this.videoGateway, times(1)).update(videoCaptor.capture());

        final var actualVideo = videoCaptor.getValue();

        assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertTrue(actualVideoMedia.encodedLocation().isEmpty());
    }

    @Test
    public void givenCommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        // given
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign()
                .setTrailer(expectedMedia);
        final var expectedId = aVideo.getId();

        when(this.videoGateway.findById(any()))
                .thenReturn(of(aVideo));
        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFilename);

        // when
        this.useCase.execute(aCmd);

        // then
        verify(this.videoGateway, times(1)).findById(eq(expectedId));

        final var videoCaptor = ArgumentCaptor.forClass(Video.class);

        verify(this.videoGateway, times(1)).update(videoCaptor.capture());

        final var actualVideo = videoCaptor.getValue();

        assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        assertEquals(expectedMedia.id(), actualVideoMedia.id());
        assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        assertEquals(expectedStatus, actualVideoMedia.status());
        assertTrue(actualVideoMedia.encodedLocation().isEmpty());
    }

    @Test
    public void givenCommandForTrailer_whenIsInValid_shouldDoNothing() {
        // given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "media";
        final var expectedFilename = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.systemDesign()
                .setTrailer(expectedMedia);
        final var expectedId = aVideo.getId();

        when(this.videoGateway.findById(any()))
                .thenReturn(of(aVideo));

        final var aCmd = UpdateMediaStatusCommand.with(expectedStatus,
                expectedId.getValue(),
                "RANDOM",
                expectedFolder,
                expectedFilename);

        // when
        this.useCase.execute(aCmd);

        // then
        verify(this.videoGateway, times(0)).update(any());
    }
}
