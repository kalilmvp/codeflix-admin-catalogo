package com.fullcycle.admin.catalogo.application.video.update;

import com.fullcycle.admin.catalogo.domain.video.Video;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record UpdateVideoOutput(String id) {

    public static UpdateVideoOutput from(final String anId) {
        return new UpdateVideoOutput(anId);
    }

    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}
