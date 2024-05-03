package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.video.Video;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final String anId) {
        return new CreateVideoOutput(anId);
    }

    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
