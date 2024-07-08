package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kalil.peixoto
 * @date 02/05/24 05:23
 * @email kalilmvp@gmail.com
 */
@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public VideoUseCaseConfig(final CategoryGateway categoryGateway,
                              final GenreGateway genreGateway,
                              final CastMemberGateway castMemberGateway,
                              final VideoGateway videoGateway,
                              final MediaResourceGateway mediaResourceGateway) {
        this.categoryGateway = categoryGateway;
        this.genreGateway = genreGateway;
        this.castMemberGateway = castMemberGateway;
        this.videoGateway = videoGateway;
        this.mediaResourceGateway = mediaResourceGateway;
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(this.categoryGateway,
                                             this.genreGateway,
                                             this.castMemberGateway,
                                             this.videoGateway,
                                             this.mediaResourceGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(this.videoGateway);
    }
}
