package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.DefaultListCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kalil.peixoto
 * @date 3/19/23 10:54
 * @email kalilmvp@gmail.com
 */
@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(this.categoryGateway);
    }

    @Bean
    public ListCategoryUseCase getListCategoryUseCase() {
        return new DefaultListCategoryUseCase(this.categoryGateway);
    }
}
