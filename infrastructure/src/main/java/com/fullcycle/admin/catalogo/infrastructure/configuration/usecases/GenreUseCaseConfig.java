package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;

/**
 * @author kalil.peixoto
 * @date 02/05/24 05:23
 * @email kalilmvp@gmail.com
 */
@Configuration
public class GenreUseCaseConfig {

  private final GenreGateway genreGateway;
  private final CategoryGateway categoryGateway;

  public GenreUseCaseConfig(GenreGateway genreGateway, CategoryGateway categoryGateway) {
    this.genreGateway = genreGateway;
    this.categoryGateway = categoryGateway;
  }

  @Bean
  public CreateGenreUseCase createGenreUseCase() {
    return new DefaultCreateGenreUseCase(this.genreGateway, this.categoryGateway);
  }

  @Bean
  public DeleteGenreUseCase deleteGenreUseCase() {
    return new DefaultDeleteGenreUseCase(this.genreGateway);
  }

  @Bean
  public GetGenreByIdUseCase getGenreByIdUseCase() {
    return new DefaultGetGenreByIdUseCase(this.genreGateway);
  }

  @Bean
  public ListGenreUseCase listGenreUseCase() {
    return new DefaultListGenreUseCase(this.genreGateway);
  }

  @Bean
  public UpdateGenreUseCase updateGenreUseCase() {
    return new DefaultUpdateGenreUseCase(this.genreGateway, this.categoryGateway);
  }
}
