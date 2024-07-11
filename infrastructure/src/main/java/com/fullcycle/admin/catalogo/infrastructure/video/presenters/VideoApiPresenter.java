package com.fullcycle.admin.catalogo.infrastructure.video.presenters;

import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.VideosListOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.infrastructure.video.models.*;

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

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        if (media == null) return null;
        return new AudioVideoMediaResponse(media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name());
    }

    static ImageMediaResponse present(final ImageMedia media) {
        if (media == null) return null;
        return new ImageMediaResponse(media.id(),
                media.checksum(),
                media.name(),
                media.location());
    }

    static UpdateVideoResponse present(final UpdateVideoOutput updateOutput) {
        return new UpdateVideoResponse(updateOutput.id());
    }

    static VideoListResponse present(final VideosListOutput videosListOutput) {
        return new VideoListResponse(videosListOutput.id(),
                videosListOutput.title(),
                videosListOutput.description(),
                videosListOutput.createdAt(),
                videosListOutput.updatedAt());
    }

    static UploadMediaResponse present(final UploadMediaOutput uploadMediaOutput) {
        return new UploadMediaResponse(
                uploadMediaOutput.videoId(),
                uploadMediaOutput.mediaType()
        );
    }
}
