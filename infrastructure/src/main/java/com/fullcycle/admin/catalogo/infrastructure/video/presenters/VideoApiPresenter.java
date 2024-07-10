package com.fullcycle.admin.catalogo.infrastructure.video.presenters;

import com.fullcycle.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.infrastructure.video.models.AudioVideoMediaResponse;
import com.fullcycle.admin.catalogo.infrastructure.video.models.ImageMediaResponse;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoResponse;

/**
 * @author kalil.peixoto
 * @date 7/10/24 11:59
 * @email kalilmvp@gmail.com
 */
public interface VideoApiPresenter {

    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating(),
                output.createdAt(),
                output.updatedAt(),
                output.categories(),
                output.genres(),
                output.castMembers(),
                present(output.video()),
                present(output.trailer()),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()));
    }

    static AudioVideoMediaResponse present(AudioVideoMedia media) {
        if (media == null) return null;
        return new AudioVideoMediaResponse(media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name());
    }

    static ImageMediaResponse present(ImageMedia media) {
        if (media == null) return null;
        return new ImageMediaResponse(media.id(),
                media.checksum(),
                media.name(),
                media.location());
    }
}
