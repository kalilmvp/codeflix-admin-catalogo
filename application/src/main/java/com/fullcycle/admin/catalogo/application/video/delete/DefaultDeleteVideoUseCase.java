package com.fullcycle.admin.catalogo.application.video.delete;

import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:56
 * @email kalilmvp@gmail.com
 */
public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway,
                                     final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = mediaResourceGateway;
    }

    @Override
    public void execute(String anIn) {
        final var aVideoId = VideoID.from(anIn);
        this.videoGateway.deleteById(aVideoId);
        this.mediaResourceGateway.clearResources(aVideoId);
    }
}
