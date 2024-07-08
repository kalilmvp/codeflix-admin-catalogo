package com.fullcycle.admin.catalogo.infrastructure.amqp;

import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 7/8/24 12:00
 * @email kalilmvp@gmail.com
 */
@Component
public class VideoEncoderListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoEncoderListener.class);
    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;
    static final String LISTENER_ID = "videoEncoderListener";

    public VideoEncoderListener(UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload String message) {
        final var aResult = Json.readValue(message, VideoEncoderResult.class);

        if (aResult instanceof VideoEncoderCompleted dto) {
            LOGGER.error("[message:video.listener.income] [status:completed] [payload:{}]", message);
            final var aCmd = UpdateMediaStatusCommand.with(
                    MediaStatus.COMPLETED,
                    dto.id(),
                    dto.video().resourceId(),
                    dto.video().encodedVideoFolder(),
                    dto.video().filePath()
            );
            this.updateMediaStatusUseCase.execute(aCmd);
        } else if (aResult instanceof VideoEncoderError) {
            LOGGER.error("[message:video.listener.income] [status:error] [payload:{}]", message);
        } else {
            LOGGER.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
        }
    }
}
