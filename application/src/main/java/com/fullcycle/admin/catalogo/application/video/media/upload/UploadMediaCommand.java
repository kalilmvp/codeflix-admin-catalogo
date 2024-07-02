package com.fullcycle.admin.catalogo.application.video.media.upload;

import com.fullcycle.admin.catalogo.domain.video.VideoResource;

/**
 * @author kalil.peixoto
 * @date 7/2/24 16:25
 * @email kalilmvp@gmail.com
 */
public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {
    public static UploadMediaCommand with(final String aVideoId, final VideoResource aResource) {
        return new UploadMediaCommand(aVideoId, aResource);
    }
}
