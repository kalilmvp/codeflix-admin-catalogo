package com.fullcycle.admin.catalogo.domain.video;

import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 6/18/24 21:41
 * @email kalilmvp@gmail.com
 */
public record VideoPreview(String id,
                           String title,
                           String description,
                           Instant createdAt,
                           Instant updatedAt) {

    public VideoPreview(Video aVideo) {
        this(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }

}
