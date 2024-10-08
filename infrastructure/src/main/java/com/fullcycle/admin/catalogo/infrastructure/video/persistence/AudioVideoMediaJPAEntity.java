package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.UUID;

/**
 * @author kalil.peixoto
 * @date 6/16/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "AudioMediaVideo")
@Table(name = "videos_video_media")
public class AudioVideoMediaJPAEntity {

    @Id
    private String id;
    @Column(name = "checksum", nullable = false)
    private String checksum;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public AudioVideoMediaJPAEntity() {
    }

    private AudioVideoMediaJPAEntity(final String id,
                                     final String checksum,
                                     final String name,
                                     final String filePath,
                                     final String encodedPath,
                                     final MediaStatus status) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJPAEntity from(final AudioVideoMedia aAudioVideoMedia) {
        return new AudioVideoMediaJPAEntity(
                aAudioVideoMedia.id(),
                aAudioVideoMedia.checksum(),
                aAudioVideoMedia.name(),
                aAudioVideoMedia.rawLocation(),
                aAudioVideoMedia.encodedLocation(),
                aAudioVideoMedia.status());
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
                this.getId(),
                this.getChecksum(),
                this.getName(),
                this.getFilePath(),
                this.getEncodedPath(),
                this.getStatus()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }
}
