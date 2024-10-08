package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.*;

import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.TRAILER;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.VIDEO;

/**
 * @author kalil.peixoto
 * @date 4/24/24 21:43
 * @email kalilmvp@gmail.com
 */
public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    private Video(final VideoID anId,
                  final String aTitle,
                  final String aDescription,
                  final Year aLaunchYear,
                  final double aDuration,
                  final Rating aRating,
                  final boolean wasOpened,
                  final boolean wasPublished,
                  final Instant aCreationDate,
                  final Instant aUpdateDate,
                  final AudioVideoMedia aVideo,
                  final AudioVideoMedia aTrailer,
                  final ImageMedia aBanner,
                  final ImageMedia aThumb,
                  final ImageMedia aThumbHalf,
                  final Set<CategoryID> categories,
                  final Set<GenreID> genres,
                  final Set<CastMemberID> castMembers,
                  final List<DomainEvent> domainEvents) {
        super(anId, domainEvents);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchYear;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");;
        this.video = aVideo;
        this.trailer = aTrailer;
        this.banner = aBanner;
        this.thumbnail = aThumb;
        this.thumbnailHalf = aThumbHalf;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
    }


    public Video updateBannerMedia(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailMedia(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailHalfMedia(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateTrailerMedia(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();

        this.onAudioVideoMediaUpdated(trailer);

        return this;
    }

    public Video updateVideoMedia(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();

        this.onAudioVideoMediaUpdated(video);

        return this;
    }

    private void onAudioVideoMediaUpdated(AudioVideoMedia media) {
        if (media != null && media.isPendingEncoded()) {
            this.registerEvent(new VideoMediaCreated(this.getId().getValue(), media.rawLocation()));
        }
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
    }

    private void setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
    }

    public static Video newVideo(final String aTitle,
                                 final String aDescription,
                                 final Year aLaunchYear,
                                 final double aDuration,
                                 final Rating aRating,
                                 final boolean wasOpened,
                                 final boolean wasPublished,
                                 final Set<CategoryID> categories,
                                 final Set<GenreID> genres,
                                 final Set<CastMemberID> castMembers) {
        final var now = InstantUtils.now();
        return new Video(VideoID.unique(),
                        aTitle,
                        aDescription,
                        aLaunchYear,
                        aDuration,
                        aRating,
                        wasOpened,
                        wasPublished,
                        now,
                        now,
                        null,
                        null,
                        null,
                        null,
                        null,
                        categories,
                        genres,
                        castMembers,
                        null);
    }

    public Video update(final String aTitle,
                       final String aDescription,
                       final Year aLaunchYear,
                       final double aDuration,
                       final Rating aRating,
                       final boolean wasOpened,
                       final boolean wasPublished,
                       final Set<CategoryID> categories,
                       final Set<GenreID> genres,
                       final Set<CastMemberID> castMembers) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchYear;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(castMembers);

        this.updatedAt = InstantUtils.now();

        return this;
    }

    public static Video with(final Video aVideo) {
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.getRating(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers()),
                aVideo.getDomainEvents()
        );
    }

    public static Video with(final VideoID anId,
                             final String aTitle,
                             final String aDescription,
                             final Year aLaunchYear,
                             final double aDuration,
                             final Rating aRating,
                             final boolean wasOpened,
                             final boolean wasPublished,
                             final Instant aCreationDate,
                             final Instant aUpdateDate,
                             final AudioVideoMedia aVideo,
                             final AudioVideoMedia aTrailer,
                             final ImageMedia aBanner,
                             final ImageMedia aThumb,
                             final ImageMedia aThumbHalf,
                             final Set<CategoryID> categories,
                             final Set<GenreID> genres,
                             final Set<CastMemberID> castMembers) {
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLaunchYear,
                aDuration,
                aRating,
                wasOpened,
                wasPublished,
                aCreationDate,
                aUpdateDate,
                aVideo,
                aTrailer,
                aBanner,
                aThumb,
                aThumbHalf,
                categories,
                genres,
                castMembers,
                null
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean getOpened() {
        return opened;
    }

    public boolean getPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Video processing(VideoMediaType aType) {
        if (VIDEO == aType) {
            this.getVideo().ifPresent(media -> this.updateVideoMedia(media.processing()));
        } else if (TRAILER == aType) {
            this.getTrailer().ifPresent(media -> this.updateTrailerMedia(media.processing()));
        }
        return this;
    }

    public Video completed(VideoMediaType aType, String encodedPath) {
        if (VIDEO == aType) {
            this.getVideo().ifPresent(media -> this.updateVideoMedia(media.completed(encodedPath)));
        } else if (TRAILER == aType) {
            this.getTrailer().ifPresent(media -> this.updateTrailerMedia(media.completed(encodedPath)));
        }
        return this;
    }

}
