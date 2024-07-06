package com.fullcycle.admin.catalogo.application.video.media.update;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.video.*;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.TRAILER;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.VIDEO;

/**
 * @author kalil.peixoto
 * @date 7/2/24 21:21
 * @email kalilmvp@gmail.com
 */
public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {
    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aResourceId = aCmd.resourceId();
        final var aFolder = aCmd.folder();
        final var aFilename = aCmd.filename();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> this.notFoundException(anId));

        final var encodedPath = "%s/%s".formatted(aFolder, aFilename);

        if (this.matches(aResourceId, aVideo.getVideo().orElse(null))) {
            this.updateVideo(aCmd.status(), aVideo, VIDEO,  encodedPath);
        } else if (this.matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            this.updateVideo(aCmd.status(), aVideo, TRAILER,  encodedPath);
        }
    }

    private void updateVideo(final MediaStatus aStatus,
                             final Video aVideo,
                             final VideoMediaType aType,
                             final String encodedPath) {
        switch (aStatus) {
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
        }
        this.videoGateway.update(aVideo);
    }

    private boolean matches(String aResourceId, AudioVideoMedia aVideoMedia) {
        if (aVideoMedia == null) return false;
        return aVideoMedia.id().equals(aResourceId);
    }

    private NotFoundException notFoundException(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
