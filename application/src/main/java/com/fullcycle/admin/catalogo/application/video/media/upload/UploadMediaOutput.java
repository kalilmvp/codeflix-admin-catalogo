package com.fullcycle.admin.catalogo.application.video.media.upload;

import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

/**
 * @author kalil.peixoto
 * @date 7/2/24 16:26
 * @email kalilmvp@gmail.com
 */
public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {

    public static UploadMediaOutput with(final Video aVideo, VideoMediaType aMediaType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), aMediaType);
    }

}
