package com.fullcycle.admin.catalogo.infrastructure.castmembers;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence.CastMembersJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * @author kalil.peixoto
 * @date 1/13/24 23:34
 * @email kalilmvp@gmail.com
 */
@Component
public class CastMembersMySQLGateway implements CastMemberGateway {
    private CastMemberRepository castMemberRepository;

    public CastMembersMySQLGateway(CastMemberRepository castMemberRepository) {
        this.castMemberRepository = castMemberRepository;
    }

    @Override
    public CastMember create(CastMember aCastMember) {
        return this.save(aCastMember);
    }

    @Override
    public CastMember update(CastMember aCastMember) {
        return this.save(aCastMember);
    }

    private CastMember save(CastMember aCastMember) {
        return this.castMemberRepository.save(CastMembersJPAEntity.from(aCastMember)).toAggregate();
    }

    @Override
    public void deleteById(CastMemberID anId) {
        final var aCastMemberID = anId.getValue();

        if (this.castMemberRepository.existsById(aCastMemberID)) {
            this.castMemberRepository.deleteById(aCastMemberID);
        }
    }

    @Override
    public Optional<CastMember> findById(CastMemberID anId) {
        return this.castMemberRepository.findById(anId.getValue())
            .map(CastMembersJPAEntity::toAggregate);
    }

    @Override
    public Pagination<CastMember> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(aQuery.page()
                                       ,aQuery.perPage(),
                                       Sort.by(Sort.Direction.fromString(aQuery.direction())
                                                                        ,aQuery.sort()));


        final var whereClause = Optional.ofNullable(aQuery.terms())
                                    .filter(str -> !str.isBlank())
                                    .map(this::assemBleSpecification)
                                    .orElse(null);

        final var result = this.castMemberRepository.findAll(where(whereClause), page);

        return new Pagination<>(result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.map(CastMembersJPAEntity::toAggregate)
                        .toList());
    }

    private Specification<CastMembersJPAEntity> assemBleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
