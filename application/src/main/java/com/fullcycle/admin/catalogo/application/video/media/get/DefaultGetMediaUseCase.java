package com.fullcycle.admin.catalogo.application.video.media.get;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

/**
 * @author kalil.peixoto
 * @date 7/1/24 11:22
 * @email kalilmvp@gmail.com
 */
public class DefaultGetMediaUseCase extends GetMediaUseCase {
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = mediaResourceGateway;
    }

    @Override
    public MediaOutput execute(GetMediaCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aType = VideoMediaType.of(aCmd.mediaType())
                .orElseThrow(() -> this.typeNotFound(aCmd.mediaType()));

        final var aResource = this.mediaResourceGateway.getResource(anId, aType)
                .orElseThrow(() -> notFoundException(aCmd.videoId(), aCmd.mediaType()));

        return MediaOutput.with(aResource);
    }

    private NotFoundException typeNotFound(String aMediaType) {
        return NotFoundException.with(new Error("Media type %s does not exist".formatted(aMediaType)));
    }

    private NotFoundException notFoundException(String anId, String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }
}
