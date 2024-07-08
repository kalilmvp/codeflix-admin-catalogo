package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fullcycle.admin.catalogo.JacksonTest;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kalil.peixoto
 * @date 7/8/24 11:16
 * @email kalilmvp@gmail.com
 */
@JacksonTest
public class VideoEncoderResultTest {

    @Autowired
    private JacksonTester<VideoEncoderResult> json;

    @Test
    public void testUnmarshallSuccessResult() throws IOException {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = "COMPLETED";
        final var expectedEncodedVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilepath = "any.mp4";
        final var expectedVideoMetaData = new VideoMetaData(expectedEncodedVideoFolder, expectedResourceId, expectedFilepath);

        final var json = """
                {
                    "id": "%s",
                    "output_bucket_path": "%s",
                    "status": "%s",
                    "video": {
                        "encoded_video_folder": "%s",
                        "resource_id": "%s",
                        "file_path": "%s"
                    }
                }
                """.formatted(expectedId,
                expectedOutputBucket,
                expectedStatus,
                expectedEncodedVideoFolder,
                expectedResourceId,
                expectedFilepath);

        // when
        final var actualResult = this.json.parse(json);

        // then
        assertThat(actualResult)
                .isInstanceOf(VideoEncoderCompleted.class)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
                .hasFieldOrPropertyWithValue("video", expectedVideoMetaData);
    }

    @Test
    public void testMarshallSuccessResult() throws IOException {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedStatus = "COMPLETED";
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedEncodedVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilepath = "any.mp4";
        final var expectedVideoMetaData = new VideoMetaData(expectedEncodedVideoFolder, expectedResourceId, expectedFilepath);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedVideoMetaData);

        // when
        final var actualResult = this.json.write(aResult);

        // then
        assertThat(actualResult)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.output_bucket_path", expectedOutputBucket)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.video.encoded_video_folder", expectedEncodedVideoFolder)
                .hasJsonPathValue("$.video.resource_id", expectedResourceId)
                .hasJsonPathValue("$.video.file_path", expectedFilepath);
    }

    @Test
    public void testUnmarshallErrorResult() throws IOException {
        // given
        final var expectedError = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilepath = "any.mp4";
        final var expectedMessage = new VideoMessage(expectedResourceId, expectedFilepath);

        final var json = """
                {
                    "status": "%s",
                    "error": "%s",
                    "message": {
                        "resource_id": "%s",
                        "file_path": "%s"
                    }
                }
                """.formatted(expectedStatus,
                expectedError,
                expectedResourceId,
                expectedFilepath);

        // when
        final var actualResult = this.json.parse(json);

        // then
        assertThat(actualResult)
                .isInstanceOf(VideoEncoderError.class)
                .hasFieldOrPropertyWithValue("error", expectedError)
                .hasFieldOrPropertyWithValue("message", expectedMessage);
    }

    @Test
    public void testMarshallErrorResult() throws IOException {
        // given
        final var expectedError = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilepath = "any.mp4";
        final var expectedMessage = new VideoMessage(expectedResourceId, expectedFilepath);

        final var aResult = new VideoEncoderError(expectedMessage, expectedError);

        // when
        final var actualResult = this.json.write(aResult);

        // then
        assertThat(actualResult)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.error", expectedError)
                .hasJsonPathValue("$.message.resource_id", expectedResourceId)
                .hasJsonPathValue("$.message.file_path", expectedFilepath);
    }
}
