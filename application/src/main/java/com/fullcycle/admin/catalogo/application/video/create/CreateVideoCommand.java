package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:52
 * @email kalilmvp@gmail.com
 */
public record CreateVideoCommand(String title,
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

    public static CreateVideoCommand with(final String aTitle,
                                          final String aDescription,
                                          final Integer anLaunchedAt,
                                          final Double aDuration,
                                          final Boolean aOpened,
                                          final Boolean aPublished,
                                          final String aRating,
                                          final Set<String> aCategories,
                                          final Set<String> aGenres,
                                          final Set<String> aCastMembers,
                                          final Resource aVideo,
                                          final Resource aTrailer,
                                          final Resource aBanner,
                                          final Resource aThumbnail,
                                          final Resource aThumbnailHalf) {
        return new CreateVideoCommand(
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

    public static CreateVideoCommand with(final String aTitle,
                                          final String aDescription,
                                          final Integer anLaunchedAt,
                                          final Double aDuration,
                                          final Boolean aOpened,
                                          final Boolean aPublished,
                                          final String aRating,
                                          final Set<String> aCategories,
                                          final Set<String> aGenres,
                                          final Set<String> aCastMembers) {
        return with(
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
