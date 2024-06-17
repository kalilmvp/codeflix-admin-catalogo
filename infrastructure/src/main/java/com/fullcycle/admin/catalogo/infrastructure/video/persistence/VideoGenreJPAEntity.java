package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.genre.GenreID;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 6/16/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
public class VideoGenreJPAEntity {

    @EmbeddedId
    private VideoGenreID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "videoId")
    private VideoJPAEntity video;

    public VideoGenreJPAEntity(){}

    public static VideoGenreJPAEntity from(final VideoJPAEntity video, final GenreID genreID) {
        return new VideoGenreJPAEntity(
                VideoGenreID.from(video.getId(), genreID.getValue()),
                video
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreJPAEntity that = (VideoGenreJPAEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }

    private VideoGenreJPAEntity(VideoGenreID id, VideoJPAEntity video) {
        this.id = id;
        this.video = video;
    }

    public VideoGenreID getId() {
        return id;
    }

    public void setId(VideoGenreID id) {
        this.id = id;
    }

    public VideoJPAEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJPAEntity video) {
        this.video = video;
    }
}
