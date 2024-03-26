package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

/**
 * @author kalil.peixoto
 * @date 2/18/23 11:13
 * @email kalilmvp@gmail.com
 */
public interface CastMemberGateway {

    CastMember create(CastMember aCastMember);
    void deleteById(CastMemberID anId);
    Optional<CastMember> findById(CastMemberID anId);
    CastMember update(CastMember aCastMember);
    Pagination<CastMember> findAll(SearchQuery aQuery);
}
