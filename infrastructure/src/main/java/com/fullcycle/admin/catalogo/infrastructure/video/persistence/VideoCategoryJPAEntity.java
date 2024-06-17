package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoID;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author kalil.peixoto
 * @date 6/16/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "VideoCategory")
@Table(name = "videos_categories")
public class VideoCategoryJPAEntity {

    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "videoId")
    private VideoJPAEntity video;

    public VideoCategoryJPAEntity(){}

    public static VideoCategoryJPAEntity from(final VideoJPAEntity video, final CategoryID categoryID) {
        return new VideoCategoryJPAEntity(
                VideoCategoryID.from(video.getId(), categoryID.getValue()),
                video
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryJPAEntity that = (VideoCategoryJPAEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }

    private VideoCategoryJPAEntity(VideoCategoryID id, VideoJPAEntity video) {
        this.id = id;
        this.video = video;
    }

    public VideoCategoryID getId() {
        return id;
    }

    public void setId(VideoCategoryID id) {
        this.id = id;
    }

    public VideoJPAEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJPAEntity video) {
        this.video = video;
    }
}
