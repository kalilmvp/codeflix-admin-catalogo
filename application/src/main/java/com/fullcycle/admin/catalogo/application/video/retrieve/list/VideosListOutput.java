package com.fullcycle.admin.catalogo.application.video.retrieve.list;

import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoPreview;

import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 3/12/23 08:28
 * @email kalilmvp@gmail.com
 */
public record VideosListOutput(String id,
                               String title,
                               String description,
                               Instant createdAt,
                               Instant updatedAt) {

    public static VideosListOutput from(Video aVideo) {
        return new VideosListOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt());
    }

    public static VideosListOutput from(VideoPreview aVideo) {
        return new VideosListOutput(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                aVideo.createdAt(),
                aVideo.updatedAt());
    }
}
