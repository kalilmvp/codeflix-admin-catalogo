package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.utils.CollectionUtils;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoID;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kalil.peixoto
 * @date 6/16/24 21:32
 * @email kalilmvp@gmail.com
 */
@Entity(name = "Video")
@Table(name = "videos")
public class VideoJPAEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", length = 4000)
    private String description;
    @Column(name = "year_launched")
    private int yearLaunched;
    @Column(name = "opened")
    private boolean opened;
    @Column(name = "published")
    private boolean published;
    @Column(name = "rating")
    private Rating rating;
    @Column(name = "duration", precision = 2)
    private double duration;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private AudioVideoMediaJPAEntity video;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    private AudioVideoMediaJPAEntity trailer;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_id")
    private ImageMediaJPAEntity banner;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private ImageMediaJPAEntity thumbnail;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_half_id")
    private ImageMediaJPAEntity thumbnailHalf;
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCategoryJPAEntity> categories;
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoGenreJPAEntity> genres;
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCastMemberJPAEntity> castMembers;

    public VideoJPAEntity() {
    }

    private VideoJPAEntity(final String id,
                           final String title,
                           final String description,
                           final int yearLaunched,
                           final boolean opened,
                           final boolean published,
                           final Rating rating,
                           final double duration,
                           final Instant createdAt,
                           final Instant updatedAt,
                           final AudioVideoMediaJPAEntity video,
                           final AudioVideoMediaJPAEntity trailer,
                           final ImageMediaJPAEntity banner,
                           final ImageMediaJPAEntity thumbnail,
                           final ImageMediaJPAEntity thumbnailHalf) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = new HashSet<>(3);
        this.genres = new HashSet<>(3);
        this.castMembers = new HashSet<>(3);
    }

    public static VideoJPAEntity from(final Video aVideo) {
        final var entity = new VideoJPAEntity(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getVideo()
                        .map(AudioVideoMediaJPAEntity::from)
                        .orElse(null),
                aVideo.getTrailer()
                        .map(AudioVideoMediaJPAEntity::from)
                        .orElse(null),
                aVideo.getBanner()
                        .map(ImageMediaJPAEntity::from)
                        .orElse(null),
                aVideo.getThumbnail()
                        .map(ImageMediaJPAEntity::from)
                        .orElse(null),
                aVideo.getThumbnailHalf()
                        .map(ImageMediaJPAEntity::from)
                        .orElse(null))
                ;

        aVideo.getCategories().forEach(entity::addCategory);
        aVideo.getGenres().forEach(entity::addGenre);
        aVideo.getCastMembers().forEach(entity::addCastMember);

        return entity;
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(this.getId()),
                this.getTitle(),
                this.getDescription(),
                Year.of(this.getYearLaunched()),
                this.getDuration(),
                this.getRating(),
                this.isOpened(),
                this.isPublished(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                Optional.ofNullable(this.getVideo())
                        .map(AudioVideoMediaJPAEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.getTrailer())
                        .map(AudioVideoMediaJPAEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.getBanner())
                        .map(ImageMediaJPAEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.getThumbnail())
                        .map(ImageMediaJPAEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.getThumbnailHalf())
                        .map(ImageMediaJPAEntity::toDomain)
                        .orElse(null),
                this.getCategories()
                        .stream()
                        .map(it -> CategoryID.from(it.getId().getCategoryId()))
                        .collect(Collectors.toSet()),
                this.getGenres()
                        .stream()
                        .map(it -> GenreID.from(it.getId().getGenreId()))
                        .collect(Collectors.toSet()),
                this.getCastMembers()
                        .stream()
                        .map(it -> CastMemberID.from(it.getId().getCastMemberId()))
                        .collect(Collectors.toSet())
        );
    }

    public void addCategory(final CategoryID anId) {
        this.categories.add(VideoCategoryJPAEntity.from(this, anId));
    }

    public void addGenre(final GenreID anId) {
        this.genres.add(VideoGenreJPAEntity.from(this, anId));
    }

    public void addCastMember(final CastMemberID anId) {
        this.castMembers.add(VideoCastMemberJPAEntity.from(this, anId));
    }

    public Set<CategoryID> getCategoriesId() {
        return CollectionUtils.mapTo(this.getCategories(), it -> CategoryID.from(it.getId().getCategoryId()));
    }

    public Set<GenreID> getGenresId() {
        return CollectionUtils.mapTo(this.getGenres(), it -> GenreID.from(it.getId().getGenreId()));
    }

    public Set<CastMemberID> getCastMembersId() {
        return CollectionUtils.mapTo(this.getCastMembers(), it -> CastMemberID.from(it.getId().getCastMemberId()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public void setYearLaunched(int yearLaunched) {
        this.yearLaunched = yearLaunched;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AudioVideoMediaJPAEntity getVideo() {
        return video;
    }

    public void setVideo(AudioVideoMediaJPAEntity video) {
        this.video = video;
    }

    public AudioVideoMediaJPAEntity getTrailer() {
        return trailer;
    }

    public void setTrailer(AudioVideoMediaJPAEntity trailer) {
        this.trailer = trailer;
    }

    public ImageMediaJPAEntity getBanner() {
        return banner;
    }

    public void setBanner(ImageMediaJPAEntity banner) {
        this.banner = banner;
    }

    public ImageMediaJPAEntity getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageMediaJPAEntity thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageMediaJPAEntity getThumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(ImageMediaJPAEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }

    public Set<VideoCategoryJPAEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<VideoCategoryJPAEntity> categories) {
        this.categories = categories;
    }

    public Set<VideoGenreJPAEntity> getGenres() {
        return genres;
    }

    public void setGenres(Set<VideoGenreJPAEntity> genres) {
        this.genres = genres;
    }

    public Set<VideoCastMemberJPAEntity> getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(Set<VideoCastMemberJPAEntity> castMembers) {
        this.castMembers = castMembers;
    }
}
