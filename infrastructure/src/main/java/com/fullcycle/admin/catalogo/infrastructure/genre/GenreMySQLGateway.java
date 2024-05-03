package com.fullcycle.admin.catalogo.infrastructure.genre;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 1/13/24 23:34
 * @email kalilmvp@gmail.com
 */
@Component
public class GenreMySQLGateway implements GenreGateway {
    private GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre create(Genre aGenre) {
        return this.save(aGenre);
    }

    private Genre save(Genre aGenre) {
        return this.genreRepository.save(GenreJPAEntity.from(aGenre)).toAggregate();
    }

    @Override
    public void deleteById(GenreID anId) {
        final var aGenreId = anId.getValue();
        
        if (this.genreRepository.existsById(aGenreId)) {
            this.genreRepository.deleteById(aGenreId);
        }
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return this.genreRepository.findById(anId.getValue())
            .map(GenreJPAEntity::toAggregate);
    }

    @Override
    public Genre update(Genre agenGenre) {
        return this.save(agenGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(aQuery.page()
                                       ,aQuery.perPage(),
                                       Sort.by(Sort.Direction.fromString(aQuery.direction())
                                                                        ,aQuery.sort()));


        final var whereClause = Optional.ofNullable(aQuery.terms())
                                    .filter(str -> !str.isBlank())
                                    .map(this::assemBleSpecification)
                                    .orElse(null);

        final var result = this.genreRepository.findAll(where(whereClause), page);

        return new Pagination<>(result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.map(GenreJPAEntity::toAggregate)
                        .toList());
    }

    private Specification<GenreJPAEntity> assemBleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    @Override
    public List<GenreID> existsByIds(final Iterable<GenreID> ids) {
        throw new UnsupportedOperationException();
    }
}
