package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author kalil.peixoto
 * @date 6/16/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
public class VideoCastMemberJPAEntity {

    @EmbeddedId
    private VideoCastMemberID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "videoId")
    private VideoJPAEntity video;

    public VideoCastMemberJPAEntity(){}

    public static VideoCastMemberJPAEntity from(final VideoJPAEntity video, final CastMemberID castMemberId) {
        return new VideoCastMemberJPAEntity(
                VideoCastMemberID.from(video.getId(), castMemberId.getValue()),
                video
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberJPAEntity that = (VideoCastMemberJPAEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }

    private VideoCastMemberJPAEntity(VideoCastMemberID id, VideoJPAEntity video) {
        this.id = id;
        this.video = video;
    }

    public VideoCastMemberID getId() {
        return id;
    }

    public void setId(VideoCastMemberID id) {
        this.id = id;
    }

    public VideoJPAEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJPAEntity video) {
        this.video = video;
    }
}
