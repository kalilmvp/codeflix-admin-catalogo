package com.fullcycle.admin.catalogo.infrastructure.category;

import static com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

/**
 * @author kalil.peixoto
 * @date 3/15/23 22:16
 * @email kalilmvp@gmail.com
 */
@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }


    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {
        String id = anId.getValue();
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.repository.findById(anId.getValue())
                .map(CategoryJPAEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    private Category save(Category aCategory) {
        return this.repository.save(CategoryJPAEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
        // Pagination
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );
        // Busca dinamica pelo criterio terms
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assemBleSpecification).orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJPAEntity::toAggregate).toList());
    }

    @Override
    public List<CategoryID> existsByIds(Iterable<CategoryID> categoryIds) {
        final var ids = StreamSupport.stream(categoryIds.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();
        return this.repository.existsByIds(ids)
                .stream()
                .map(CategoryID::from)
                .toList();
    }

    private Specification<CategoryJPAEntity> assemBleSpecification(final String str) {
        final Specification<CategoryJPAEntity> nameLike = like("name", str);
        final Specification<CategoryJPAEntity> descriptionLike = like("description", str);
        return nameLike.or(descriptionLike);
    }
}
