package com.fullcycle.admin.catalogo.infrastructure.amqp;

import com.fullcycle.admin.catalogo.AmqpTest;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalogo.infrastructure.video.models.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static com.fullcycle.admin.catalogo.infrastructure.amqp.VideoEncoderListener.LISTENER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * @author kalil.peixoto
 * @date 7/8/24 12:18
 * @email kalilmvp@gmail.com
 */
@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Test
    public void givenErrorResult_whenCallListener_shouldProcess() throws InterruptedException {
        // given
        final var expectedError = new VideoEncoderError(new VideoMessage("id", "video.mp4"), "Video not found");
        final var expectedMessage = Json.writeValueAsString(expectedError);

        // when
        this.rabbitTemplate.convertAndSend(this.queueProperties.getRoutingKey(), this.queueProperties.getQueue(), expectedMessage);

        // then
        final var invocationData = this.harness.getNextInvocationDataFor(LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String)invocationData.getArguments()[0];

        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenCompletedResult_whenCallListener_shouldCallUseCase() throws InterruptedException {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedEncodedVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilepath = "any.mp4";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedVideoMetaData = new VideoMetaData(expectedEncodedVideoFolder, expectedResourceId, expectedFilepath);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedVideoMetaData);
        final var expectedMessage = Json.writeValueAsString(aResult);

        doNothing().when(this.updateMediaStatusUseCase).execute(any());

        // when
        this.rabbitTemplate.convertAndSend(this.queueProperties.getRoutingKey(), this.queueProperties.getQueue(), expectedMessage);

        // then
        final var invocationData = this.harness.getNextInvocationDataFor(LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String)invocationData.getArguments()[0];

        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
        verify(this.updateMediaStatusUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();
        assertNotNull(actualCmd);
        assertEquals(expectedStatus, actualCmd.status());
        assertEquals(expectedId, actualCmd.videoId());
        assertEquals(expectedResourceId, actualCmd.resourceId());
        assertEquals(expectedEncodedVideoFolder, actualCmd.folder());
        assertEquals(expectedFilepath, actualCmd.filename());
    }
}
