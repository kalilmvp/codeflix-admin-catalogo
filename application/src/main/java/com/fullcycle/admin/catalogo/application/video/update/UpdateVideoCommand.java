package com.fullcycle.admin.catalogo.application.video.update;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record UpdateVideoCommand(String id,
                                 String title,
                                 String description,
                                 Integer launchedAt,
                                 Double duration,
                                 Boolean opened,
                                 Boolean published,
                                 String rating,
                                 Set<String> categories,
                                 Set<String> genres,
                                 Set<String> castMembers,
                                 Resource video,
                                 Resource banner,
                                 Resource trailer,
                                 Resource thumbnail,
                                 Resource thumbnailHalf) {

    public static UpdateVideoCommand with(String anId,
                                          String aTitle,
                                          String aDescription,
                                          Integer anLaunchedAt,
                                          Double aDuration,
                                          Boolean aOpened,
                                          Boolean aPublished,
                                          String aRating,
                                          Set<String> aCategories,
                                          Set<String> aGenres,
                                          Set<String> aCastMembers,
                                          Resource aVideo,
                                          Resource aTrailer,
                                          Resource aBanner,
                                          Resource aThumbnail,
                                          Resource aThumbnailHalf) {
        return new UpdateVideoCommand(
                anId,
                aTitle,
                aDescription,
                anLaunchedAt,
                aDuration,
                aOpened,
                aPublished,
                aRating,
                aCategories,
                aGenres,
                aCastMembers,
                aVideo,
                aBanner,
                aTrailer,
                aThumbnail,
                aThumbnailHalf);
    }

    public static UpdateVideoCommand with(String anId,
                                          String aTitle,
                                          String aDescription,
                                          Integer anLaunchedAt,
                                          Double aDuration,
                                          Boolean aOpened,
                                          Boolean aPublished,
                                          String aRating,
                                          Set<String> aCategories,
                                          Set<String> aGenres,
                                          Set<String> aCastMembers) {
        return with(
                anId,
                aTitle,
                aDescription,
                anLaunchedAt,
                aDuration,
                aOpened,
                aPublished,
                aRating,
                aCategories,
                aGenres,
                aCastMembers,
                null,
                null,
                null,
                null,
                null);
    }

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(this.video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(this.trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(this.banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(this.thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(this.thumbnailHalf);
    }
}
