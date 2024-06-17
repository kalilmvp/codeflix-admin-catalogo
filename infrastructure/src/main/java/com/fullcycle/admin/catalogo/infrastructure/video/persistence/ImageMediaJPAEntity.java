package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;

import javax.persistence.*;

/**
 * @author kalil.peixoto
 * @date 6/16/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "ImageMedia")
@Table(name = "videos_image_media")
public class ImageMediaJPAEntity {

    @Id
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "filePath", nullable = false)
    private String filePath;

    public ImageMediaJPAEntity() {
    }

    private ImageMediaJPAEntity(final String id,
                               final String name,
                               final String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public static ImageMediaJPAEntity from(final ImageMedia aImageMedia) {
        return new ImageMediaJPAEntity(
                aImageMedia.checksum(),
                aImageMedia.name(),
                aImageMedia.location());
    }

    public ImageMedia toDomain() {
        return ImageMedia.with(
                this.getId(),
                this.getName(),
                this.getFilePath()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
